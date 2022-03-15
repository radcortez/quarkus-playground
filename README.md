# Quarkus Playground

The purpose of this Github repository is to showcase typical use cases of [Quarkus](https://quarkus.io) in applications
development.

## Pre Requisites

* [OpenJDK 11](https://adoptopenjdk.net/?variant=openjdk11&jvmVariant=hotspot)
* [GraalVM 21 - JDK 11](https://github.com/graalvm/graalvm-ce-builds/releases) (for native image)
* [Maven 3.6.3](https://maven.apache.org/download.cgi)
* [Docker](https://hub.docker.com/search/?type=edition&offering=community)

## Project Structure

The project is a backend for a book store. It provides a REST API to Create, Read, Update and Delete books, and a REST
API to retrieve the ISBN of the book.

* number-api - A service to generate configurable Numbers.
* book-api - A service to manage books.
* standalone - A standalone client to call the Book API.
* simulator - A client simulator that generates random requests to the Book API and simulate traffic.

## SmallRye APIs

The following SmallRye APIs can be found through the project:

* Config (to generate the prefix of the Number API generation)
* OpenAPI (to document the REST API of both Number API and Book API)
* JWT (to authenticate and authorize calls that manage books)
* Fault Tolerance (to handle ISBN book generation if Number API cannot be called)
* Open Tracing (to trace calls between Book API and Number API)
* Reactive Messaging (to store Books that require ISBN book generation due to failure)
* Metrics (to record call statistics and count how many books require ISBN)
* Health (to monitor health of the Number API)
* REST Client (to call Book API with a standalone client)

## Libraries and Infra

The project uses [Quarkus](https://quarkus.io) as the Java stack, and the built in [SmallRye](https://smallrye.io)
components.

The required infrastructure provided by either Docker or Kubernetes includes:

* Postgres Database
* Kafka
* Jaeger
* Prometheus

## Build

Set up a local Docker Registry first to store the generated Docker images:

```bash
docker run -d -p 5000:5000 --restart=always --name docker-registry registry:2
```

Use Maven to build the project with the following command from the project root:

```bash
mvn verify -Dquarkus.container-image.build=true
```

## Run

### With Docker

The easiest way to run the entire system is to use `docker-compose`. This will run the apps, plus all the required
infrastructure. Run the following command from the project root:

```bash
docker-compose up
```

Use the following command to stop and remove all the containers:

```bash
docker-compose down
```

### With Java

The infrastructure is still required to run the applications properly. They can also be set up manually in the running
host, or use `docker-compose` to start only the required infrastructure:

```bash
docker-compose up database zookeeper kafka prometheus jaeger
```

To execute `number-api` and `book-api` directly, run the following command from each module root:

```bash
java -jar target/number-api-runner.jar

java -jar target/book-api-runner.jar
```

### With Kubernetes

The infrastructure to run in Kubernetes is available in the `.kubernetes` folder. To start the infrastructure run:

```bash
kubectl apply -f .kubernetes
```

Quarkus is able to generate the Kubernetes deployment files and deploy the application directly. This requires a Maven
build to generate the deployment descriptors:

 ```bash
mvn verify -Dquarkus.container-image.build=true -Dquarkus.kubernetes.deploy=true
```

## Test the Applications

Use Swagger-UI to access the applications REST endpoint and invoke the APIs:

* [Book API](http://localhost:8080/swagger-ui/#/)
* [Number API](http://localhost:8090/swagger-ui/#/)

For Authentication and Authorization in `book-api` you need to call `Authorize` in the Swagger interface to generate a
`JWT`. Any `client_id`, and `client_secret` is acceptable.

To check Metris and Tracing information:

* [Prometheus](http://localhost:9090)
* [Jaeger](http://localhost:16686)

## Native Images

Install GraalVM Native Image binary with:

```bash
gu install native-image
```

Set up an environment variable `GRAALVM_HOME` pointing to the GraalVM installation folder.

```bash
mvn package -Pnative
```

This is going to generate a binary executable for each module. To execute `number-api` and `book-api` as native, run
the following command from each module root:

```bash
./target/number-api-runner

./target/book-apo-runner
```

### Build Native Docker Images

To build Docker Images with the native binaries run the following command:

```bash
mvn verify -Pnative -Dquarkus.native.container-build=true -Dquarkus.container-image.build=true
```

Or to deploy it directly to Kubernetes:

```bash
mvn verify -Pnative -Dquarkus.native.container-build=true -Dquarkus.container-image.build=true -Dquarkus.kubernetes.deploy=true
```

## Resources

* [Quarkus](https://quarkus.io)
* [SmallRye](https://smallrye.io)
