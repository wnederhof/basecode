package main

import (
	"crudcodegen/internal/generator"
	"errors"
	"github.com/urfave/cli/v2"
	"log"
	"os"
)

func main() {
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
						Name:    "scaffold",
						Aliases: []string{"s"},
						Usage:   "Scaffold",
						Action: func(c *cli.Context) error {
							return nil
						},
					},
					{
						Name:    "be-scaffold",
						Aliases: []string{"bes"},
						Usage:   "Backend Scaffold",
						Action: func(c *cli.Context) error {
							return nil
						},
					},
					{
						Name:    "fe-scaffold",
						Aliases: []string{"fes"},
						Usage:   "Backend Scaffold",
						Action: func(c *cli.Context) error {
							return nil
						},
					}, {
						Name:    "entity",
						Aliases: []string{"e"},
						Usage:   "Entity",
						Action: func(c *cli.Context) error {
							return nil
						},
					},
					{
						Name:    "graphql",
						Aliases: []string{"g"},
						Usage:   "GraphQL",
						Action: func(c *cli.Context) error {
							return nil
						},
					},
					{
						Name:    "service",
						Aliases: []string{"s"},
						Usage:   "Service",
						Action: func(c *cli.Context) error {
							return nil
						},
					},
					{
						Name:    "frontend",
						Aliases: []string{"fe"},
						Usage:   "Frontend",
						Action: func(c *cli.Context) error {
							return nil
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
