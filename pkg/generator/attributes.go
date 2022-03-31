package generator

import (
	"github.com/iancoleman/strcase"
	"os"
	"regexp"
	"strconv"
	"strings"
)

const (
	STRING = iota
	INT
	TEXT
	DATE
	DATETIME
	BOOLEAN
	NULL_STRING
	NULL_INT
	NULL_TEXT
	NULL_DATE
	NULL_DATETIME
	NULL_BOOLEAN
	RELATIONAL
)

type Model struct {
	Name       string
	Attributes []ModelAttribute
}

type ModelAttribute struct {
	Name     string
	Type     uint8
	Relation string
}

var (
	regexDot = regexp.MustCompile(`\.`)
)

func provideProjectContextAttributes(context map[string]interface{}, properties Properties) {
	context["groupId"] = properties.GroupId
	context["artifactId"] = properties.ArtifactId

	context["groupIdSlashes"] = regexDot.ReplaceAllString(properties.GroupId, "/")
	context["artifactIdSlashes"] = regexDot.ReplaceAllString(properties.ArtifactId, "/")
}

func provideHelperContextAttributes(context map[string]interface{}) {
	context["dot"] = "."
	context["underscore"] = "_"
	context["squareBracketLeft"] = "["
	context["squareBracketRight"] = "]"
}

func provideModelContextAttributes(context map[string]interface{}, name string) {
	provideVariableWithDifferentCases(context, "name", name)
}

func provideFileContextAttributes(context map[string]interface{}, projectName string, rootPath string) {
	res, err := os.ReadDir(rootPath + "/" + projectName + "-server/src/main/resources/db/migration")
	if err != nil {
		context["nextMigrationPrefix"] = "001"
		return
	}
	suffix := strconv.Itoa(len(res) + 1)
	context["nextMigrationPrefix"] = strings.Repeat("0", 3-len(suffix)) + suffix
}

func provideRelationContextAttributes(context map[string]interface{}, attributes []ModelAttribute) {
	for _, attribute := range attributes {
		if attribute.Type == RELATIONAL {
			context["hasRelations"] = true
			return
		}
	}
	context["hasRelations"] = false
}

func provideFieldContextAttributes(context map[string]interface{}, attributes []ModelAttribute) {
	if context["fields"] == nil {
		context["fields"] = make([]map[string]interface{}, len(attributes))
	}
	for i, attribute := range attributes {
		context["fields"].([]map[string]interface{})[i] = make(map[string]interface{}, len(attributes))
		fieldContext := context["fields"].([]map[string]interface{})[i]
		provideVariableWithDifferentCases(fieldContext, "fieldName", attribute.Name)
		provideTypeNames(fieldContext, attribute)
		if attribute.Type == RELATIONAL {
			provideVariableWithDifferentCases(fieldContext, "fieldType", attribute.Relation)
		}
		provideDatabaseDefinitionTypeContextAttributes(fieldContext, attribute)
		provideIsFieldNullableContextAttributes(fieldContext, attribute)
		provideIsFieldRelationalContextAttributes(fieldContext, attribute)
		provideGraphQLFieldTypeContextAttributes(fieldContext, attribute)
		provideKotlinFieldContextAttributes(fieldContext, attribute)
		provideInputFieldContextAttributes(fieldContext, attribute)
		provideVueTemplateContextAttributes(fieldContext, attribute)
	}
}

func provideTypeNames(context map[string]interface{}, attribute ModelAttribute) {
	switch attribute.Type {
	case STRING:
		context["fieldType"] = "STRING"
	case INT:
		context["fieldType"] = "INT"
	case TEXT:
		context["fieldType"] = "TEXT"
	case DATE:
		context["fieldType"] = "DATE"
	case DATETIME:
		context["fieldType"] = "DATETIME"
	case BOOLEAN:
		context["fieldType"] = "BOOLEAN"
	case NULL_STRING:
		context["fieldType"] = "NULL_STRING"
	case NULL_INT:
		context["fieldType"] = "NULL_INT"
	case NULL_TEXT:
		context["fieldType"] = "NULL_TEXT"
	case NULL_DATE:
		context["fieldType"] = "NULL_DATE"
	case NULL_DATETIME:
		context["fieldType"] = "NULL_DATETIME"
	case NULL_BOOLEAN:
		context["fieldType"] = "NULL_BOOLEAN"
	case RELATIONAL:
		context["fieldType"] = "RELATIONAL"
	default:
		panic("Undetermined attribute type.")
	}
}

