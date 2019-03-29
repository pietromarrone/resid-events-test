# Tests

Una serie di servizi minimali, destinati a testare specifici aspetti della architettura Resid

### Prerequisites

What things you need to run the software:

-   Java 8+
-   [docker-compose](https://docs.docker.com/compose/)

## Getting Started

Tutti i moduli possono essere buildati con:

```
./mvnw clean package
```

## Simple Events

Code can be found under [simple-events](simple-events) Module.

Additional components:

-   Kafka

Additional components:

-   H2 DB to keep credit cards.
-   [MongoDB](https://www.mongodb.com/what-is-mongodb) to keep withdrawals.
-   Spring Data Reactive MongoDb to reactively talk to Mongo
-   [Project Reactor](http://projectreactor.io) to serve non-blocking web-service
-   [Apache Kafka](https://kafka.apache.org) for pub/sub for domain events
-   [Spring Cloud Stream](https://cloud.spring.io/spring-cloud-stream/) to read/write messages from/to Kafka’s topic.

Running the app, remember to be in **root** of the project:

-   Run the whole infrastructure:

```
docker-compose up
```

A sample _Withdraw_ command:

```
curl localhost:8080/withdrawals -X POST --header 'Content-Type: application/json' -d '{"card":"3a3e99f0-5ad9-47fa-961d-d75fab32ef0e", "amount": 10.00}' --verbose
```

Verifed by a query (notifce a different port: **8888**!):

```
curl http://localhost:8888/withdrawals?cardId=3a3e99f0-5ad9-47fa-961d-d75fab32ef0e --verbose
```

Expected result can be seen below. Remember that it takes time to publish and read domain events from Kafka. Hence a withdrawal might be not immedietly seen:

```
[{"amount":10.00}]
```

Architecture overview:

![events](https://github.com/ddd-by-examples/all-things-cqrs/blob/master/events.jpg)

Since it is not recommended to test 2 microservices in one test, there is no E2E test that verifies commands and queries. But we can test if a message arrival in Kafka's topic results in a proper withdrawal created. The code is [here](https://github.com/ddd-by-examples/all-things-cqrs/blob/master/with-events/with-events-sink/src/test/java/io/dddbyexamples/cqrs/sink/ReadModelUpdaterTest.java):

```java
    @Test
    public void shouldSeeWithdrawalAfterGettingAnEvent() {
        //when
        anEventAboutWithdrawalCame(TEN, cardID);

        //then
        thereIsOneWithdrawalOf(TEN, cardID);
    }
```

Also it is possible to test if a successful withdrawal is followed eventually by a proper domain event publication. The code is [here](https://github.com/ddd-by-examples/all-things-cqrs/blob/master/with-events/with-events-source/src/test/java/io/dddbyexamples/cqrs/EventsPublishingTest.java).

```java
    @Test
    public void shouldEventuallySendAnEventAboutCardWithdrawal() throws IOException {
        // given
        UUID cardUUid = thereIsCreditCardWithLimit(new BigDecimal(100));
        // when
        clientWantsToWithdraw(TEN, cardUUid);
        // then
        await().atMost(FIVE_SECONDS).until(() -> eventAboutWithdrawalWasSent(TEN, cardUUid));
    }
```
