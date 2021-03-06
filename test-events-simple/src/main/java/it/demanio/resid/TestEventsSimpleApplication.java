package it.demanio.resid;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.demanio.resid.dto.EventDto;
import it.demanio.resid.events.DomainEventPublisher;
import it.demanio.resid.events.Event;
import it.demanio.resid.events.EventReceived;
import it.demanio.resid.events.EventRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@SpringBootApplication
@EnableBinding(Processor.class)
public class TestEventsSimpleApplication {

  @Autowired
  DomainEventPublisher publisher;

  @Autowired
  EventRepository eventRepository;

  public static void main(String[] args) {
    SpringApplication.run(TestEventsSimpleApplication.class, args);
  }

  // Rest Controller
  @GetMapping
  String hello() {
    return "Hello from Test Events Simple";
  }

  @PostMapping("publish")
  ResponseEntity<Void> sendOne(@RequestBody EventDto eventDto) {
    log.info("publish: {}", eventDto);

    Event e = new Event(UUID.randomUUID(), eventDto.getText());
    log.info("Sending Event {}", e);

    publisher.publish(e);
    return ResponseEntity.ok()
        .build();
  }

  @PostMapping("publish/{num}")
  ResponseEntity<Void> sendMany(@PathVariable long num, @RequestBody EventDto eventDto) {
    log.info("publish/{}: {}", num, eventDto);

    new Thread(() -> {
      LongStream.range(0, num)
          .forEach(i -> {
            Event e = new Event(UUID.randomUUID(), i + "-" + eventDto.getText());
            log.info("Sending {} Event {}", i, e);
            publisher.publish(e);
          });
    }).start();

    return ResponseEntity.ok()
        .build();
  }

  @PostMapping("publish/{num}/sleep/{sleep}")
  ResponseEntity<Void> sendManyWithSleep(@PathVariable long num, @PathVariable long sleep, @RequestBody EventDto eventDto) throws InterruptedException {
    log.info("publish/{}/sleep/{}: {}", num, sleep, eventDto);

    new Thread(() -> {
      LongStream.range(0, num)
          .forEach(i -> {
            try {
              TimeUnit.MILLISECONDS.sleep(sleep);
            } catch (InterruptedException e) {
            }
            Event e = new Event(UUID.randomUUID(), i + "-" + eventDto.getText());
            log.info("Sending {} Event {} with sleep {}", i, e, sleep);
            publisher.publish(e);
          });
    }).start();

    return ResponseEntity.ok()
        .build();
  }

  // Event Consumer
  @StreamListener(target = Sink.INPUT)
  public void handle(EventReceived event) {
    event.received();

    log.info("Event received {}", event);
    eventRepository.add(event);
  }

  @GetMapping("events")
  List<EventReceived> getAllEvents() {
    return eventRepository.findAllEvents();
  }

}
