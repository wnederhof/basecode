package cli

import (
	"errors"
	"github.com/urfave/cli/v2"
	"github.com/wnederhof/basecode/pkg/generator"
	"strings"
)

func parseArgs(cliArgs cli.Args) (generator.Model, error) {
	args := make([]string, cliArgs.Len())
	for i := 0; i < cliArgs.Len(); i++ {
		args[i] = cliArgs.Get(i)
	}
	return parseArgsFromStrings(args)
}

func parseArgsFromStrings(args []string) (generator.Model, error) {
	if len(args) < 2 {
		return generator.Model{}, errors.New("usage: <name> (<fieldName>:<fieldType>)+")
	}
	attributes, err := parseAttributeArgsFromStrings(args[1:])
	if err != nil {
		return generator.Model{}, err
	}
	return generator.Model{
		Name:       args[0],
		Attributes: attributes,
	}, nil
}

func parseAttributeArgs(cliArgs cli.Args) ([]generator.ModelAttribute, error) {
	// TODO test separately
	args := make([]string, cliArgs.Len())
	for i := 0; i < cliArgs.Len(); i++ {
		args[i] = cliArgs.Get(i)
	}
	return parseAttributeArgsFromStrings(args)
}

func parseAttributeArgsFromStrings(attributeArgs []string) ([]generator.ModelAttribute, error) {
	if len(attributeArgs) < 1 {
		return []generator.ModelAttribute{}, errors.New("usage: (<fieldName>:<fieldType>)+")
	}
	attributeCount := len(attributeArgs)
	attributes := make([]generator.ModelAttribute, attributeCount)
	for i := 0; i < attributeCount; i++ {
		parts := strings.Split(attributeArgs[i], ":")
		if len(parts) > 2 {
			return []generator.ModelAttribute{}, errors.New("usage: <name> (<fieldName>:<fieldType>)+")
		}
		var parseTypeResult uint8
		var err error
		if len(parts) == 2 {
			parseTypeResult, err = parseType(parts[1])
		} else {
			parseTypeResult, err = parseType("string")
		}
		if err != nil {
			return []generator.ModelAttribute{}, err
		}
		relation := ""
		if parseTypeResult == generator.RELATIONAL {
			relation = parts[1]
		}
		attributes[i] = generator.ModelAttribute{
			Name:     parts[0],
			Type:     parseTypeResult,
			Relation: relation,
		}
	}
	return attributes, nil
}

func parseType(s string) (uint8, error) {
	if s[0] >= 'A' && s[0] <= 'Z' {
		return generator.RELATIONAL, nil
	}

	cases := []string{
		"string",
		"int",
		"text",
		"date",
		"boolean",
		"string_opt",
		"int_opt",
		"text_opt",
		"date_opt",
		"boolean_opt",
	}

	for i, testcase := range cases {
		if s == testcase {
			return uint8(i), nil
		}
	}
	return 0, errors.New("unknown type " + s)
}
