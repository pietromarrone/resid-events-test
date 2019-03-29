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
public class SimpleEventsSinkApplication {

	@Autowired
	EventRepository eventRepository;

	public static void main(String[] args) {
		SpringApplication.run(SimpleEventsSinkApplication.class, args);
	}

	// Event Consumer
	@StreamListener(target = Sink.INPUT)
	public void handle(EventReceived event) {
		event.received();

		log.info("Event received {}", event);
		eventRepository.add(event);
	}

	// Rest Controller
	@GetMapping
	String hello() {
		return "Hello from Simple Events Sink (Consumer)";
	}

	@GetMapping("events")
	List<EventReceived> getAllEvents() {
		return eventRepository.findAllEvents();
	}

}
