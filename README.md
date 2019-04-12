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

## Getting Started

Tutti i moduli possono essere buildati in una unica istruzione con:

```
./mvnw package
```

Nella cartella di ciascun singolo progetto:

-   [simple-events](http://gitlab.demaniodg.it/RESID/tests/tree/master/simple-events)
-   [simple-events-condition](http://gitlab.demaniodg.it/RESID/tests/tree/master/simple-events-condition)
-   [simple-events-condition-helper](http://gitlab.demaniodg.it/RESID/tests/tree/master/simple-events-condition-helper)

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

## Simple Events

Testa l'invio e la ricezione di Eventi tramite Kafka senza alcun filtro tramite il topic **events**

Code can be found under [simple-events](http://gitlab.demaniodg.it/RESID/tests/tree/master/simple-events) Module.

Contiene due servizi:

-   [simple-events-source](http://gitlab.demaniodg.it/RESID/tests/tree/master/simple-events/simple-events-source): Event Producer
-   [simple-events-sink](http://gitlab.demaniodg.it/RESID/tests/tree/master/simple-events/simple-events-sink): Event Consumer

Additional components:

-   [Apache Kafka](https://kafka.apache.org) for pub/sub for domain events
-   [Apache ZooKeeper](https://zookeeper.apache.org/) ZooKeeper is a centralized service for maintaining configuration information
-   [Spring Cloud Stream](https://cloud.spring.io/spring-cloud-stream/) to read/write messages from/to Kafka’s topic.

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

Leggere messaggi ricevuti di tipo: **_TIPE-A_** (notifce a different port: **8888**!):

```
curl http://localhost:8888/events/a --verbose
```

Leggere messaggi ricevuti di tipo: **_TIPE-B_** (notifce a different port: **8888**!):

```
curl http://localhost:8888/events/b --verbose
```

Expected result can be seen below. Remember that it takes time to publish and read domain events from Kafka. Hence a Events might be not immedietly seen:

```
[{"receivedAt":"2019-03-29T13:11:28.607Z","producedAt":"2019-03-29T13:11:28.466Z","text":"Testo dell Evento","latency":"141(ms)"}]
```

## Simple Events Condition & Simple Events Condition Helper

Testa l'invio e la ricezione di Eventi tramite Kafka senza con filtri tramite il topic **events**, la versione helper ha le stesse funzionalità ma utilizza la libreria **events-helper**

### Simple Events Condition

Code can be found under:

-   [simple-events-condition](http://gitlab.demaniodg.it/RESID/tests/tree/master/simple-events-condition) Module

Contiene due servizi:

-   [simple-events-condition-source](http://gitlab.demaniodg.it/RESID/tests/tree/master/simple-events-condition/simple-events-condition-source): Event Producer, produce eventi con Type: [TYPE-A|TYPE-B|TYPE-OTHER]
-   [simple-events-condition-sink](http://gitlab.demaniodg.it/RESID/tests/tree/master/simple-events-condition/simple-events-condition-sink): Event Consumer, consuma solo eventi di tipo [TYPE-A|TYPE-B] mentre scarta gli altri

### Simple Events Condition Helper

Code can be found under:

-   [simple-events-condition-helper](http://gitlab.demaniodg.it/RESID/tests/tree/master/simple-events-condition-helper) Module.

Contiene due servizi:

-   [simple-events-condition-source-helper](http://gitlab.demaniodg.it/RESID/tests/tree/master/simple-events-condition-helper/simple-events-condition-source-helper): Event Producer, produce eventi con Type: [TYPE-A|TYPE-B|TYPE-OTHER]
-   [simple-events-condition-sink-helper](http://gitlab.demaniodg.it/RESID/tests/tree/master/simple-events-condition-helper/simple-events-condition-sink-helper): Event Consumer, consuma solo eventi di tipo [TYPE-A|TYPE-B] mentre scarta gli altri

Additional components:

-   [Apache Kafka](https://kafka.apache.org) for pub/sub for domain events
-   [Apache ZooKeeper](https://zookeeper.apache.org/) ZooKeeper is a centralized service for maintaining configuration information
-   [Spring Cloud Stream](https://cloud.spring.io/spring-cloud-stream/) to read/write messages from/to Kafka’s topic.

Pubblicare un evento:

```
curl localhost:8080/publish -X POST --header 'Content-Type: application/json' -d '{"type":"TYPE-A","text":"Evento Singolo"}' --verbose
```

```
curl localhost:8080/publish -X POST --header 'Content-Type: application/json' -d '{"type":"TYPE-B","text":"Evento Singolo"}' --verbose
```

```
curl localhost:8080/publish -X POST --header 'Content-Type: application/json' -d '{"type":"TYPE-OTHER","text":"Evento Singolo"}' --verbose
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
