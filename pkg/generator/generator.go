package generator

import (
	"embed"
	"gopkg.in/yaml.v3"
	"os"
	"strings"
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
	err := os.MkdirAll(projectName, os.ModePerm)
	if err != nil {
		return err
	}
	err = writeProperties(properties, projectName)
	if err != nil {
		return err
	}
	provideProjectContextAttributes(context, properties)
	return writeFiles("templates/new", projectName, context)
}

func GenerateScaffold(model Model) error {
	err := GenerateBackendScaffold(model)
	if err != nil {
		return err
	}
	return GenerateFrontendScaffold(model)
}

func GenerateBackendScaffold(model Model) error {
	err := GenerateBackendModel(model)
	if err != nil {
		return err
	}
	err = GenerateBackendApi(model)
	if err != nil {
		return err
	}
	err = GenerateBackendService(model)
	if err != nil {
		return err
	}
	return nil
}

func GenerateBackendModel(model Model) error {
	return GenerateModelTemplate("templates/entity", model)
}

func GenerateBackendApi(model Model) error {
	return GenerateModelTemplate("templates/graphql", model)
}

func GenerateBackendService(model Model) error {
	return GenerateModelTemplate("templates/service", model)
}

func GenerateFrontend() error {
	properties, err := readProperties()
	if err != nil {
		return err
	}
	context := make(map[string]interface{})
	provideProjectContextAttributes(context, properties)
	return writeFiles("templates/new", ".", context)
}

func GenerateFrontendScaffold(model Model) error {
	return GenerateModelTemplate("templates/frontend-scaffold", model)
}

func GenerateModelTemplate(templateDirectory string, model Model) error {
	properties, err := readProperties()
	if err != nil {
		return err
	}
	context := make(map[string]interface{})
	provideProjectContextAttributes(context, properties)
	provideModelContextAttributes(context, model.Name)
	provideFileContextAttributes(context, properties.ArtifactId, ".")
	provideRelationContextAttributes(context, model.Attributes)
	provideFieldContextAttributes(context, model.Attributes)
	return writeFiles(templateDirectory, ".", context)
}

func readProperties() (Properties, error) {
	file, err := os.ReadFile("crudcodegen.yml")
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

func writeProperties(properties Properties, projectName string) error {
	d, err := yaml.Marshal(&properties)
	if err != nil {
		return err
	}
	return os.WriteFile(projectName+"/crudcodegen.yml", d, os.FileMode.Perm(0644))
}

func writeFiles(sourceDirectory string, targetDirectoryTemplate string, context map[string]interface{}) error {
	err := createDirectoryUsingTemplate(targetDirectoryTemplate, context)
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
			err := writeFileFromTemplate(sourceFile, targetFileTemplate, context)
			if err != nil {
				return err
			}
		}
	}
	return nil
}

func createDirectoryUsingTemplate(targetDirectoryTemplate string, context map[string]interface{}) error {
	targetDirectory := substitutePathParamsAndRemovePeb(targetDirectoryTemplate, context)
	println("[D] " + targetDirectory)
	return os.MkdirAll(targetDirectory, os.ModePerm)
}

func writeFileFromTemplate(sourceFile string, targetFileTemplate string, context map[string]interface{}) error {
	targetFile := substitutePathParamsAndRemovePeb(targetFileTemplate, context)
	templateContents, err := assets.ReadFile(sourceFile)
	if err != nil {
		return err
	}
	contents, err := substituteFile(string(templateContents), context)
	if err != nil {
		return err
	}
	if strings.TrimSpace(contents) == "" {
		return nil
	}
	println("[F] " + targetFile)
	return os.WriteFile(targetFile, []byte(contents), os.FileMode.Perm(0644))
}
