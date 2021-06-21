package generator

import (
	"embed"
	"gopkg.in/yaml.v3"
	"os"
)

var (
	//go:embed templates
	assets embed.FS
)

type Properties struct {
	ArtifactId string `yaml:",omitempty"`
	GroupId    string `yaml:",omitempty"`
}

func GenerateNewProject(groupId string, artifactId string) error {
	projectName := artifactId
	context := make(map[string]interface{})
	properties := Properties{
		GroupId:    groupId,
		ArtifactId: artifactId,
	}
	err := writeProperties(properties, projectName)
	if err != nil {
		return err
	}
	provideProjectContextAttributes(context, properties)
	return writeFiles("templates/new", projectName, context)
}

func GenerateFrontend() error {
	properties, err := readProperties()
	if err != nil {
		return err
	}
	context := make(map[string]interface{})
	provideProjectContextAttributes(context, properties)
	return writeFiles("templates/new", "", context)
}

func writeProperties(properties Properties, projectName string) error {
	d, err := yaml.Marshal(&properties)
	if err != nil {
		return err
	}
	return os.WriteFile(projectName+"/crudcodegen.yml", d, os.FileMode.Perm(0644))
}

func readProperties() (Properties, error) {
	file, err := os.ReadFile("/crudcodegen.yml")
	if err != nil {
		return Properties{}, err
	}
	properties := Properties{}
	err = yaml.Unmarshal(file, &properties)
	if err != nil {
		return Properties{}, err
	}
	return properties, nil
}

func writeFiles(sourceDirectory string, targetDirectoryTemplate string, context map[string]interface{}) error {
	err := createDirectoryForContext(targetDirectoryTemplate, context)
	if err != nil {
		return err
	}
	dirEntry, _ := assets.ReadDir(sourceDirectory)
	for _, e := range dirEntry {
		sourceFile := sourceDirectory + "/" + e.Name()
		targetFileTemplate := targetDirectoryTemplate + "/" + e.Name()
		if e.IsDir() {
			err := writeFiles(sourceFile, targetFileTemplate, context)
			if err != nil {
				return err
			}
		} else {
			err := writeFileForContext(sourceFile, targetFileTemplate, context)
			if err != nil {
				return err
			}
		}
	}
	return nil
}

func createDirectoryForContext(targetDirectoryTemplate string, context map[string]interface{}) error {
	targetDirectory := substitutePathParamsAndRemovePeb(targetDirectoryTemplate, context)
	println("[D] " + targetDirectory)
	return os.MkdirAll(targetDirectory, os.ModePerm)
}

func writeFileForContext(sourceFile string, targetFileTemplate string, context map[string]interface{}) error {
	targetFile := substitutePathParamsAndRemovePeb(targetFileTemplate, context)
	templateContents, err := assets.ReadFile(sourceFile)
	if err != nil {
		return err
	}
	contents, err := substituteFile(string(templateContents), context)
	if err != nil {
		return err
	}
	println("[F] " + targetFile)
	return os.WriteFile(targetFile, []byte(contents), os.FileMode.Perm(0644))
}
