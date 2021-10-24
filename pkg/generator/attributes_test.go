package generator

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestProvideProjectContextAttributes(t *testing.T) {
	context := make(map[string]interface{})
	provideProjectContextAttributes(context, Properties{
		ArtifactId: "artifact.id",
		GroupId:    "group.id",
	})
	provideHelperContextAttributes(context)
	assert.Equal(t, "artifact.id", context["artifactId"])
	assert.Equal(t, "group.id", context["groupId"])

	assert.Equal(t, "artifact/id", context["artifactIdSlashes"])
	assert.Equal(t, "group/id", context["groupIdSlashes"])
}

func TestProvideEntityContextAttributes(t *testing.T) {
	context := make(map[string]interface{})

	// Only test a few cases, as it basically just calls the more generic TestProvideVariableWithDifferentCases.
	provideModelContextAttributes(context, "projectManager")

	assert.Equal(t, "projectManager", context["nameCamelCase"])
	assert.Equal(t, "ProjectManager", context["namePascalCase"])
	assert.Equal(t, "projectManagers", context["namePluralCamelCase"])
	assert.Equal(t, "projectmanager", context["nameLowerCase"])
	assert.Equal(t, "ProjectManagers", context["namePluralPascalCase"])
	assert.Equal(t, "project_manager", context["nameSnakeCase"])
	assert.Equal(t, "project_managers", context["namePluralSnakeCase"])
	assert.Equal(t, "project-manager", context["nameKebabCase"])
	assert.Equal(t, "project-managers", context["namePluralKebabCase"])
	assert.Equal(t, "PROJECT_MANAGER", context["nameScreamingSnakeCase"])
}

func TestProvideRelationContextAttributes(t *testing.T) {
	emptyAttributes := []ModelAttribute{
	}

	attributesNoRelations := []ModelAttribute{
		{Name: "name", Type: STRING, Relation: ""},
	}

	attributesWithRelations := []ModelAttribute{
		{Name: "name", Type: RELATIONAL, Relation: "Employee"},
	}

	context := make(map[string]interface{})
	provideRelationContextAttributes(context, emptyAttributes)
	assert.Equal(t, false, context["hasRelations"])

	context = make(map[string]interface{})
	provideRelationContextAttributes(context, attributesNoRelations)
	assert.Equal(t, false, context["hasRelations"])

	context = make(map[string]interface{})
	provideRelationContextAttributes(context, attributesWithRelations)
	assert.Equal(t, true, context["hasRelations"])
}

