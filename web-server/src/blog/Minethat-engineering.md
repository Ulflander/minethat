
# Minethat engineering

### Code quality assurance

Here at Minethat, we believe in quality software. What does it mean for us? We don't speak here about testing, that is uber important when writing softwares, but the tools that are complements to your tests.

#### First, setup quality assurance tools at the early beginning

We took the habit to spend hours before any project just working on a packaging of the new project that includes enough code quality assurance tools.

For example, any of our Java project starts with an empty source code already validated by a bunch of tools, such as Findbugs or PMD.

Those tools help us improve our code quality by validating such potential issues:

* Code conventions not followed
* Potential bugs
* Potential security issues
* Code not covered by tests
* Duplicated code

and a lot more.

### Second, setup your integration process so quality assurance tools run at each stage

We setup all these tools to validate code at each change, even locally. Then once code is pushed we rerun all these tests and tools in a production environment, as part of our continuous integration and deployment process.

### Third, store everything in database

The golden rule of quality software. Every process should store a result in a database. This data let us have a good overview of the health of the software, monitor it recurently, and understand our common errors so we can plan new tools or new habits to avoid them the most possible.

### Our current stack of tools

Here are the stacks of tools that we currently use for our codebase (that is primarily based on Java and Javascript).

#### Java

* Unit testing: jUnit
* Logging: Log4J
* Code analyzers: FindBugs + PMD
* Coding conventions check: checkstyle
* Test coverage analyzer: cobertura

#### Javascript

* Code linter: JSLint ()
* Logging: tracer (we prefered it over winston because of its handy MongoDB appender)

#### Database

We store all error and warning reports in a Mongo database, as well as integration status (tests, builds, deployments). Both Log4J (Java) and tracer (Node) logging tools have appenders for MongoDB, so global setup is fast and simple.