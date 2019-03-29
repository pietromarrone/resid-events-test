# Tests

Una serie di servizi minimali, destinati a testare specifici aspetti, magari critici, della architettura Resid

### Prerequisites

What things you need to run the software:

-   Java 8+
-   [docker-compose](https://docs.docker.com/compose/)

## Getting Started

Tutti i moduli possono essere buildati con:

```
./mvnw package
```

## Simple Events

Code can be found under [simple-events](simple-events) Module.

Additional components:

-   [Apache Kafka](https://kafka.apache.org) for pub/sub for domain events
-   [Apache ZooKeeper](https://zookeeper.apache.org/) ZooKeeper is a centralized service for maintaining configuration information
-   [Spring Cloud Stream](https://cloud.spring.io/spring-cloud-stream/) to read/write messages from/to Kafka’s topic.

Build the whole infrastructure:

```
./mvnw package
```

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

Pubblicare un evento:

```
curl localhost:8080/publish -X POST --header 'Content-Type: application/json' -d '{"text":"testo del messaggio"}' --verbose
```

Leggere messaggi ricevuti (notifce a different port: **8888**!):

```
curl http://localhost:8888/events --verbose
```

Expected result can be seen below. Remember that it takes time to publish and read domain events from Kafka. Hence a Events might be not immedietly seen:

```
[{"receivedAt":"2019-03-29T10:33:01.264Z","producedAt":"2019-03-29T10:33:01.095Z","text":"testo del messaggio"}]
```
