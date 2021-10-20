# Wallets Server

The folder helps develop Wallets Server Application using Java 8, Spring Boot, H2 Database, maven build and run the application. This application Creates, Reads, Updates( Add / Withdraw  money ) wallet and does Fund Transfer between wallets by exposing the Server-Side API's.

- Each Wallet has id, name, email, amount and list of transactions.
- Each Transaction has id, name, type ( CREDIT / DEBIT ), transactionAmount, transactionReferenceId.
- The Wallet is audit tracked with createdBy, created ( LocalDateTime ), modifiedBy and modified ( LocalDateTime ).
- The Transaction is audit tracked with createdBy and created ( LocalDateTime ).
- We can create, retrieve, update, fund transfer amount from Wallets by capturing transaction information.

## Pre-requsites

1. Java 8.
2. Maven 3.5+.
3. Spring Boot 2.5.5 (latest version).
4. Spring Tool Suite IDE recommended.
5. Embedded H2 Database.
6. Docker Engine version 19.03.0+ ( Tested version: 20.10.8 ).
7. docker-compose version 1.29.2, build 5becea4c ( Tested ).

## Build and Run

### Steps to Build and Run using Maven

1. Download the project ( clone ) the project in the local machine.
2. Go to the project folder
3. Clean and install project using ``` mvn clean install ```
4. To run the project ``` mvn spring-boot:run```

### Build and Run using docker-compose

On your local machine, clone this repo and navigate to the folder backend/wallets. Then build and run the apllication following the [Docker Compose](https://docs.docker.com/compose/) documentation.

```bash
docker-compose up -d --build
```

> Docker Compose is included with [Docker Desktop](https://docs.docker.com/desktop/).
> If you don't have Docker Compose installed, [follow these installation instructions](https://docs.docker.com/compose/install/).

Once the container is built and running, visit [http://localhost:3000](http://localhost:3000)
in your web browser to view the K+N Wallets UI.

To rebuild the application after you made changes, run the `docker-compose up` command
again. This rebuilds the application, and updates the container with your changes:

```bash
docker-compose up -d --build
```

Once the container is built and running, visit [http://localhost:3000](http://localhost:3000)
in your web browser to view the K+N Wallets UI.

To stop the containers, use the `docker-compose down` command:

```bash
docker-compose down
```
## Access the API using Swagger

Click on the link to access the swagger for the application [wallets-server-swagger](http://localhost:8080/swagger-ui.html)

## Access the H2 Database

Click on the link to access the swagger for the application [wallets-server-H2-Console](http://localhost:8080/h2-console)

Click on Connect to access the DB.

if the DB connection fails with an error. Validate if the password field is kept `empty` and JDBC URL is set to
```
jdbc:h2:mem:testdb
```

## Class Diagram

![Wallets-Class-Diagram](https://github.com/naveenkulkarni029/kuehne-nagel-project/blob/master/backend/wallets/class-daigram.PNG)
