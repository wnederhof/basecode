---
layout: page
title: Getting Started
permalink: /getting-started
---
# Getting Started

## Prerequisites
Before getting started using CrudCodeGen, make sure you have Java 11 or higher, Git and Docker installed.

## From Source Code
### Installation
- Clone the `crudcodegen` repository into `~/.crudcodegen` (where `~` is your home folder) and `cd` into the folder.
- Build the project using `./mvnw clean package -DskipTests`.
- Locate the built artifact in the `target` folder. E.g. `crudcodegen-<version>.jar`
- Set up an alias for quick access, e.g.
```
alias ccg="java -jar $HOME/.crudcodegen/target/crudcodegen-<version>.jar"
```
- Verify that CrudCodeGen works, by typing: `ccg -h`, which should show the help, starting with:
```
CrudCodeGen - Open Source Full Stack Code Generator
```

### Updating
In order to update, simply pull the crudcodegen repository and build the image.
```
cd ~/.crudcodegen
git pull
mvn clean package -DskipTests
```

Locate the built artifact in the `target` folder. E.g. `crudcodegen-<version>.jar`, and update the alias defined in the installation for quick access. E.g.
```
alias ccg="java -jar $HOME/.crudcodegen/target/crudcodegen-<version>.jar"
```

## Tutorials
- Building a Blog