func provideDatabaseDefinitionTypeContextAttributes(context map[string]interface{}, attribute ModelAttribute) {
	switch attribute.Type {
	case STRING:
		context["fieldDatabaseDefinitionType"] = "VARCHAR(255) NOT NULL"
	case INT:
		context["fieldDatabaseDefinitionType"] = "INT NOT NULL"
	case TEXT:
		context["fieldDatabaseDefinitionType"] = "TEXT NOT NULL"
	case DATE:
		context["fieldDatabaseDefinitionType"] = "DATE NOT NULL"
	case DATETIME:
		context["fieldDatabaseDefinitionType"] = "TIMESTAMP NOT NULL"
	case BOOLEAN:
		context["fieldDatabaseDefinitionType"] = "BOOLEAN NOT NULL"
	case NULL_STRING:
		context["fieldDatabaseDefinitionType"] = "VARCHAR(255) NULL"
	case NULL_INT:
		context["fieldDatabaseDefinitionType"] = "INT NULL"
	case NULL_TEXT:
		context["fieldDatabaseDefinitionType"] = "TEXT NULL"
	case NULL_DATE:
		context["fieldDatabaseDefinitionType"] = "DATE NULL"
	case NULL_DATETIME:
		context["fieldDatabaseDefinitionType"] = "TIMESTAMP NULL"
	case NULL_BOOLEAN:
		context["fieldDatabaseDefinitionType"] = "BOOLEAN NULL"
	case RELATIONAL:
		context["fieldDatabaseDefinitionType"] = "INT NOT NULL"
	default:
		panic("Undetermined attribute type.")
	}
}

func provideIsFieldNullableContextAttributes(context map[string]interface{}, attribute ModelAttribute) {
	switch attribute.Type {
	case STRING:
		context["isFieldNullable"] = false
	case INT:
		context["isFieldNullable"] = false
	case TEXT:
		context["isFieldNullable"] = false
	case DATE:
		context["isFieldNullable"] = false
	case DATETIME:
		context["isFieldNullable"] = false
	case BOOLEAN:
		context["isFieldNullable"] = false
	case NULL_STRING:
		context["isFieldNullable"] = true
	case NULL_INT:
		context["isFieldNullable"] = true
	case NULL_TEXT:
		context["isFieldNullable"] = true
	case NULL_DATE:
		context["isFieldNullable"] = true
	case NULL_DATETIME:
		context["isFieldNullable"] = true
	case NULL_BOOLEAN:
		context["isFieldNullable"] = true
	case RELATIONAL:
		context["isFieldNullable"] = false
	default:
		panic("Undetermined attribute type.")
	}
}

func provideIsFieldRelationalContextAttributes(context map[string]interface{}, attribute ModelAttribute) {
	context["isFieldRelational"] = attribute.Type == RELATIONAL
}

