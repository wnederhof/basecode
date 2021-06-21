package generator

import (
	"crudcodegen"
	"crudcodegen/internal/templating"
	"gopkg.in/yaml.v3"
	"os"
)

type Properties struct {
	ArtifactId string `yaml:",omitempty"`
	GroupId    string `yaml:",omitempty"`
}

func GenerateNewProject(groupId string, artifactId string) error {
	projectName := artifactId
	context := make(map[string]interface{})
	properties := Properties{
		GroupId: groupId,
		ArtifactId: artifactId,
	}
	err := writeProperties(properties, projectName)
	if err != nil {
		return err
	}
	provideProjectContextAttributes(context, properties)
	return writeFiles("assets/templates/new", projectName, context)
}

func writeProperties(properties Properties, projectName string) error {
	d, err := yaml.Marshal(&properties)
	if err != nil {
		return err
	}
	return os.WriteFile(projectName+"/crudcodegen.yml", d, os.FileMode.Perm(0644))
}

func writeFiles(sourceDirectory string, targetDirectoryTemplate string, context map[string]interface{}) error {
	err := createDirectoryForContext(targetDirectoryTemplate, context)
	if err != nil {
		return err
	}
	dirEntry, _ := crudcodegen.Assets.ReadDir(sourceDirectory)
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
	targetDirectory := templating.SubstitutePathParamsAndRemovePeb(targetDirectoryTemplate, context)
	println("[D] " + targetDirectory)
	return os.MkdirAll(targetDirectory, os.ModePerm)
}

func writeFileForContext(sourceFile string, targetFileTemplate string, context map[string]interface{}) error {
	targetFile := templating.SubstitutePathParamsAndRemovePeb(targetFileTemplate, context)
	templateContents, err := crudcodegen.Assets.ReadFile(sourceFile)
	if err != nil {
		return err
	}
	contents, err := templating.SubstituteFile(string(templateContents), context)
	if err != nil {
		return err
	}
	println("[F] " + targetFile)
	return os.WriteFile(targetFile, []byte(contents), os.FileMode.Perm(0644))
}
