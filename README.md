# Basecode - The fastest way to create a web app

Basecode is a full-stack code generator for creating web apps using Kotlin, Spring Boot, GraphQL, React (NextJS) and
PostgreSQL. The focus of Basecode is on creating lean, decoupled code with a solid foundation.

Basecode is fully open source and community-driven ([MIT](LICENSE.md)).

## Installation
The following software needs to be installed on your machine before you can use Basecode effectively:

- Go 1.16 or later
- JDK 21 or later
- Node 18 or later
- Docker

Install Basecode using the following command:
```shell
go install github.com/wnederhof/basecode/cmd/basecode@latest
```

## Usage
### Create a New Project
```
basecode new <groupId> <artifactId>
```
Here, `groupId` and `artifactId` are the name of the group and artifact respectively, as defined by Maven.

For example:
```
basecode new com.example blog
```

### Scaffold Generation
Using `basecode generate`, you can generate code based using one of the following generators.
```
   backend:scaffold, bes   Backend Scaffold
   backend:model, bem      Model files, including migration script, entity and repository
   backend:api, bea        GraphQL API (schema and resolvers)
   backend:service, bsv    Service between API and repository
   frontend, fe            Frontend Support
   frontend:scaffold, fes  Frontend Scaffold (Generate frontend support first)
   scaffold, s             Backend and Frontend Scaffold (Generate frontend support first)
   backend:auth, ba        Backend Authentication - EXPERIMENTAL
   frontend:auth, fa       Frontend Authentication - EXPERIMENTAL
```
For more information about the generators, run:
```
basecode generate <generator name> -h
```
For example:
```
basecode generate scaffold -h
```
In most cases, you will want to use `scaffold`. This generator takes a model name and a list of field names and types and will generate backend and frontend code for the model you specified. For example:
```
basecode generate scaffold Post title:string description:text
```
Available types:
- string
- int 
- text
- date
- datetime
- boolean

# Example
When you want, for example, to generate a blog, you can do that as following:
```
basecode new com.mycorp blog
cd blog
basecode generate scaffold Post title contents:text
basecode generate scaffold Comment postId:Post contents:text
```
Most generators specify the following parameters:
```
  -d, --delete
  -h, --help
  -o, --overwrite
```
Here:
- `delete` will undo the file generation. This command may also additional generate files, such as migration scripts for dropping a previously created table.
- `overwrite` will overwrite any existing files. When this option is not specified, Basecode will abort when a file is about to be overwritten.

## After Initialization
After initializing your application, you can use `docker-compose up` to spin up a development database.

You can then either start the backend using your IDE by running the `main` method in the `Application.kt` file, or start the Spring Boot server using `./mvnw spring-boot:run`. You should be able to access your GraphQL dashboard at: `http://localhost:8080/graphiql`.

To start the frontend, make sure your artifacts are installed using `npm install` and run `npm run dev`.

## Credits
- The `new` template is based on the code generated using Spring Boot Initializr.
- The `react-frontend` template is mostly based on https://github.com/sanjaytwisk/nextjs-ts.
