package com.demanio.resid;

import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demanio.resid.dto.EventDto;
import com.demanio.resid.events.EventA;
import com.demanio.resid.events.EventB;
import com.demanio.resid.events.EventWithoutHeader;

import it.demanio.events.Header;
import it.demanio.events.HeaderBuilder;
import it.demanio.events.ResidEvent;
import it.demanio.resid.helper.publisher.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@SpringBootApplication
//@Import(EventProducerConfiguration.class)
public class SimpleEventsConditionSourceHelperApplication {

  @Autowired
  DomainEventPublisher publisher;

  public static void main(String[] args) {
    SpringApplication.run(SimpleEventsConditionSourceHelperApplication.class, args);
  }

  // Rest Controller
  @RequestMapping
  String hello() {
    return "Hello from Simple Events Source (Producer) for Spring 1";
  }

  @RequestMapping(path = "publish", method = RequestMethod.POST)
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

  @RequestMapping(path = "publish/{num}", method = RequestMethod.POST)
  ResponseEntity<Void> sendMany(@PathVariable long num, @RequestBody EventDto eventDto) {
    log.info("publish/{}: {}", num, eventDto);

    new Thread(() -> {
      LongStream.range(0, num)
          .forEach(i -> {
            ResidEvent e = eventFactory(eventDto);
            if (e != null) {
              log.info("Sending Event {}", e);
//              publisher.publish(e);
            } else {
              sendWithHeader(eventDto);
            }
          });
    }).start();

    return ResponseEntity.ok()
        .build();
  }

  @RequestMapping(path = "publish/{num}/sleep/{sleep}", method = RequestMethod.POST)
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
//              publisher.publish(e);
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

    Header header = HeaderBuilder.builder() //
        .eventType("TYPE-OTHER") //
        .sender("SimpleConsumerConditionSource") //
        .build();

    log.info("Sending Event {} with Header {}", other, header);
//    publisher.publish(header, other);
  }

}
