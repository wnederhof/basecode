package cli

import (
	"crudcodegen/pkg/generator"
	"errors"
	"github.com/urfave/cli/v2"
	"log"
	"os"
)

func Run() {
	app := &cli.App{
		Name:  "crudcodegen",
		Usage: "CrudCodeGen is a full-stack scaffolding generator for Kotlin, Spring Boot, GraphQL, Vue (NuxtJS) and PostgreSQL.",
		Commands: []*cli.Command{
			{
				Name:    "new",
				Aliases: []string{"n"},
				Usage:   "Create a new application",
				Action: func(c *cli.Context) error {
					if c.Args().Len() != 2 {
						return errors.New("required two arguments for: new groupId and artifactId")
					}
					return generator.GenerateNewProject(c.Args().Get(0), c.Args().Get(1))
				},
			},
			{
				Name:    "generate",
				Aliases: []string{"g"},
				Usage:   "Generate parts of the application",
				Subcommands: []*cli.Command{
					{
						Name:    "backend:scaffold",
						Aliases: []string{"bes"},
						Usage:   "Backend Scaffold",
						Action: func(c *cli.Context) error {
							model, err := parseArgs(c.Args())
							if err != nil {
								return err
							}
							return generator.GenerateBackendScaffold(model)
						},
					},
					{
						Name:    "backend:model",
						Aliases: []string{"bem"},
						Usage:   "Model files, including migration script, entity and repository",
						Action: func(c *cli.Context) error {
							model, err := parseArgs(c.Args())
							if err != nil {
								return err
							}
							return generator.GenerateBackendModel(model)
						},
					},
					{
						Name:    "backend:api",
						Aliases: []string{"bea"},
						Usage:   "GraphQL API (schema and resolvers)",
						Action: func(c *cli.Context) error {
							model, err := parseArgs(c.Args())
							if err != nil {
								return err
							}
							return generator.GenerateBackendApi(model)
						},
					},
					{
						Name:    "backend:service",
						Aliases: []string{"bsv"},
						Usage:   "Service between API and repository",
						Action: func(c *cli.Context) error {
							model, err := parseArgs(c.Args())
							if err != nil {
								return err
							}
							return generator.GenerateBackendService(model)
						},
					},
					{
						Name:    "frontend",
						Aliases: []string{"fe"},
						Usage:   "Frontend Support",
						Action: func(c *cli.Context) error {
							if c.Args().Len() != 0 {
								return errors.New("required zero arguments")
							}
							return generator.GenerateFrontend()
						},
					},
					{
						Name:    "frontend:scaffold",
						Aliases: []string{"fes"},
						Usage:   "Frontend Scaffold (Generate frontend support first)",
						Action: func(c *cli.Context) error {
							model, err := parseArgs(c.Args())
							if err != nil {
								return err
							}
							return generator.GenerateFrontendScaffold(model)
						},
					},
					{
						Name:    "scaffold",
						Aliases: []string{"s"},
						Usage:   "Backend and Frontend Scaffold (Generate frontend support first)",
						Action: func(c *cli.Context) error {
							model, err := parseArgs(c.Args())
							if err != nil {
								return err
							}
							return generator.GenerateScaffold(model)
						},
					},
				},
			},
		},
	}

	err := app.Run(os.Args)
	if err != nil {
		log.Fatal(err)
	}
}
