package cli

import (
	"crudcodegen/pkg/generator"
	"errors"
	"github.com/urfave/cli/v2"
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
	attributeCount := len(args) - 1
	attributes := make([]generator.ModelAttribute, attributeCount)
	for i := 1; i <= attributeCount; i++ {
		parts := strings.Split(args[i], ":")
		parseTypeResult, err := parseType(parts[1])
		if err != nil {
			return generator.Model{}, err
		}
		relation := ""
		if parseTypeResult == generator.RELATIONAL {
			relation = parts[1]
		}
		attributes[i - 1] = generator.ModelAttribute{
			Name:     parts[0],
			Type:     parseTypeResult,
			Relation: relation,
		}
	}

	return generator.Model{
		Name:       args[0],
		Attributes: attributes,
	}, nil
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
		"datetime",
		"boolean",
		"string?",
		"int?",
		"text?",
		"date?",
		"datetime?",
		"boolean?",
	}

	for i, testcase := range cases {
		if s == testcase {
			return uint8(i), nil
		}
	}
	return 0, errors.New("Unknown type " + s)
}
