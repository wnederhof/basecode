package generator

import (
	"crudcodegen"
	"crudcodegen/internal/templating"
	"os"
)

func GenerateNewProject(groupId string, artifactId string) error {
	context := make(map[string]interface{})
	return writeFiles("assets/templates/new", artifactId, context)
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
		err := writeFileForContext(sourceFile, targetFileTemplate, context)
		if err != nil {
			return err
		}
		if e.IsDir() {
			err := writeFiles(sourceFile, targetFileTemplate, context)
			if err != nil {
				return err
			}
		}
	}
	return nil
}

func createDirectoryForContext(targetDirectoryTemplate string, context map[string]interface{}) error {
	targetDirectory := templating.SubstitutePathParams(targetDirectoryTemplate, context)
	return os.MkdirAll(targetDirectory, os.ModePerm)
}

func writeFileForContext(sourceFile string, targetFileTemplate string, context map[string]interface{}) error {
	targetFile := templating.SubstitutePathParams(targetFileTemplate, context)
	templateContents, err := crudcodegen.Assets.ReadFile(sourceFile)
	if err != nil {
		return err
	}
	contents := templating.SubstituteFile(string(templateContents), context)
	return os.WriteFile(targetFile, []byte(contents), os.FileMode.Perm(0644))
}