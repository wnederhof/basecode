package cli

import (
	"crudcodegen/pkg/generator"
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestParseArgs(t *testing.T) {
	expected := generator.Model{Name: "someModel", Attributes: []generator.ModelAttribute{
		{Name: "someField", Type: generator.STRING, Relation: ""},
		{Name: "someField", Type: generator.INT, Relation: ""},
		{Name: "someField", Type: generator.TEXT, Relation: ""},
		{Name: "someField", Type: generator.DATE, Relation: ""},
		{Name: "someField", Type: generator.DATETIME, Relation: ""},
		{Name: "someField", Type: generator.BOOLEAN, Relation: ""},
		{Name: "someField", Type: generator.NULL_STRING, Relation: ""},
		{Name: "someField", Type: generator.NULL_INT, Relation: ""},
		{Name: "someField", Type: generator.NULL_TEXT, Relation: ""},
		{Name: "someField", Type: generator.NULL_DATE, Relation: ""},
		{Name: "someField", Type: generator.NULL_DATETIME, Relation: ""},
		{Name: "someField", Type: generator.NULL_BOOLEAN, Relation: ""},
		{Name: "someField", Type: generator.RELATIONAL, Relation: "Employer"},
	}}
	actual, _ := parseArgsFromStrings([]string{
		"someModel",
		"someField:string",
		"someField:int",
		"someField:text",
		"someField:date",
		"someField:datetime",
		"someField:boolean",
		"someField:string?",
		"someField:int?",
		"someField:text?",
		"someField:date?",
		"someField:datetime?",
		"someField:boolean?",
		"someField:Employer",
	})
	assert.Equal(t, expected, actual)
}
