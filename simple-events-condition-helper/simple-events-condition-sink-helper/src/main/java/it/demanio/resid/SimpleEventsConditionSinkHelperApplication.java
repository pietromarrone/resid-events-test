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
import it.demanio.resid.events.PayloadText;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@SpringBootApplication
@Import(EventHandlerConfiguration.class)
public class SimpleEventsConditionSinkHelperApplication {

	@Autowired
	EventRepository eventRepository;

	public static void main(String[] args) {
		SpringApplication.run(SimpleEventsConditionSinkHelperApplication.class, args);
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
		return "Hello from Simple Events Sink Helper (Consumer)";
	}

	@GetMapping("events/a")
	List<Object> getAllEventsA() {
		return eventRepository.findAllEventsA();
	}

	@GetMapping("events/b")
	List<Object> getAllEventsB() {
		return eventRepository.findAllEventsB();
	}

}
