package cli

import (
	"errors"
	"github.com/urfave/cli/v2"
	"github.com/wnederhof/basecode/pkg/generator"
)

func Run(args []string) error {
	generatorFlags := []cli.Flag{
		&cli.BoolFlag{
			Name:    "overwrite",
			Aliases: []string{"o"},
		},
		&cli.BoolFlag{
			Name:    "delete",
			Aliases: []string{"d"},
		},
	}
	app := &cli.App{
		Name:  "basecode",
		Usage: "Basecode is a full-stack scaffolding generator for Kotlin, Spring Boot, GraphQL, React (NextJS) and PostgreSQL.",
		Commands: []*cli.Command{
			{
				Name:    "new",
				Aliases: []string{"n"},
				Usage:   "Create a new application",
				Flags: []cli.Flag{
					&cli.StringFlag{
						Name:    "backend",
						Aliases: []string{"b"},
					},
					&cli.StringFlag{
						Name:    "frontend",
						Aliases: []string{"f"},
					},
				},
				Action: func(c *cli.Context) error {
					if c.Args().Len() != 2 {
						return errors.New("required two arguments for: new groupId and artifactId")
					}
					backend := c.String("backend")
					if backend == "" {
						backend = "kotlin"
					}
					frontend := c.String("frontend")
					if frontend == "" {
						frontend = "react"
					}
					return generator.GenerateNewProject(c.Args().Get(0), c.Args().Get(1), backend, frontend)
				},
			},
			{
				Name:    "generate",
				Flags:   generatorFlags,
				Aliases: []string{"g"},
				Usage:   "Generate parts of the application",
				Subcommands: []*cli.Command{
					{
						Name:    "backend:scaffold",
						Aliases: []string{"bes"},
						Usage:   "Backend Scaffold",
						Action: func(c *cli.Context) error {
							model, err := parseArgs(c.Args(), 1)
							if err != nil {
								return err
							}
							return generator.GenerateBackendScaffold(model, c.Bool("overwrite"), c.Bool("delete"))
						},
					},
					{
						Name:    "backend:model",
						Aliases: []string{"bem"},
						Usage:   "Model files, including migration script, entity and repository",
						Action: func(c *cli.Context) error {
							model, err := parseArgs(c.Args(), 1)
							if err != nil {
								return err
							}
							return generator.GenerateBackendModel(model, c.Bool("overwrite"), c.Bool("delete"))
						},
					},
					{
						Name:    "backend:api",
						Aliases: []string{"bea"},
						Usage:   "GraphQL API (schema and resolvers)",
						Action: func(c *cli.Context) error {
							model, err := parseArgs(c.Args(), 1)
							if err != nil {
								return err
							}
							return generator.GenerateBackendApi(model, c.Bool("overwrite"), c.Bool("delete"))
						},
					},
					{
						Name:    "backend:service",
						Aliases: []string{"bsv"},
						Usage:   "Service between API and repository",
						Action: func(c *cli.Context) error {
							model, err := parseArgs(c.Args(), 1)
							if err != nil {
								return err
							}
							return generator.GenerateBackendService(model, c.Bool("overwrite"), c.Bool("delete"))
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
							return generator.GenerateFrontend(c.Bool("overwrite"), c.Bool("delete"))
						},
					},
					{
						Name:    "frontend:scaffold",
						Aliases: []string{"fes"},
						Usage:   "Frontend Scaffold (Generate frontend support first)",
						Action: func(c *cli.Context) error {
							model, err := parseArgs(c.Args(), 1)
							if err != nil {
								return err
							}
							return generator.GenerateFrontendScaffold(model, c.Bool("overwrite"), c.Bool("delete"))
						},
					},
					{
						Name:    "scaffold",
						Aliases: []string{"s"},
						Usage:   "Backend and Frontend Scaffold (Generate frontend support first)",
						Action: func(c *cli.Context) error {
							model, err := parseArgs(c.Args(), 1)
							if err != nil {
								return err
							}
							return generator.GenerateScaffold(model, c.Bool("overwrite"), c.Bool("delete"))
						},
					},
					{
						Name:    "backend:auth",
						Aliases: []string{"ba"},
						Usage:   "Backend Authentication",
						Action: func(c *cli.Context) error {
							model, err := parseAttributeArgs(c.Args(), 0)
							if err != nil {
								return err
							}
							return generator.GenerateBackendAuthentication(model, c.Bool("overwrite"), c.Bool("delete"))
						},
					},
					{
						Name:    "frontend:auth",
						Aliases: []string{"fa"},
						Usage:   "Frontend Authentication",
						Action: func(c *cli.Context) error {
							model, err := parseAttributeArgs(c.Args(), 0)
							if err != nil {
								return err
							}
							return generator.GenerateFrontendAuthentication(model, c.Bool("overwrite"), c.Bool("delete"))
						},
					},
					{
						Name:    "auth",
						Aliases: []string{"a"},
						Usage:   "Authentication",
						Action: func(c *cli.Context) error {
							model, err := parseAttributeArgs(c.Args(), 0)
							if err != nil {
								return err
							}
							return generator.GenerateAuthentication(model, c.Bool("overwrite"), c.Bool("delete"))
						},
					},
				},
			},
		},
	}

	err := app.Run(args)
	if err != nil {
		println(err.Error())
		return err
	}
	return nil
}
