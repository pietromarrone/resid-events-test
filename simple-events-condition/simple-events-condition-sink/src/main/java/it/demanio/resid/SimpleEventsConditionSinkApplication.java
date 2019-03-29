package it.demanio.resid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import it.demanio.resid.events.EventReceived;
import it.demanio.resid.events.EventRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@SpringBootApplication
@EnableBinding(Sink.class)
public class SimpleEventsConditionSinkApplication {

	@Autowired
	EventRepository eventRepository;

	public static void main(String[] args) {
		SpringApplication.run(SimpleEventsConditionSinkApplication.class, args);
	}

	// Event Consumer
	@StreamListener(target = Sink.INPUT, condition = "headers['type'] == 'TYPE-A'")
	public void onEventA(EventReceived event) {
		event.received();

		log.info("Event TYPE-A received {}", event);
		eventRepository.addA(event);
	}

	@StreamListener(target = Sink.INPUT, condition = "headers['type'] == 'TYPE-B'")
	public void onEventB(EventReceived event) {
		event.received();

		log.info("Event TYPE-B received {}", event);
		eventRepository.addB(event);
	}

	// Rest Controller
	@GetMapping
	String hello() {
		return "Hello from Simple Events Sink (Consumer)";
	}

	@GetMapping("events/a")
	List<EventReceived> getAllEventsA() {
		return eventRepository.findAllEventsA();
	}

	@GetMapping("events/b")
	List<EventReceived> getAllEventsB() {
		return eventRepository.findAllEventsB();
	}

}
