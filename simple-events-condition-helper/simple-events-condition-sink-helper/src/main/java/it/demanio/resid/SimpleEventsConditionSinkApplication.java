package it.demanio.resid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resid.events.annotation.EventHandler;
import com.resid.events.configuration.EventHandlerConfiguration;

import it.demanio.resid.events.EventReceived;
import it.demanio.resid.events.EventRepository;
import it.demanio.resid.events.PayloadOther;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@SpringBootApplication
@Import(EventHandlerConfiguration.class)
public class SimpleEventsConditionSinkApplication {

	@Autowired
	EventRepository eventRepository;

	public static void main(String[] args) {
		SpringApplication.run(SimpleEventsConditionSinkApplication.class, args);
	}

	// Event Consumer
	@EventHandler(eventType = "TYPE-A")
	public void onEventA(EventReceived<String> event) {
		event.received();

		String payload = event.getPayload();
		String sender = event.getSender();

		log.info("Event TYPE-A received {} from {}, with payload {}", event, sender, payload);
		eventRepository.addA(event);
	}

	@EventHandler(eventType = "TYPE-B")
	public void onEventB(EventReceived<String> event) {
		event.received();

		log.info("Event TYPE-B received {}", event);
		eventRepository.addB(event);
	}

	@EventHandler(eventType = "TYPE-OTHER")
	public void onOther(PayloadOther event) {

		log.info("Event TYPE-OTHER received {}", event);
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
