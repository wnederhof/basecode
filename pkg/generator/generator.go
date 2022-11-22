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
	provideHelperContextAttributes(context)
	return writeFiles("templates/new", projectName, context, false)
}

func GenerateScaffold(model Model, overwrite bool, delete bool) error {
	err := GenerateBackendScaffold(model, overwrite, delete)
	if err != nil {
		return err
	}
	return GenerateFrontendScaffold(model, overwrite, delete)
}

func GenerateBackendScaffold(model Model, overwrite bool, delete bool) error {
	err := GenerateBackendModel(model, overwrite, delete)
	if err != nil {
		return err
	}
	err = GenerateBackendApi(model, overwrite, delete)
	if err != nil {
		return err
	}
	err = GenerateBackendService(model, overwrite, delete)
	if err != nil {
		return err
	}
	return nil
}

func GenerateBackendModel(model Model, overwrite bool, delete bool) error {
	if delete {
		err := GenerateModelTemplate("templates/entity", model, overwrite, true)
		if err != nil {
			return err
		}
		return GenerateModelTemplate("templates/entity-removal", model, overwrite, false)
	}
	return GenerateModelTemplate("templates/entity", model, overwrite, false)
}

func GenerateBackendApi(model Model, overwrite bool, delete bool) error {
	return GenerateModelTemplate("templates/graphql", model, overwrite, delete)
}

func GenerateBackendService(model Model, overwrite bool, delete bool) error {
	return GenerateModelTemplate("templates/service", model, overwrite, delete)
}

func GenerateFrontend(overwrite bool, delete bool) error {
	properties, err := readProperties()
	if err != nil {
		return err
	}
	context := make(map[string]interface{})
	provideProjectContextAttributes(context, properties)
	provideHelperContextAttributes(context)
	if delete {
		return deleteFiles("templates/react-frontend", ".", context)
	}
	return writeFiles("templates/react-frontend", ".", context, overwrite)
}

func GenerateFrontendScaffold(model Model, overwrite bool, delete bool) error {
	return GenerateModelTemplate("templates/react-frontend-scaffold", model, overwrite, delete)
}

func GenerateModelTemplate(templateDirectory string, model Model, overwrite bool, delete bool) error {
	properties, err := readProperties()
	if err != nil {
		return err
	}
	context := make(map[string]interface{})
	provideProjectContextAttributes(context, properties)
	provideHelperContextAttributes(context)
	provideModelContextAttributes(context, model.Name)
	provideFileContextAttributes(context, properties.ArtifactId, ".")
	provideRelationContextAttributes(context, model.Attributes)
	provideFieldContextAttributes(context, model.Attributes)
	if delete {
		return deleteFiles(templateDirectory, ".", context)
	}
	return writeFiles(templateDirectory, ".", context, overwrite)
}

func GenerateBackendAuthentication(attributes []ModelAttribute, overwrite bool, delete bool) error {
	model := Model{
		Name: "User",
		Attributes: append([]ModelAttribute{{
			Name:     "username",
			Type:     STRING,
			Relation: "",
		}, {
			Name:     "password",
			Type:     STRING,
			Relation: "",
		}}, attributes...),
	}
	if delete {
		err := GenerateModelTemplate("templates/auth", model, overwrite, true)
		if err != nil {
			return err
		}
		return GenerateModelTemplate("templates/auth-removal", model, overwrite, false)
	}
	return GenerateModelTemplate("templates/auth", model, overwrite, false)
}

func GenerateFrontendAuthentication(attributes []ModelAttribute, overwrite bool, delete bool) error {
	model := Model{
		Name: "User",
		Attributes: append([]ModelAttribute{{
			Name:     "username",
			Type:     STRING,
			Relation: "",
		}, {
			Name:     "password",
			Type:     STRING,
			Relation: "",
		}}, attributes...),
	}
	if delete {
		err := GenerateModelTemplate("templates/auth-frontend", model, overwrite, true)
		if err != nil {
			return err
		}
	}
	return GenerateModelTemplate("templates/auth-frontend", model, overwrite, false)
}

func GenerateAuthentication(attributes []ModelAttribute, overwrite bool, delete bool) error {
	// TODO test
	err := GenerateBackendAuthentication(attributes, overwrite, delete)
	if err != nil {
		return err
	}
	return GenerateFrontendAuthentication(attributes, overwrite, delete)
}

func deleteFiles(sourceDirectory string, targetDirectoryTemplate string, context map[string]interface{}) error {
	dirEntry, _ := assets.ReadDir(sourceDirectory)
	for _, e := range dirEntry {
		targetFileTemplate := targetDirectoryTemplate + "/" + e.Name()
		sourceFile := sourceDirectory + "/" + e.Name()

		if e.IsDir() {
			err := deleteFiles(sourceFile, targetFileTemplate, context)
			if err != nil {
				return err
			}
		} else {
			err := deleteFileFromTemplate(targetFileTemplate, context)
			if err != nil {
				return err
			}
		}
	}
	return nil
}

func readProperties() (Properties, error) {
	file, err := os.ReadFile("basecode.yml")
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
	return os.WriteFile(projectName+"/basecode.yml", d, os.FileMode.Perm(0644))
}

func writeFiles(sourceDirectory string, targetDirectoryTemplate string, context map[string]interface{}, overwrite bool) error {
	err := createDirectoryUsingTemplate(targetDirectoryTemplate, context)
	if err != nil {
		return err
	}
	dirEntry, _ := assets.ReadDir(sourceDirectory)
	for _, e := range dirEntry {
		sourceFile := sourceDirectory + "/" + e.Name()
		targetFileTemplate := targetDirectoryTemplate + "/" + e.Name()
		if e.IsDir() {
			err := writeFiles(sourceFile, targetFileTemplate, context, overwrite)
			if err != nil {
				return err
			}
		} else {
			err := writeFileFromTemplate(sourceFile, targetFileTemplate, context, overwrite)
			if err != nil {
				return err
			}
		}
	}
	return nil
}

func exists(name string) bool {
	if _, err := os.Stat(name); err != nil {
		if os.IsNotExist(err) {
			return false
		}
	}
	return true
}

func createDirectoryUsingTemplate(targetDirectoryTemplate string, context map[string]interface{}) error {
	targetDirectory := substitutePathParamsAndRemovePeb(targetDirectoryTemplate, context)
	return os.MkdirAll(targetDirectory, os.ModePerm)
}

func writeFileFromTemplate(sourceFile string, targetFileTemplate string, context map[string]interface{}, overwrite bool) error {
	targetFile := substitutePathParamsAndRemovePeb(targetFileTemplate, context)

	if exists(targetFile) && !overwrite {
		println("[-] " + targetFile + " (already exists)")
		return nil
	} else {
		if !strings.HasSuffix(sourceFile, ".peb") {
			contents, err := assets.ReadFile(sourceFile)
			if err != nil {
				return err
			}
			println("[F] " + targetFile)
			return os.WriteFile(targetFile, contents, os.FileMode.Perm(0644))
		} else {
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
			println("[G] " + targetFile)
			return os.WriteFile(targetFile, []byte(contents), os.FileMode.Perm(0644))
		}
	}
}

func deleteFileFromTemplate(targetFileTemplate string, context map[string]interface{}) error {
	targetFile := substitutePathParamsAndRemovePeb(targetFileTemplate, context)

	if exists(targetFile) {
		println("[D] " + targetFile)
		return os.RemoveAll(targetFile)
	}
	return nil
}
