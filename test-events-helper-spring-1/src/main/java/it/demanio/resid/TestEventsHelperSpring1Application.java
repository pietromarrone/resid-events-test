package it.demanio.resid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.demanio.events.ResidEvent;
import it.demanio.events.audit.LottoProntoPerAuditEvent;
import it.demanio.resid.dto.EventDto;
import it.demanio.resid.events.EventA;
import it.demanio.resid.events.EventB;
import it.demanio.resid.events.EventRepository;
import it.demanio.resid.helper.publisher.DomainEventPublisher;
import it.demanio.resid.helper.spring.EventHelperFullConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@SpringBootApplication
@Import(EventHelperFullConfiguration.class)
public class TestEventsHelperSpring1Application {

  @Autowired
  private EventRepository eventRepository;

  @Autowired
  private DomainEventPublisher publisher;

  public static void main(String[] args) {
    SpringApplication.run(TestEventsHelperSpring1Application.class, args);
  }

  @Value("${kafka.bootstrap-servers}")
  private String bootstrapAddress;

  @Value("${kafka.groupId}")
  private String groupId;

  @KafkaListener(topics = "${kafka.topic}")
  public void consume(Message<String> message) {
    log.info("Kafka:\tConsumed message in {} with Header Type {}", message.getPayload(), message.getHeaders()
        .get("resid_type"));
  }

  // Rest Controller
  @RequestMapping
  String hello() {
    return "Hello from Test Events Helper For String Boot 1";
  }

  @RequestMapping("events/a")
  List<Object> getAllEventsA() {
    return eventRepository.findAllEventsA();
  }

  @RequestMapping("events/b")
  List<Object> getAllEventsB() {
    return eventRepository.findAllEventsB();
  }

  @RequestMapping(value = "publish", method = RequestMethod.POST)
  ResponseEntity<Void> sendOne(@RequestBody EventDto eventDto) {
    log.info("publish: {}", eventDto);

    ResidEvent e = eventFactory(eventDto);
    log.info("Sending Event {}", e);
    publisher.publish(e);

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
      return new LottoProntoPerAuditEvent();
    }
  }

}