func provideGraphQLFieldTypeContextAttributes(context map[string]interface{}, attribute ModelAttribute) {
	switch attribute.Type {
	case STRING:
		context["nullableGraphQLFieldType"] = "String"
		context["graphQLFieldType"] = "String!"
	case INT:
		context["nullableGraphQLFieldType"] = "Int"
		context["graphQLFieldType"] = "Int!"
	case TEXT:
		context["nullableGraphQLFieldType"] = "String"
		context["graphQLFieldType"] = "String!"
	case DATE:
		context["nullableGraphQLFieldType"] = "Date"
		context["graphQLFieldType"] = "Date!"
	case DATETIME:
		context["nullableGraphQLFieldType"] = "DateTime"
		context["graphQLFieldType"] = "DateTime!"
	case BOOLEAN:
		context["nullableGraphQLFieldType"] = "Boolean"
		context["graphQLFieldType"] = "Boolean!"
	case NULL_STRING:
		context["nullableGraphQLFieldType"] = "String"
		context["graphQLFieldType"] = "String"
	case NULL_INT:
		context["nullableGraphQLFieldType"] = "Int"
		context["graphQLFieldType"] = "Int"
	case NULL_TEXT:
		context["nullableGraphQLFieldType"] = "String"
		context["graphQLFieldType"] = "String"
	case NULL_DATE:
		context["nullableGraphQLFieldType"] = "Date"
		context["graphQLFieldType"] = "Date"
	case NULL_DATETIME:
		context["nullableGraphQLFieldType"] = "DateTime"
		context["graphQLFieldType"] = "DateTime"
	case NULL_BOOLEAN:
		context["nullableGraphQLFieldType"] = "Boolean"
		context["graphQLFieldType"] = "Boolean"
	case RELATIONAL:
		context["nullableGraphQLFieldType"] = "Int"
		context["graphQLFieldType"] = "Int!"
	default:
		panic("Undetermined attribute type.")
	}
}

func provideKotlinFieldContextAttributes(context map[string]interface{}, attribute ModelAttribute) {
	switch attribute.Type {
	case STRING:
		context["fieldKotlinType"] = "String"
		context["fieldKotlinTypeNotNullable"] = "String"
		context["fieldKotlinTestDummyValue"] = "\"Some " + attribute.Name + "\""
	case INT:
		context["fieldKotlinType"] = "Int"
		context["fieldKotlinTypeNotNullable"] = "Int"
		context["fieldKotlinTestDummyValue"] = "1"
	case TEXT:
		context["fieldKotlinAnnotations"] = "@Lob"
		context["fieldKotlinType"] = "String"
		context["fieldKotlinTypeNotNullable"] = "String"
		context["fieldKotlinTestDummyValue"] = "\"Some streetName\""
	case DATE:
		context["fieldKotlinType"] = "LocalDate"
		context["fieldKotlinTypeNotNullable"] = "LocalDate"
		context["fieldKotlinTestDummyValue"] = "LocalDate.of(2000, 1, 1)"
	case DATETIME:
		context["fieldKotlinType"] = "LocalDateTime"
		context["fieldKotlinTypeNotNullable"] = "LocalDateTime"
		context["fieldKotlinTestDummyValue"] = "LocalDateTime.of(2000, 1, 1, 0, 0)"
	case BOOLEAN:
		context["fieldKotlinType"] = "Boolean"
		context["fieldKotlinTypeNotNullable"] = "Boolean"
		context["fieldKotlinTestDummyValue"] = "true"
	case NULL_STRING:
		context["fieldKotlinType"] = "String?"
		context["fieldKotlinTypeNotNullable"] = "String"
		context["fieldKotlinTestDummyValue"] = "\"Some streetName\""
	case NULL_INT:
		context["fieldKotlinType"] = "Int?"
		context["fieldKotlinTypeNotNullable"] = "Int"
		context["fieldKotlinTestDummyValue"] = "1"
	case NULL_TEXT:
		context["fieldKotlinAnnotations"] = "@Lob"
		context["fieldKotlinType"] = "String?"
		context["fieldKotlinTypeNotNullable"] = "String"
		context["fieldKotlinTestDummyValue"] = "\"Some streetName\""
	case NULL_DATE:
		context["fieldKotlinType"] = "Date?"
		context["fieldKotlinTypeNotNullable"] = "LocalDate"
		context["fieldKotlinTestDummyValue"] = "LocalDate.of(2000, 1, 1)"
	case NULL_DATETIME:
		context["fieldKotlinType"] = "DateTime?"
		context["fieldKotlinTypeNotNullable"] = "LocalDateTime"
		context["fieldKotlinTestDummyValue"] = "LocalDateTime.of(2000, 1, 1, 0, 0)"
	case NULL_BOOLEAN:
		context["fieldKotlinType"] = "Boolean?"
		context["fieldKotlinTypeNotNullable"] = "Boolean"
		context["fieldKotlinTestDummyValue"] = "true"
	case RELATIONAL:
		context["fieldKotlinType"] = "Long"
		context["fieldKotlinTypeNotNullable"] = "Long"
		context["fieldKotlinTestDummyValue"] = "10"
	default:
		panic("Undetermined attribute type.")
	}
}

