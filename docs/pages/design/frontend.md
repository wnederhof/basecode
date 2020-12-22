---
layout: page
title: Frontend Design
permalink: /design/frontend
parent: Project Structure and Design
nav_order: 2
---
## Code Generated for the Frontend
### Frontend technology
Traditional web applications, in contrast to SPAs, are often rendered on the
server with Javascript "sprinkled" on top of it.
The programming model of traditional web applications is often much
simpler than that of SPAs, since it does not have to deal with loading data from the
server, client-side state management, client-side routing, SSR, etc.,
since the data is immediately present while generating the html.

Single Page Applications, however, often feel more responsive than traditional
web applications. Although libraries such as Turbolinks make traditional
web applications feel as responsive as SPAs, "sprinkling" Javascript
over the traditional application only takes you so far.

Recent developments, such as Nuxt.JS and GraphQL, have made it much easier to
create a SPA than a traditional web application.
While the client still needs to ask for data to the server, Nuxt.JS already
takes care of routing and SSR, and the backend comes decoupled from
the backend due to GraphQL.

Especially since the frontend and backend are dependent on very different
features (e.g. a frontend page may show both an activity feed and a list of posts at the same time,
which may be entirely different features on the backend concepts with no dependencies between them),
it is not such a bad idea to decouple them.

Furthermore, when an application would have to support a new consumer, such
as a mobile app, no changes need to be made to the backend, whereas
this may require a brand new API and lots of refactorings for traditional web applications.

For these reasons, CrudCodeGen does not render HTML on the backend server and physically
separates the backend from the frontend. By combining the best tools available
in the industry, building a separate frontend and connecting it through an API
takes a similar amount of effort as it would take for a similar traditional web application.

As for the choice of the frontend framework, the main question was whether to choose
Angular, React and Vue.js. CrudCodeGen uses Vue.js since it is the most productive
web technology, and feels most natural when coming from building traditional web applications.
Furthermore, Vue.js felt more productive than both React and Angular and has a shallower
learning curve. The ecosystem of Vue.js is similar to React's ecosystem, and has
become the most popular frontend library of the three on Github.
When combining Vue.js with Nuxt.js, SSR, routing, code splitting, asset delivery and a lot more
is taken care of without any additional boilerplate code. As such, CrudCodeGen
uses Nuxt.js as the frontend framework of choice.
