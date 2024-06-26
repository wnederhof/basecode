package cli

import (
	"errors"
	"github.com/stretchr/testify/assert"
	"github.com/wnederhof/basecode/pkg/generator"
	"testing"
)

func TestParseArgs(t *testing.T) {
	expected := generator.Model{Name: "someModel", Attributes: []generator.ModelAttribute{
		{Name: "someField", Type: generator.STRING, Relation: ""},
		{Name: "someField", Type: generator.STRING, Relation: ""},
		{Name: "someField", Type: generator.INT, Relation: ""},
		{Name: "someField", Type: generator.TEXT, Relation: ""},
		{Name: "someField", Type: generator.DATE, Relation: ""},
		{Name: "someField", Type: generator.BOOLEAN, Relation: ""},
		{Name: "someField", Type: generator.NULL_STRING, Relation: ""},
		{Name: "someField", Type: generator.NULL_INT, Relation: ""},
		{Name: "someField", Type: generator.NULL_TEXT, Relation: ""},
		{Name: "someField", Type: generator.NULL_DATE, Relation: ""},
		{Name: "someField", Type: generator.NULL_BOOLEAN, Relation: ""},
		{Name: "someField", Type: generator.RELATIONAL, Relation: "Employer"},
	}}
	actual, _ := parseArgsFromStrings([]string{
		"someModel",
		"someField",
		"someField:string",
		"someField:int",
		"someField:text",
		"someField:date",
		"someField:boolean",
		"someField:string_opt",
		"someField:int_opt",
		"someField:text_opt",
		"someField:date_opt",
		"someField:boolean_opt",
		"someField:Employer",
	}, 1)
	assert.Equal(t, expected, actual)
}

func TestParseArgsInvalidInput(t *testing.T) {
	_, err := parseArgsFromStrings([]string{}, 1)
	assert.Equal(t, err, errors.New("usage: <name> (<fieldName>:<fieldType>)+"))

	_, err = parseArgsFromStrings([]string{"test"}, 1)
	assert.Equal(t, err, errors.New("usage: <name> (<fieldName>:<fieldType>)+"))

	_, err = parseArgsFromStrings([]string{"test", "fieldName:something:else"}, 1)
	assert.Equal(t, err, errors.New("usage: <name> (<fieldName>:<fieldType>)+"))

	_, err = parseArgsFromStrings([]string{"test", "fieldName:float"}, 1)
	assert.Equal(t, err, errors.New("unknown type float"))
}
