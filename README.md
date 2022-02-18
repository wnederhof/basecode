# CrudCodeGen
CrudCodeGen is a full-stack scaffolding generator for Kotlin, Spring Boot, GraphQL, Vue (NuxtJS) and PostgreSQL.

- **Productive**: Generate relational CRUD functionality for the frontend and backend including migrations, GraphQL schema extensions, unit tests and integration tests with a single command.
- **Maintainable**: A package-by-feature backend structure, GraphQL communication and an event-driven backend model make for a highly decoupled and extensible architecture which is built to last.
- **Incremental**: Start with almost no code. Then, once you're ready for the next step, add a GraphQL API, a frontend and more at your own pace.

CrudCodeGen introduces the concept of "non-intrusive relational scaffolding", which is designed to keep your code maintainable, even for entities with 1-N relationships.

- **Relational:** the user may generate generate entities with 1-N relationships.
- **Non-intrusive:** code generated for one entity will not affect code of any another entity, nor will it *change* any other file in the project.

## Installation
Pull the latest crudcodegen from the associated Github releases.

## Usage
### New project
You can create a new project using `ccg new`.

For example:
```
ccg new com.mycorp blog
```

### Generate
Using `ccg generate`, you can generate code based using one of the following generators.
```
   backend:scaffold, bes   Backend Scaffold
   backend:model, bem      Model files, including migration script, entity and repository
   backend:api, bea        GraphQL API (schema and resolvers)
   backend:service, bsv    Service between API and repository
   frontend, fe            Frontend Support
   frontend:scaffold, fes  Frontend Scaffold (Generate frontend support first)
   scaffold, s             Backend and Frontend Scaffold (Generate frontend support first)
```
For more information about the generators, use `-h`:
```
ccg generate scaffold -h
```
In most cases, you will want to use `scaffold`. This generator takes a model name and a list of field names and types and will generate backend and frontend code for the model you specified. For example:
```
ccg generate scaffold Post title:string description:text
```
Available types:
- string
- int 
- text
- date
- datetime
- boolean

For each of these types, you can add `?` to make this type optional. For example: `title:string?`.

# Example
When you want, for example, to generate a blog, you can do that as following:
```
ccg new com.mycorp blog
cd blog
ccg generate scaffold Post title:String
ccg generate scaffold Comment postId:Post comment:String
```
Most generators specify the following parameters:
```
  -d, --delete
  -h, --help
  -o, --overwrite
```
Here:
- `delete` will undo the file generation. This command may also additional generate files, such as migration scripts for dropping a previously created table.
- `overwrite` will overwrite any existing files. When this option is not specified, CrudCodeGen will abort when a file is about to be overwritten.

## Development
For developing your application, you can use `docker-compose up` to spin up a development database. You can then either start the backend using your IDE by running the `main` method in the `Application.kt` file, or start the Spring Boot server using `./mvnw spring-boot:run`. You should be able to access your GraphQL dashboard at: `http://localhost:8080/graphiql`.

To start the frontend, make sure your artifacts are installed using `npm install` and run `npm run dev`.

When both the backend and frontend are running, you can build your next best thing at: `http://localhost:3000`. (Note: as of yet, there is no index page). If you created an entity called `Post`, you will find your scaffolds at: `http://localhost:3000/posts`.

## TODO
- X Use DGS
- X Use NextJS and/or Angular instead of NuxtJS?
- Id as LONG
- Fix SSR using Next-URQL
- Add tests
- Default Tailwind?
- Fix relationships

## License
Licensed under [MIT](LICENSE.md).

## Credits
- The `new` template is based on the code generated using Spring Boot Initializr.
- The `frontend` template is mostly based on the code generated using NuxtJS.
- The `react-frontend` template is mostly based on https://github.com/sanjaytwisk/nextjs-ts.
