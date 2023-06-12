# Spring React Demo (SRD)

This Demo project was designed for the 'Hogeschool Arnhem en Nijmegen' (HAN) school in The Netherlands, Arnhem to teach
students about the inner workings of some of the most-used tech in Web App development. The intent is therefore to 
provide examples and hooks to study material for teachers to use in class and thus is not an appropriate reflection of
best practices in Web App development per se. For example, this project uses Spring Core (e.g. MVC) for educational 
purposes instead of the more popular Spring Boot framework to bootstrap the Web Server and its services.

The Web App includes all necessary build tools and dependencies in a single application to ease deployment, testing and
debugging. For example, CORS is not an issue, since the entire front-end is bundled together in the same archive as a
(set of) static resource(s).

## Apache 2.0 License
[![License](https://img.shields.io/badge/License-Apache_2.0-yellowgreen.svg)](https://opensource.org/licenses/Apache-2.0)

## Features

The featured elements of this Demo project include at least:
* Spring Core framework
  * Spring MVC
    * REST Controllers, HttpServlet request/response interaction 
    * Centralised exception handling for REST controllers
  * Spring Data
    * Pre-configured implementation for JPA repositories, functional query system
  * Spring Security
    * Configurable Security filter chain and context to fine-tune authentication and authorisation 
  * Spring Sessions
    * Database-managed sessions
* Hibernate / JPA
  * Entity-managed domain-binding of business logic and database
  * Custom username bean validation (JSR 380)
* Logback
  * Logging of Request, Security and Application relevant errors and warnings (console + file)
* Maven
* Node / npm
* React

## About this repository

This repository is provided as a largely independent Spring Web App, but assumes at least a database connection. An
example `application.properties` and `database.properties` file is included in the project that uses PostgreSQL. To be
able to use Spring Session, the required tables need to be created, using the following SQL:

```sql
CREATE TABLE IF NOT EXISTS SPRING_SESSION (
  PRIMARY_ID CHAR(36) NOT NULL,
  SESSION_ID CHAR(36) NOT NULL,
  CREATION_TIME BIGINT NOT NULL,
  LAST_ACCESS_TIME BIGINT NOT NULL,
  MAX_INACTIVE_INTERVAL INT NOT NULL,
  EXPIRY_TIME BIGINT NOT NULL,
  PRINCIPAL_NAME VARCHAR(100),
  CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
);

CREATE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);
```

The required `user_account` table should be clear from the `UserEntity` class.

In addition, to succesfully build
the project, the following local dependencies are required:
* Java 12
* TomEE Webprofile 8/9
* Maven

Of course, your favourite IDE may provide these for you while developing. Methods for deployment on a remote server are
not described, as it depends highly on additional startup parameters. Still, this project is intended to run on any 
number of different architectures and contexts. 

### Expectation management
This webapp is not intended nor configured for remote deployment nor for a multi-environment setup (e.g. using Spring
Profiles). It is intended to be demo'ed on localhost as a feature presentation of a lot of the elements of a Spring 
Webapp. A multi-environment deployment version would require at least a specific `application.properties` file for each 
environment and a deployment configuration (e.g. docker-compose, Jenkins).

To ease adoption and expansion of the sourcecode, the entire Webapp is fully Java-configured and does not require any
external configuration files, instead of traditional configuration and requirements (e.g. web.xml, beans.xml, 
persistence.xml). This should allow for better debugging capabilities. The project consists of a statically served 
front-end in React to ease demoing. Some downsides are limited interplay between React components (e.g. routing)
 and Spring's MVC-based endpoint configuration. These features can often be implemented, but generally not in a `Hello 
World` prototype scenario.

####_Not included_
- Hot reloading of the front-end, due to Webpack's inclusion in the same project
- Unit/integration testing; JUnit is recommended
- Jakarta EE, Java 17+ nor Spring 6 (updated Spring Security configuration)
- i18n

New versions of this project are expected to include some or all of the above.

### Security

Security is, as intended, to be managed fully server-side and fully under the control of Spring Security. Concretely, 
this means the login endpoint is auto-generated using form-login, sessions are managed automatically (JDBC-driven) and 
custom filters are implemented once-per-request as part of the `SecurityFilterChain`, which in turn are to be explicitly 
declared as servlet (i.e. Tomcat) filters (see `SpringMvcApplication`).

The demo'able login process is as follows. Create a new user using the registration form. After that, use the login form
to make a POST call to the auto-generated server-side login endpoint with valid credentials. This will result in a 
front-end 'redirect' (i.e. React conditionals) to a permission-protected serverside endpoint `/private`. In this demo, 
permissions are provided by default instead of using a role-permission table, resulting in a successful render of the 
HTML that is represented for `/private` using React. It is advised to implement an actual rigorous permission system 
going forward.

The current implementation also provide a stepping stone for other authentication implementation such as OAuth 2.0,
requiring minimal changes to the `WebSecurityConfig` file.

### Build process
The basic process of building the Web App is to delegate the build process to Maven. For command line usage this is 
straightforward through use of its binaries. For others, such as the IntelliJ IDE, it requires you to denote this 
delegation manually (`Settings > Build, Execution, Deployment > Build Tools > Maven > Runner > Delegate IDE build/run actions to Maven`).
The Maven build process includes the [Maven Frontend Plugin](https://github.com/eirslett/frontend-maven-plugin) to build
the static `HTML`, `CSS` and `JavaScript` files using automatically installed Node, npm and Webpack services and 
dependencies and bundling all static files into a single `bundle.js` file. Therefore, HTML files should reference this 
bundle instead of individual JS files. To provide a smooth build process, the internal configuration for these build 
tools through the `package.json` and `webpack.config.js` files assumes a certain package structure and so it is 
important to keep track of any changes made between them.

The Java files are published as a `WAR` archive at `/target/` and the static interface is published at
`/resources/static/built`. The `React`-based output of the front-end uses internal routing to find Java endpoints and
dynamically update the requested HTML. If you want to build individual JS files for each HTML file, refer to Webpack's
capabilities of [Code Splitting](https://webpack.js.org/guides/code-splitting/).

### Contribute
If you want to help and _improve_ this project, please contact me directly as the current project is:
* A bootstrapped demo
* Intended for educational purposes

It is not:
* An enterprise app
* A commercial product

Please feel free to take this repository at face value, fork it and expand upon it under your own expertise. Any
development on this root will be discussed and implemented both through `.git` version control as well as physical
meetings at the HAN. Not all changes will therefore reflect a typical software development process.

Please try to keep the project Java-only as external configuration will interfere / override the use of any Java-based 
configuration that is added later on.

## Quick Start

Either:
* Build on commandline using `mvn install` followed by a `startup` command from Tomcat's install directory
* Build and start on commandline using `mvn install tomee:run` by including the [tomee-maven-plugin](https://mvnrepository.com/artifact/org.apache.tomee.maven/tomee-maven-plugin)
in your pom.xml
* Build and start using your favourite IDE by creating a `Run Configuration` that builds a WAR archive in the `/target/`
folder using a specific TomEE instance


## Documentation

The codebase provides Javadoc documentation for certain dependencies used and functions that require additional context.
Again, this is done for educational purposes and does not reflect commercial best-practices.
