package engine

import "crudcodegen/internal/generators"

func ReadProperties(contextPath string) generators.ProjectProperties {
	return generators.ProjectProperties{
		GroupId:    "",
		ArtifactId: "",
	}
}

func WriteProperties(contextPath string, properties generators.ProjectProperties) {

}
