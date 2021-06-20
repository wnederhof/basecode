package impl

import (
	"crudcodegen/internal/engine"
	"crudcodegen/internal/generators"
)

func GenerateNewProject(groupId string, artifactId string) {
	properties := generators.ProjectProperties{
		GroupId:    groupId,
		ArtifactId: artifactId,
	}
	engine.WriteProperties(artifactId+"/crudcodegen.yml", properties)
	engine.Generate(artifactId, "new", []engine.Variable{})
}
