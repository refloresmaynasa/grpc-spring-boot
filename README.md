# GRPC server and client (Java Spring Boot)
[![Java CI with Maven](https://github.com/refloresmaynasa/grpc-spring-boot/actions/workflows/maven.yml/badge.svg)](https://github.com/refloresmaynasa/grpc-spring-boot/actions/workflows/maven.yml)

The following was discovered as part of building this project:

* The JVM level was changed from '11' to '17', review the [JDK Version Range](https://github.com/spring-projects/spring-framework/wiki/Spring-Framework-Versions#jdk-version-range) on the wiki for more details.

## Table of contents

- Pre-requisites
- Getting started
    - Docker compose (MongoDB)
    - Build and run the project
    - Endpoints to Navigate

## Pre-requisites

- Java 11
- Maven
- Docker Compose (MongoDB)

## Getting started

- Clone the repository
``` powershell
git clone https://github.com/refloresmaynasa/grpc-spring-boot.git
```
- Check Java and Maven
``` powershell
java -version
mvn -version
```
### Docker compose (MongoDB)

The project contains a `docker-compose.yml` file to up a **MongoDB** instance.
``` powershell
docker-compose up -d
```

### Build and run the project
* Compile the module `Proto`:
``` powershell
cd proto
mvn compile
```

* Return to root folder and build Project:
``` powershell
mvn clean package
mvn clean install
```

* Run the Server module:
``` powershell
cd grpc-service
mvn spring-boot:run
```

* Run the Client module:
``` powershell
cd grpc-client
mvn spring-boot:run
```

### Endpoints to Navigate

* GRPC Server (You can test with Postman)
  - localhost:9000

* Client API Rest
  - http://localhost:8080/product-lines/2
  - http://localhost:8080/products
  - http://localhost:8080/products/expensive
  - http://localhost:8080/products/years/2019,2020

---
- :memo: Author: [Ricardo Flores: ***refloresmaynasa***](https://github.com/refloresmaynasa)
---
### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.6/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.6/maven-plugin/reference/html/#build-image)
