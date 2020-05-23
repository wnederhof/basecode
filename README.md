# CrudCodeGen
CrudCodeGen is a tool for generating and managing the basic skeleton of a web application using Kotlin, Spring Boot and NextJS.

CrudCodeGen was created to automate the repetitive job of writing boilerplate code for common tasks when creating a web application.

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

# Installation
`curl ... | sh`

# Design Principles
CrudCodeGen is designed with good practies in mind. Many of these practices are common, while others are less common.

## Productive
CrudCodeGen should be the go-to tool for quickly setting up a prototype, but also your go-to tool for building large web applications.

## Simple
CrudCodeGen is designed with simplicity in mind and to accommodate 80% of the use cases for 20% of the complexity.

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
While CrudCodeGen is able to generate REST interfaces and show them using Swagger, the preferred way is using GraphQL, because it allows for much better decoupling between the frontend and the backend.

# Documentation, Videos, etc.
Find out more about this project in this YouTube video of the original author, Wouter Nederhof.

# License
Licensed under MIT.

# Credits
- The `new` template is mostly a "templified" version of the template generated using Spring Initializr.
- The `frontend` template is mostly based on NextJS templates.
