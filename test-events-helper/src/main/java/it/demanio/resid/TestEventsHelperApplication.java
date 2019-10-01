package it.demanio.resid;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.demanio.events.Header;
import it.demanio.events.ResidEvent;
import it.demanio.resid.dto.EventDto;
import it.demanio.resid.events.EventA;
import it.demanio.resid.events.EventB;
import it.demanio.resid.events.EventReceived;
import it.demanio.resid.events.EventRepository;
import it.demanio.resid.events.EventWithoutHeader;
import it.demanio.resid.events.PayloadOther;
import it.demanio.resid.events.PayloadText;
import it.demanio.resid.helper.publisher.DomainEventPublisher;
import it.demanio.resid.helper.spring.annotation.EventHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@SpringBootApplication
public class TestEventsHelperApplication {

  @Autowired
  EventRepository eventRepository;

  @Autowired
  DomainEventPublisher publisher;

  public static void main(String[] args) {
    SpringApplication.run(TestEventsHelperApplication.class, args);
  }

  // Event Consumer
  @EventHandler(eventType = "TYPE-A")
  public void onEventA(PayloadText event) {
    EventReceived<PayloadText> er = new EventReceived<PayloadText>();
    er.setPayload(event);
    er.setTimestamp(event.getTimestamp());

    log.info("Event TYPE-A received {}", event);
    eventRepository.addA(er);
  }

  @EventHandler(eventType = "TYPE-B")
  public void onEventB(PayloadText event) {
    EventReceived<PayloadText> er = new EventReceived<PayloadText>();
    er.setPayload(event);
    er.setTimestamp(event.getTimestamp());

    log.info("Event TYPE-B received {}", event);
    eventRepository.addB(er);
  }

  @EventHandler(eventType = "TYPE-OTHER")
  public void onOther(PayloadOther event) {
    log.info("Event TYPE-OTHER received {}", event);
  }

//	@EventHandler(eventType = "TYPE-OTHER")
//	public void onOther(Message<PayloadOther> event) {
//		log.info("Event TYPE-OTHER received with Payload {}, and Header {}", event.getPayload(), event.getHeaders());
//	}

  // Rest Controller
  @GetMapping
  String hello() {
    return "Hello from Test Events Helper";
  }

  @GetMapping("events/a")
  List<Object> getAllEventsA() {
    return eventRepository.findAllEventsA();
  }

  @GetMapping("events/b")
  List<Object> getAllEventsB() {
    return eventRepository.findAllEventsB();
  }

  @PostMapping("publish")
  ResponseEntity<Void> sendOne(@RequestBody EventDto eventDto) {
    log.info("publish: {}", eventDto);

    ResidEvent e = eventFactory(eventDto);
    if (e != null) {
      log.info("Sending Event {}", e);
      publisher.publish(e);
    } else {
      sendWithHeader(eventDto);
    }

    return ResponseEntity.ok()
        .build();
  }

  @PostMapping("publish/{num}")
  ResponseEntity<Void> sendMany(@PathVariable long num, @RequestBody EventDto eventDto) {
    log.info("publish/{}: {}", num, eventDto);

    new Thread(() -> {
      LongStream.range(0, num)
          .forEach(i -> {
            ResidEvent e = eventFactory(eventDto);
            if (e != null) {
              log.info("Sending Event {}", e);
              publisher.publish(e);
            } else {
              sendWithHeader(eventDto);
            }
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
            ResidEvent e = eventFactory(eventDto);
            if (e != null) {
              log.info("Sending Event {}", e);
              publisher.publish(e);
            } else {
              sendWithHeader(eventDto);
            }
          });
    }).start();

    return ResponseEntity.ok()
        .build();
  }

  private ResidEvent eventFactory(EventDto eventDto) {
    switch (eventDto.getType()) {
    case "TYPE-A":
      return new EventA(eventDto.getText());
    case "TYPE-B":
      return new EventB(eventDto.getText());
    default:
      return null;
    }
  }

  private void sendWithHeader(EventDto eventDto) {
    EventWithoutHeader other = new EventWithoutHeader();
    other.setNome("Nome " + eventDto.getText());
    other.setCognome("Cognome " + eventDto.getText());
    other.setCodiceFiscale("Codice Fiscale " + eventDto.getText());

    Header header = new Header("TYPE-OTHER", "SimpleConsumerConditionSource");

    log.info("Sending Event {} with Header {}", other, header);
    publisher.publish(header, other);
  }

}
