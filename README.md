# Scalar 2024 - ZIO Full-Stack Workshop

This repository holds the code we wrote in the Scalar 2024 workshop on full-stack Scala 3 with the ZIO stack.

## How to Run and Develop

For the starter code:

```
git checkout start
```

You need the following installed:

-   IntelliJ IDEA or Metals
-   Docker and Docker Compose
-   NPM

### Backend

```
sbt
project server
~compile
```

To run tests, in an SBT command line, run `project server`, then either

```
test
```

or

```
testOnly (full class name)
```

To run the backend, in an SBT command line, run `project server`, then

```
run
```

or

```
runMain (full class name)
```

### Frontend

First, you need to install NPM packages in order to serve the frontend HTML/JS/CSS/asset files. Run

```
cd modules/app
npm install
```

To run the frontend, in one terminal:

```
sbt
project app
~fastOptJS
```

in another terminal, in the `modules/app` directory:

```
npm run start
```

Then http://localhost:1234 to see the page.
