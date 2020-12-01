# CrudCodeGen
CrudCodeGen is designed to automate the repetitive job of writing boilerplate code for many common tasks when creating a web application in Kotlin and Spring Boot.

CrudCodeGen is a tool for generating and managing the basic skeleton of a web application using Kotlin, Spring Boot and zero or more frontends.

Note: CrudCodeGen is currently highly experimental.

Example Usage:
```
# Generate a new blog with groupId `com.myblog` and artifactId `blog`.
ccg new com.myblog.blog

# Generate a Post GraphQL Resolver, Controller, Service, Entity and Repository.
ccg scaffold post title:String

# Generate a Comment GraphQL Resolver, Controller, Service, Entity and Repository.
ccg scaffold comment postId:Post comment:String

# Add and Configure Spring Security.
ccg authentication

# Add a frontend
ccg frontend

# Add a Post Frontend Scaffold
ccg frontend Post

# Add a Comment Frontend Scaffold
ccg frontend Comment
```

NOTE: Run `npm run eslint-fix` after scaffolding
frontend parts of the projects to ensure
that the files are formatted appropriately.

# Installation
`curl ... | sh`

# Design Principles

## Productive
CrudCodeGen should be the go-to tool for quickly setting up a prototype, but also your go-to tool for building large web applications.

## Simple
CrudCodeGen is designed with simplicity and decoupling in mind and to accommodate 80% of the use cases for 20% of the complexity.

## Coherent
CrudCodeGen is designed to provide a great and coherent user experience. As such, CrudCodegen was not designed to be an extensible platform. Instead, all the code for generating websites and the generators themselves are packed in the same repository. Nevertheless, MRs are always welcome.

## Keep it Simple, Yet Scalable
It can be very daunting to generate a new project and start out with many different up-front choices to make and tens of thousands of lines of boilerplate code. And yet, as a project grows, you will need most of those features and you may want even more functionality along the way. As such, when you start out with CrudCodeGen, your projects will only contain the absolute minimum to get your application running, while allowing you to simply "generate features", such as Authentication or a Frontend. This way, your project stays as simple as possible, yet scalable for future endeavours.

## Focus on Decoupling
CrudCodeGen attempts to facilitate a project structure that encourages writing decoupled code, by packaging code by feature and by encouraging the proper use of event listeners.

### Package-by-Feature
All generated controllers / models / entities are packaged by their feature. Instead of having a package named `controllers` where all the controllers live, `services` where all the services live and `entities` where all the entities live, we use packages like `users`, `posts`, etc. This way we make sure that classes that belong together, are packaged together, which should discourage the user to make too much use of classes from other packages and to encourage decoupling features.

### Utilizing Event Listeners
Furthermore, whenever a service is generated, we also generate events. Using events, it should be fairly simple to avoid two-way coupling between packages.

For instance, when a comment depends on a blog post, and the blog post is removed, it is common to just call the comment repository from the blog service to remove the related comments (if there is no delete-cascading strategy in place). However, in this case, comments depend on blog posts, and through this logic, blog posts also depend on comments. By utilizing events, we can just listen to a `PostAwaitingDeletionEvent`, which is triggered when a blog post is deleted, from within the comment package, and remove the comment from over there. If we would want to remove the comments at some point, we can then simply just remove the entire comments folder, without breaking the application.

## Design Decisions
### Package-by-Feature
All code is packaged-by-feature instead of by layer to encourage decoupling different features and creating unidirectional dependencies. To illustrate why it is convenient or perhaps even necessary to use package-by-feature, let us take a look at the following example.

Say we have a blog post and a comment. The comment is usually dependent on the blog post because it refers to it (e.g. by having a `postId` field). In this way, we could build all our relations, by saying that relations between different concepts are always unidirectional. However, when we would use package-by-layer, it would be very tempting when, e.g. someone deletes a blog post, to directly call the `commentService.delete` method, meaning that we don't have a unidirectional dependency graph anymore between the different concepts.

When utilizing a package-by-feature approach, it is much easier to achieve that goal, because the boundaries are much clearer (i.e. the classes in the `comments` package may reference classes in the `posts` package, but never the other way around), and even enforce it for example by using a `module-info.java`.

To illustrate the benefit of this approach, you can delete the `comments` package, its tests (also in the `comments` package), the migrations and perhaps the GraphQL-files, and your application should still build (except that there are no comments anymore). When you would remove the `posts` package, however, you will get a compile error because `comments` depends on it. Furthermore, if you try to hold on to this strategy, you will quite likely never get a circular dependency exception ever again.

This is, however, not always possible. While we can achieve this high level of decoupling between API controllers, GraphQL Resolvers, services, repositories and entities, it is much harder or even impossible for controllers that respond with HTML. For instance, when you have a blog post, you may also want to see the top 3 comments and the related users, and it gets awkward really quickly to solve this using the before-mentioned techniques. As such, we made an exception for HTML-based views, by stating that the non-rest Controllers live in a `web` package, which depend on other feature packages. This way, a `BlogPostController` may fetch blog posts and the top 3 related comments, without sacrificing the unidirectional dependency structure.

## No Foreign Key relationships between Aggregates

## Layering
Controllers and resolvers may call Services. Services may call Repositories. Controllers and resolvers should not depend on Repositories, though, because the business logic should be in the Services. Entities can be used by anyone, but cannot use any other objects than other Entities, and only Entities in the same package (since the packages are essentially grouped as Aggregates; as such to refer to another aggregate, you can use id references such as `postId`). Services can depend on services in other packages (as well as services in its own package), if-and-only-if no classes refer to that service. Controllers and resolvers (and of course repositories) should never use any class from other services.

### Integration Testing Strategy
Integration tests use repositories of foreign objects, but not the services, because it wants to set up an environment its own tests can run. Furthermore, this way we can get rid of problems of foreign key constraints, by solving foreign key constraints between aggregates in the service layer instead of in the database. This way, integration tests can be highly focused.

Unit tests, nevertheless, are based on services of foreign objects, since the services themselves should never use repositories directly, and the unit tests are basically "mock" tests.

# Documentation, Videos, etc.
Find out more about this project in this YouTube video of the original author.

# License
Licensed under MIT.

# To Do
- Check that references exist / use foreign keys
- REST endpoints
- Lowercase ref names
- Not every Listener use case is tested
- Add Created at and updated at to GraphQL model
- Int and Other Types
- Finish GraphQL Integration tests
- Controllers
- Decide: Maven vs GradleKT
- Unit Tests for Scenarios
- SCSS / JS / etc. / Node Modules / Deno?
- Frontend

# Credits
- The `new` template is derived from the template generated using Spring Boot Initializr.
- The `frontend` template is mostly based on NextJS templates.