func TestProvideFieldNameContextAttributes(t *testing.T) {
	context := make(map[string]interface{})

	// One attribute per type.
	attributes := []ModelAttribute{
		{Name: "streetName", Type: STRING, Relation: ""},
		{Name: "streetName", Type: INT, Relation: ""},
		{Name: "streetName", Type: TEXT, Relation: ""},
		{Name: "streetName", Type: DATE, Relation: ""},
		{Name: "streetName", Type: DATETIME, Relation: ""},
		{Name: "streetName", Type: BOOLEAN, Relation: ""},
		{Name: "streetName", Type: NULL_STRING, Relation: ""},
		{Name: "streetName", Type: NULL_INT, Relation: ""},
		{Name: "streetName", Type: NULL_TEXT, Relation: ""},
		{Name: "streetName", Type: NULL_DATE, Relation: ""},
		{Name: "streetName", Type: NULL_DATETIME, Relation: ""},
		{Name: "streetName", Type: NULL_BOOLEAN, Relation: ""},
		{Name: "streetName", Type: RELATIONAL, Relation: "Relation"},
	}

	provideFieldContextAttributes(context, attributes)
	fields := context["fields"].([]map[string]interface{})

	// fieldName
	assert.Equal(t, "streetName", fields[0]["fieldNameCamelCase"])
	assert.Equal(t, "StreetName", fields[0]["fieldNamePascalCase"])
	assert.Equal(t, "streetNames", fields[0]["fieldNamePluralCamelCase"])
	assert.Equal(t, "streetname", fields[0]["fieldNameLowerCase"])
	assert.Equal(t, "StreetNames", fields[0]["fieldNamePluralPascalCase"])
	assert.Equal(t, "street_names", fields[0]["fieldNamePluralSnakeCase"])
	assert.Equal(t, "street-name", fields[0]["fieldNameKebabCase"])
	assert.Equal(t, "street-names", fields[0]["fieldNamePluralKebabCase"])
	assert.Equal(t, "STREET_NAME", fields[0]["fieldNameScreamingSnakeCase"])

	// fieldName
	assert.Equal(t, "streetName", fields[0]["fieldNameCamelCase"])
	assert.Equal(t, "StreetName", fields[0]["fieldNamePascalCase"])
	assert.Equal(t, "streetNames", fields[0]["fieldNamePluralCamelCase"])
	assert.Equal(t, "streetname", fields[0]["fieldNameLowerCase"])
	assert.Equal(t, "StreetNames", fields[0]["fieldNamePluralPascalCase"])
	assert.Equal(t, "street_names", fields[0]["fieldNamePluralSnakeCase"])
	assert.Equal(t, "street-name", fields[0]["fieldNameKebabCase"])
	assert.Equal(t, "street-names", fields[0]["fieldNamePluralKebabCase"])
	assert.Equal(t, "STREET_NAME", fields[0]["fieldNameScreamingSnakeCase"])

	// fieldDatabaseDefinitionType
	assert.Equal(t, "VARCHAR(255) NOT NULL", fields[0]["fieldDatabaseDefinitionType"])
	assert.Equal(t, "INT NOT NULL", fields[1]["fieldDatabaseDefinitionType"])
	assert.Equal(t, "TEXT NOT NULL", fields[2]["fieldDatabaseDefinitionType"])
	assert.Equal(t, "DATE NOT NULL", fields[3]["fieldDatabaseDefinitionType"])
	assert.Equal(t, "TIMESTAMP NOT NULL", fields[4]["fieldDatabaseDefinitionType"])
	assert.Equal(t, "BOOLEAN NOT NULL", fields[5]["fieldDatabaseDefinitionType"])
	assert.Equal(t, "VARCHAR(255) NULL", fields[6]["fieldDatabaseDefinitionType"])
	assert.Equal(t, "INT NULL", fields[7]["fieldDatabaseDefinitionType"])
	assert.Equal(t, "TEXT NULL", fields[8]["fieldDatabaseDefinitionType"])
	assert.Equal(t, "DATE NULL", fields[9]["fieldDatabaseDefinitionType"])
	assert.Equal(t, "TIMESTAMP NULL", fields[10]["fieldDatabaseDefinitionType"])
	assert.Equal(t, "BOOLEAN NULL", fields[11]["fieldDatabaseDefinitionType"])
	assert.Equal(t, "INT NOT NULL", fields[12]["fieldDatabaseDefinitionType"])

	// isFieldNullable
	assert.Equal(t, false, fields[0]["isFieldNullable"])
	assert.Equal(t, false, fields[1]["isFieldNullable"])
	assert.Equal(t, false, fields[2]["isFieldNullable"])
	assert.Equal(t, false, fields[3]["isFieldNullable"])
	assert.Equal(t, false, fields[4]["isFieldNullable"])
	assert.Equal(t, false, fields[5]["isFieldNullable"])
	assert.Equal(t, true, fields[6]["isFieldNullable"])
	assert.Equal(t, true, fields[7]["isFieldNullable"])
	assert.Equal(t, true, fields[8]["isFieldNullable"])
	assert.Equal(t, true, fields[9]["isFieldNullable"])
	assert.Equal(t, true, fields[10]["isFieldNullable"])
	assert.Equal(t, true, fields[11]["isFieldNullable"])
	assert.Equal(t, false, fields[12]["isFieldNullable"])

	// isFieldRelational
	assert.Equal(t, false, fields[0]["isFieldRelational"])
	assert.Equal(t, false, fields[1]["isFieldRelational"])
	assert.Equal(t, false, fields[2]["isFieldRelational"])
	assert.Equal(t, false, fields[3]["isFieldRelational"])
	assert.Equal(t, false, fields[4]["isFieldRelational"])
	assert.Equal(t, false, fields[5]["isFieldRelational"])
	assert.Equal(t, false, fields[6]["isFieldRelational"])
	assert.Equal(t, false, fields[7]["isFieldRelational"])
	assert.Equal(t, false, fields[8]["isFieldRelational"])
	assert.Equal(t, false, fields[9]["isFieldRelational"])
	assert.Equal(t, false, fields[10]["isFieldRelational"])
	assert.Equal(t, false, fields[11]["isFieldRelational"])
	assert.Equal(t, true, fields[12]["isFieldRelational"])

	// nullableGraphQLFieldType
	assert.Equal(t, "String", fields[0]["nullableGraphQLFieldType"])
	assert.Equal(t, "Int", fields[1]["nullableGraphQLFieldType"])
	assert.Equal(t, "String", fields[2]["nullableGraphQLFieldType"])
	assert.Equal(t, "Date", fields[3]["nullableGraphQLFieldType"])
	assert.Equal(t, "DateTime", fields[4]["nullableGraphQLFieldType"])
	assert.Equal(t, "Boolean", fields[5]["nullableGraphQLFieldType"])
	assert.Equal(t, "String", fields[6]["nullableGraphQLFieldType"])
	assert.Equal(t, "Int", fields[7]["nullableGraphQLFieldType"])
	assert.Equal(t, "String", fields[8]["nullableGraphQLFieldType"])
	assert.Equal(t, "Date", fields[9]["nullableGraphQLFieldType"])
	assert.Equal(t, "DateTime", fields[10]["nullableGraphQLFieldType"])
	assert.Equal(t, "Boolean", fields[11]["nullableGraphQLFieldType"])
	assert.Equal(t, "Int", fields[12]["nullableGraphQLFieldType"])

	// graphQLFieldType
	assert.Equal(t, "String!", fields[0]["graphQLFieldType"])
	assert.Equal(t, "Int!", fields[1]["graphQLFieldType"])
	assert.Equal(t, "String!", fields[2]["graphQLFieldType"])
	assert.Equal(t, "Date!", fields[3]["graphQLFieldType"])
	assert.Equal(t, "DateTime!", fields[4]["graphQLFieldType"])
	assert.Equal(t, "Boolean!", fields[5]["graphQLFieldType"])
	assert.Equal(t, "String", fields[6]["graphQLFieldType"])
	assert.Equal(t, "Int", fields[7]["graphQLFieldType"])
	assert.Equal(t, "String", fields[8]["graphQLFieldType"])
	assert.Equal(t, "Date", fields[9]["graphQLFieldType"])
	assert.Equal(t, "DateTime", fields[10]["graphQLFieldType"])
	assert.Equal(t, "Boolean", fields[11]["graphQLFieldType"])
	assert.Equal(t, "Int!", fields[12]["graphQLFieldType"])

	// fieldKotlinAnnotations
	assert.Equal(t, nil, fields[0]["fieldKotlinAnnotations"])
	assert.Equal(t, nil, fields[1]["fieldKotlinAnnotations"])
	assert.Equal(t, "@Lob", fields[2]["fieldKotlinAnnotations"])
	assert.Equal(t, nil, fields[3]["fieldKotlinAnnotations"])
	assert.Equal(t, nil, fields[4]["fieldKotlinAnnotations"])
	assert.Equal(t, nil, fields[5]["fieldKotlinAnnotations"])
	assert.Equal(t, nil, fields[6]["fieldKotlinAnnotations"])
	assert.Equal(t, nil, fields[7]["fieldKotlinAnnotations"])
	assert.Equal(t, "@Lob", fields[8]["fieldKotlinAnnotations"])
	assert.Equal(t, nil, fields[9]["fieldKotlinAnnotations"])
	assert.Equal(t, nil, fields[10]["fieldKotlinAnnotations"])
	assert.Equal(t, nil, fields[11]["fieldKotlinAnnotations"])
	assert.Equal(t, nil, fields[12]["fieldKotlinAnnotations"])

	// fieldKotlinType
	assert.Equal(t, "String", fields[0]["fieldKotlinType"])
	assert.Equal(t, "Int", fields[1]["fieldKotlinType"])
	assert.Equal(t, "String", fields[2]["fieldKotlinType"])
	assert.Equal(t, "Date", fields[3]["fieldKotlinType"])
	assert.Equal(t, "DateTime", fields[4]["fieldKotlinType"])
	assert.Equal(t, "Boolean", fields[5]["fieldKotlinType"])
	assert.Equal(t, "String?", fields[6]["fieldKotlinType"])
	assert.Equal(t, "Int?", fields[7]["fieldKotlinType"])
	assert.Equal(t, "String?", fields[8]["fieldKotlinType"])
	assert.Equal(t, "Date?", fields[9]["fieldKotlinType"])
	assert.Equal(t, "DateTime?", fields[10]["fieldKotlinType"])
	assert.Equal(t, "Boolean?", fields[11]["fieldKotlinType"])
	assert.Equal(t, "Int", fields[12]["fieldKotlinType"])

	// fieldKotlinTypeNotNullable
	assert.Equal(t, "String", fields[0]["fieldKotlinTypeNotNullable"])
	assert.Equal(t, "Int", fields[1]["fieldKotlinTypeNotNullable"])
	assert.Equal(t, "String", fields[2]["fieldKotlinTypeNotNullable"])
	assert.Equal(t, "Date", fields[3]["fieldKotlinTypeNotNullable"])
	assert.Equal(t, "DateTime", fields[4]["fieldKotlinTypeNotNullable"])
	assert.Equal(t, "Boolean", fields[5]["fieldKotlinTypeNotNullable"])
	assert.Equal(t, "String", fields[6]["fieldKotlinTypeNotNullable"])
	assert.Equal(t, "Int", fields[7]["fieldKotlinTypeNotNullable"])
	assert.Equal(t, "String", fields[8]["fieldKotlinTypeNotNullable"])
	assert.Equal(t, "Date", fields[9]["fieldKotlinTypeNotNullable"])
	assert.Equal(t, "DateTime", fields[10]["fieldKotlinTypeNotNullable"])
	assert.Equal(t, "Boolean", fields[11]["fieldKotlinTypeNotNullable"])
	assert.Equal(t, "Int", fields[12]["fieldKotlinTypeNotNullable"])

	// fieldKotlinTestDummyValue
	assert.Equal(t, "\"Some streetName\"", fields[0]["fieldKotlinTestDummyValue"])
	assert.Equal(t, "1", fields[1]["fieldKotlinTestDummyValue"])
	assert.Equal(t, "\"Some streetName\"", fields[2]["fieldKotlinTestDummyValue"])
	assert.Equal(t, "LocalDate.of(2000, 1, 1)", fields[3]["fieldKotlinTestDummyValue"])
	assert.Equal(t, "LocalDateTime.of(2000, 1, 1, 0, 0)", fields[4]["fieldKotlinTestDummyValue"])
	assert.Equal(t, "true", fields[5]["fieldKotlinTestDummyValue"])
	assert.Equal(t, "\"Some streetName\"", fields[6]["fieldKotlinTestDummyValue"])
	assert.Equal(t, "1", fields[7]["fieldKotlinTestDummyValue"])
	assert.Equal(t, "\"Some streetName\"", fields[8]["fieldKotlinTestDummyValue"])
	assert.Equal(t, "LocalDate.of(2000, 1, 1)", fields[9]["fieldKotlinTestDummyValue"])
	assert.Equal(t, "LocalDateTime.of(2000, 1, 1, 0, 0)", fields[10]["fieldKotlinTestDummyValue"])
	assert.Equal(t, "true", fields[11]["fieldKotlinTestDummyValue"])
	assert.Equal(t, "10", fields[12]["fieldKotlinTestDummyValue"])

	// fieldInputType
	assert.Equal(t, "String", fields[0]["fieldInputType"])
	assert.Equal(t, "Int", fields[1]["fieldInputType"])
	assert.Equal(t, "Text", fields[2]["fieldInputType"])
	assert.Equal(t, "Date", fields[3]["fieldInputType"])
	assert.Equal(t, "DateTime", fields[4]["fieldInputType"])
	assert.Equal(t, "Boolean", fields[5]["fieldInputType"])
	assert.Equal(t, "String?", fields[6]["fieldInputType"])
	assert.Equal(t, "Int?", fields[7]["fieldInputType"])
	assert.Equal(t, "Text?", fields[8]["fieldInputType"])
	assert.Equal(t, "Date?", fields[9]["fieldInputType"])
	assert.Equal(t, "DateTime?", fields[10]["fieldInputType"])
	assert.Equal(t, "Boolean?", fields[11]["fieldInputType"])
	assert.Equal(t, "Select", fields[12]["fieldInputType"])

	// fieldVueTypescriptTestValue
	assert.Equal(t, "'Some streetName'", fields[0]["fieldVueTypescriptTestValue"])
	assert.Equal(t, "10", fields[1]["fieldVueTypescriptTestValue"])
	assert.Equal(t, "'Some streetName'", fields[2]["fieldVueTypescriptTestValue"])
	assert.Equal(t, "'2000-01-01'", fields[3]["fieldVueTypescriptTestValue"])
	assert.Equal(t, "'2000-01-01T00:00'", fields[4]["fieldVueTypescriptTestValue"])
	assert.Equal(t, "true", fields[5]["fieldVueTypescriptTestValue"])
	assert.Equal(t, "'Some streetName'", fields[6]["fieldVueTypescriptTestValue"])
	assert.Equal(t, "10", fields[7]["fieldVueTypescriptTestValue"])
	assert.Equal(t, "'Some streetName'", fields[8]["fieldVueTypescriptTestValue"])
	assert.Equal(t, "'2000-01-01'", fields[9]["fieldVueTypescriptTestValue"])
	assert.Equal(t, "'2000-01-01T00:00'", fields[10]["fieldVueTypescriptTestValue"])
	assert.Equal(t, "true", fields[11]["fieldVueTypescriptTestValue"])
	assert.Equal(t, "1", fields[12]["fieldVueTypescriptTestValue"])
}

func TestProvideVariableWithDifferentCases(t *testing.T) {
	context := make(map[string]interface{})

	provideVariableWithDifferentCases(context, "name", "projectManager")

	assert.Equal(t, "projectManager", context["nameCamelCase"])
	assert.Equal(t, "ProjectManager", context["namePascalCase"])
	assert.Equal(t, "projectManagers", context["namePluralCamelCase"])
	assert.Equal(t, "projectmanager", context["nameLowerCase"])
	assert.Equal(t, "ProjectManagers", context["namePluralPascalCase"])
	assert.Equal(t, "project_managers", context["namePluralSnakeCase"])
	assert.Equal(t, "project-manager", context["nameKebabCase"])
	assert.Equal(t, "project-managers", context["namePluralKebabCase"])
	assert.Equal(t, "PROJECT_MANAGER", context["nameScreamingSnakeCase"])
}
