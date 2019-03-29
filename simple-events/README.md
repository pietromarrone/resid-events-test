# All Things CQRS - With Events

A bunch of ways of doing [CQRS](https://martinfowler.com/bliki/CQRS.html) with various [Spring](https://spring.io) tools.

## Getting Started

These instructions will get you and overview of how to synchronize two different datasources. We will do so by separating command and queries in a simple CQRS app. Each module represents a different way of introducing this pattern. Also, each module is a standalone [Spring Boot](https://spring.io/projects/spring-boot) application.

### Prerequisites

What things you need to run the software:

-   Java 8+
-   [docker-compose](https://docs.docker.com/compose/)

## Overview

Sample applications are based on a simple domain that serves credit cards. There are two usecases:

-   Money can be withdrawn from a card (_Withdraw_ **command**)
-   List of withdrawals from a card can be read (**query**)

The important is that:

```
After a successful Withdraw command, a withdrawal should be seen in a result from list of withdrawals query.
```

Hence there is a need for some **synchronization** that makes state for commands and queries consistent.

Let's agree on a color code for commands, queries and synchronization. It will make our drawings consistent.

![color code](https://github.com/ddd-by-examples/all-things-cqrs/blob/master/colorcode.jpg 'Color code')

### CQRS with Domain Events as synchronization

Synchronization done by sending a domain event after succesfully handling a command.

Code can be found under [events](https://github.com/ddd-by-examples/all-things-cqrs/tree/master/with-events) module. It has 2 further modules, architecture is fully distributed. There is a [source](https://github.com/ddd-by-examples/all-things-cqrs/tree/master/with-events/with-events-source) (deals with commands) and [sink](https://github.com/ddd-by-examples/all-things-cqrs/tree/master/with-events/with-events-sink) (deals with queries).

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
