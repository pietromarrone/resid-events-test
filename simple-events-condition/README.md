# Simple Events Condition

Testa l'invio e la ricezione di Eventi tramite Kafka senza con filtri tramite il topic **events**

Code can be found under:

-   [simple-events-condition](http://gitlab.demaniodg.it/RESID/tests/tree/master/simple-events-condition) Module

Contiene due servizi:

-   [simple-events-condition-source](http://gitlab.demaniodg.it/RESID/tests/tree/master/simple-events-condition/simple-events-condition-source): Event Producer, produce eventi con Type: [TYPE-A|TYPE-B|TYPE-OTHER]
-   [simple-events-condition-sink](http://gitlab.demaniodg.it/RESID/tests/tree/master/simple-events-condition/simple-events-condition-sink): Event Consumer, consuma solo eventi di tipo [TYPE-A|TYPE-B] mentre scarta gli altri

### Pubblicare un evento:

```
curl localhost:8080/publish -X POST --header 'Content-Type: application/json' -d '{"type":"TYPE-A","text":"Evento Singolo"}' --verbose
```

```
curl localhost:8080/publish -X POST --header 'Content-Type: application/json' -d '{"type":"TYPE-B","text":"Evento Singolo"}' --verbose
```

```
curl localhost:8080/publish -X POST --header 'Content-Type: application/json' -d '{"type":"TYPE-OTHER","text":"Evento Singolo"}' --verbose
```

### Pubblicare più eventi:

```
curl localhost:8080/publish/3 -X POST --header 'Content-Type: application/json' -d '{"type":"TYPE-A","text":"Evento Multiplo"}' --verbose
```

### Pubblicare più eventi con sleep (ms):

```
curl localhost:8080/publish/3/sleep/5000 -X POST --header 'Content-Type: application/json' -d '{"type":"TYPE-A","text":"Evento Multiplo con Sleep"}' --verbose
```

### Leggere messaggi ricevuti (notify a different port: **8888**!):

```
curl http://localhost:8888/events/a --verbose
```

```
curl http://localhost:8888/events/b --verbose
```

Expected result can be seen below. Remember that it takes time to publish and read domain events from Kafka. Hence a Events might be not immedietly seen:

```
[{"receivedAt":"2019-03-29T13:11:28.607Z","producedAt":"2019-03-29T13:11:28.466Z","text":"Testo dell Evento","latency":"141(ms)"}]
```