func provideInputFieldContextAttributes(context map[string]interface{}, attribute ModelAttribute) {
	switch attribute.Type {
	case STRING:
		context["fieldInputType"] = "STRING"
	case INT:
		context["fieldInputType"] = "INT"
	case TEXT:
		context["fieldInputType"] = "TEXT"
	case DATE:
		context["fieldInputType"] = "DATE"
	case DATETIME:
		context["fieldInputType"] = "DATETIME"
	case BOOLEAN:
		context["fieldInputType"] = "BOOLEAN"
	case NULL_STRING:
		context["fieldInputType"] = "STRING_OPTIONAL"
	case NULL_INT:
		context["fieldInputType"] = "INT_OPTIONAL"
	case NULL_TEXT:
		context["fieldInputType"] = "TEXT_OPTIONAL"
	case NULL_DATE:
		context["fieldInputType"] = "DATE_OPTIONAL"
	case NULL_DATETIME:
		context["fieldInputType"] = "DATETIME_OPTIONAL"
	case NULL_BOOLEAN:
		context["fieldInputType"] = "BOOLEAN_OPTIONAL"
	case RELATIONAL:
		context["fieldInputType"] = "RELATIONAL"
	default:
		panic("Undetermined attribute type.")
	}
}

func provideVueTemplateContextAttributes(context map[string]interface{}, attribute ModelAttribute) {
	switch attribute.Type {
	case STRING:
		context["fieldFrontendTestValue"] = "'Some streetName'"
	case INT:
		context["fieldFrontendTestValue"] = "10"
	case TEXT:
		context["fieldFrontendTestValue"] = "'Some streetName'"
	case DATE:
		context["fieldFrontendTestValue"] = "'2000-01-01'"
	case DATETIME:
		context["fieldFrontendTestValue"] = "'2000-01-01T00:00'"
	case BOOLEAN:
		context["fieldFrontendTestValue"] = "true"
	case NULL_STRING:
		context["fieldFrontendTestValue"] = "'Some streetName'"
	case NULL_INT:
		context["fieldFrontendTestValue"] = "10"
	case NULL_TEXT:
		context["fieldFrontendTestValue"] = "'Some streetName'"
	case NULL_DATE:
		context["fieldFrontendTestValue"] = "'2000-01-01'"
	case NULL_DATETIME:
		context["fieldFrontendTestValue"] = "'2000-01-01T00:00'"
	case NULL_BOOLEAN:
		context["fieldFrontendTestValue"] = "true"
	case RELATIONAL:
		context["fieldFrontendTestValue"] = "1"
	default:
		panic("Undetermined attribute type.")
	}
}

func provideVariableWithDifferentCases(context map[string]interface{}, name string, valueInCamelCase string) {
	context[name+"CamelCase"] = strcase.ToLowerCamel(valueInCamelCase)
	context[name+"PascalCase"] = strcase.ToCamel(valueInCamelCase)
	context[name+"PluralCamelCase"] = strcase.ToLowerCamel(valueInCamelCase) + "s"
	context[name+"LowerCase"] = strings.ToLower(valueInCamelCase)
	context[name+"PluralPascalCase"] = strcase.ToCamel(valueInCamelCase) + "s"
	context[name+"SnakeCase"] = strcase.ToSnake(valueInCamelCase)
	context[name+"PluralSnakeCase"] = strcase.ToSnake(valueInCamelCase) + "s"
	context[name+"KebabCase"] = strcase.ToKebab(valueInCamelCase)
	context[name+"PluralKebabCase"] = strcase.ToKebab(valueInCamelCase) + "s"
	context[name+"ScreamingSnakeCase"] = strcase.ToScreamingSnake(valueInCamelCase)
}
