# Kuehne-Nagel-Project: Wallets Application

The Repository helps build and run Wallets Application SERVER and CLIENT. The Wallets application is developed using Java 8, Spring Boot, H2 Database on the Server-Side, ReactJS and [react-table](https://github.com/tannerlinsley/react-table) ( tabular format ) on the Client-Side. The application Creates, Reads, Updates( Add / Withdraw  money ) wallet and does Fund Transfer between wallets by consuming the Server-Side API's. In the Client-Side, the Server-Side API's are consumed using Axios, display and modify data with Router & Bootstrap.

- Each Wallet has id, name, email, amount and list of transactions.
- Each Transaction has id, name, type ( CREDIT / DEBIT ), transactionAmount, transactionReferenceId.
- The Wallet is audit tracked with createdBy, created ( LocalDateTime ), modifiedBy and modified ( LocalDateTime ).
- The Transaction is audit tracked with createdBy and created ( LocalDateTime ).
- We can create, retrieve, update, fund transfer amount from Wallets by capturing transaction information.
- List of Wallets and Transactions foreach wallet are shown in a Tabular format.
- There is a Search bar for finding Wallets by title.

## Pre-requsites

1. Docker Engine version 19.03.0+ ( Tested version: 20.10.8 )
2. docker-compose version 1.29.2, build 5becea4c ( Tested ).

## Build and Run

On your local machine, clone this repo. Then build and run the apllication following the [Docker Compose](https://docs.docker.com/compose/) documentation.

```bash
docker-compose up -d --build
```

> Docker Compose is included with [Docker Desktop](https://docs.docker.com/desktop/).
> If you don't have Docker Compose installed, [follow these installation instructions](https://docs.docker.com/compose/install/).

Once the container is built and running, visit [http://localhost:3000](http://localhost:3000)
in your web browser to view the K+N Wallets UI.

To rebuild the docs after you made changes, run the `docker-compose up` command
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
