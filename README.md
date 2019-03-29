# Tests

Una serie di servizi minimali, destinati a testare specifici aspetti, magari critici, della architettura Resid

### Prerequisites

What things you need to run the software:

-   Java 8+
-   [docker-compose](https://docs.docker.com/compose/)
-   [Postman collection](https://www.getpostman.com/collections/6071ab6ca5838d60f5d8): contiene tutti gli Endpoint

## Getting Started

Tutti i moduli possono essere buildati in una unica istruzione con:

```
./mvnw package
```

## Simple Events

Code can be found under [simple-events](http://gitlab.demaniodg.it/RESID/tests/tree/master/simple-events) Module.

Contiene due servizi:

-   [simple-events-source](http://gitlab.demaniodg.it/RESID/tests/tree/master/simple-events/simple-events-source): Event Producer
-   [simple-events-sink](http://gitlab.demaniodg.it/RESID/tests/tree/master/simple-events/simple-events-sink): Event Consumer

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
curl localhost:8080/publish -X POST --header 'Content-Type: application/json' -d '{"text":"Evento Singolo"}' --verbose
```

Pubblicare più eventi:

```
curl localhost:8080/publish/3 -X POST --header 'Content-Type: application/json' -d '{"text":"Evento Multiplo"}' --verbose
```

Pubblicare più eventi con sleep (ms):

```
curl localhost:8080/publish/3/sleep/5000 -X POST --header 'Content-Type: application/json' -d '{"text":"Evento Multiplo con Sleep"}' --verbose
```

Leggere messaggi ricevuti (notifce a different port: **8888**!):

```
curl http://localhost:8888/events --verbose
```

Expected result can be seen below. Remember that it takes time to publish and read domain events from Kafka. Hence a Events might be not immedietly seen:

```
[{"receivedAt":"2019-03-29T13:11:28.607Z","producedAt":"2019-03-29T13:11:28.466Z","text":"Testo dell Evento","latency":"141(ms)"}]
```
