# Simple Events

Testa l'invio e la ricezione di Eventi tramite Kafka senza alcun filtro tramite il topic **events**

Code can be found under [simple-events](http://gitlab.demaniodg.it/RESID/tests/tree/master/simple-events) Module.

Contiene due servizi:

-   [simple-events-source](http://gitlab.demaniodg.it/RESID/tests/tree/master/simple-events/simple-events-source): Event Producer
-   [simple-events-sink](http://gitlab.demaniodg.it/RESID/tests/tree/master/simple-events/simple-events-sink): Event Consumer

### Pubblicare un evento:

```
curl localhost:8080/publish -X POST --header 'Content-Type: application/json' -d '{"text":"Evento Singolo"}' --verbose
```

### Pubblicare più eventi:

```
curl localhost:8080/publish/3 -X POST --header 'Content-Type: application/json' -d '{"text":"Evento Multiplo"}' --verbose
```

### Pubblicare più eventi con sleep (ms):

```
curl localhost:8080/publish/3/sleep/5000 -X POST --header 'Content-Type: application/json' -d '{"text":"Evento Multiplo con Sleep"}' --verbose
```

Leggere messaggi ricevuti: (notify a different port: **8888**!):

```
curl http://localhost:8888/events --verbose
```

Expected result can be seen below. Remember that it takes time to publish and read domain events from Kafka. Hence a Events might be not immedietly seen:

```
[{"receivedAt":"2019-03-29T13:11:28.607Z","producedAt":"2019-03-29T13:11:28.466Z","text":"Testo dell Evento","latency":"141(ms)"}]
```
