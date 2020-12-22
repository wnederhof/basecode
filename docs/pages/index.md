---
layout: page
title: Introduction
permalink: /
nav_order: 1
---
# Introduction
Imagine being able to set up the skeleton for your next Kotlin, Spring Boot and Vue-based web project in minutes. Whether you want to try out a new idea, or you want to have a solid fundament for your enterprise-grade application. No more boilerplating days, discussions about irrelevant technical details or questionable shortcuts to get up and running as soon as possible. This is exactly what CrudCodeGen tries to achieve.

Using CrudCodeGen, setting up the skeleton of your next pet store is as simple as:
```
ccg new com.petty petstore
cd petstore
ccg generate scaffold Category name:string
ccg generate scaffold Product categoryId:Category name:string description:text_o
```
