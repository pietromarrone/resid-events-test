# Tests

Una serie di servizi minimali, destinati a testare specifici aspetti, magari critici, della architettura Resid

### Prerequisites

What things you need to run the software:

-   Java 8+
-   [docker-compose](https://docs.docker.com/compose/)
-   [Postman collection](https://www.getpostman.com/collections/6071ab6ca5838d60f5d8): contiene tutti gli Endpoint

Nella folder docker-compose trovi alcuni Docker file per task comuni, per lanciarli occorre:

```
    docker-compose -f docker-compose/[NOME-FILE.yml] up -d
```

-   [kafka-exposed](http://gitlab.demaniodg.it/RESID/tests/tree/master/docker-compose/kafka-exposed.yml) lancia Kafka e lo espone al Host sulla porta standard.

Additional components:

-   [Apache Kafka](https://kafka.apache.org) for pub/sub for domain events
-   [Apache ZooKeeper](https://zookeeper.apache.org/) ZooKeeper is a centralized service for maintaining configuration information
-   [Spring Cloud Stream](https://cloud.spring.io/spring-cloud-stream/) to read/write messages from/to Kafka’s topic.

## Getting Started

Tutti i moduli possono essere buildati in una unica istruzione con:

```
./mvnw package
```

Nella cartella di ciascun singolo progetto:

-   [README simple-events](http://gitlab.demaniodg.it/RESID/tests/tree/master/simple-events)
-   [README simple-events-condition](http://gitlab.demaniodg.it/RESID/tests/tree/master/simple-events-condition)
-   [README simple-events-condition-helper](http://gitlab.demaniodg.it/RESID/tests/tree/master/simple-events-condition-helper)

## Plain Spring boot

Tutte le app possono girare come eseguibili spring boot con la seguente istruzione

```
./mvnw spring-boot:run
```

## Docker

Build Docker images:

```
docker-compose build
```

Run the whole infrastructure:

```
docker-compose up
```

oppure in unica istruzione

```
docker-compose up --build
```

ancora per i più pigri

```
./mvnw package && docker-compose up --build
```
