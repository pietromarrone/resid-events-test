package it.demanio.resid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import it.demanio.resid.events.EventReceived;
import it.demanio.resid.events.EventRepository;
import it.demanio.resid.events.PayloadText;
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
	@StreamListener(target = Sink.INPUT, condition = "headers['resid_type'] == 'TYPE-A'")
	public void onEventA(PayloadText event) {
		EventReceived<PayloadText> er = new EventReceived<PayloadText>();
		er.setPayload(event);
		er.setTimestamp(event.getTimestamp());

		log.info("Event TYPE-A received {}", event);
		eventRepository.addA(er);
	}

	@StreamListener(target = Sink.INPUT, condition = "headers['resid_type'] == 'TYPE-B'")
	public void onEventB(PayloadText event) {
		EventReceived<PayloadText> er = new EventReceived<PayloadText>();
		er.setPayload(event);
		er.setTimestamp(event.getTimestamp());

		log.info("Event TYPE-B received {}", event);
		eventRepository.addB(er);
	}

	@StreamListener(target = Sink.INPUT, condition = "headers['resid_type'] == 'TYPE-OTHER'")
	public void onOther(Message event) {
		log.info("Event TYPE-OTHER received with Payload {}, and Header {}", event.getPayload(), event.getHeaders());
	}

	// Rest Controller
	@GetMapping
	String hello() {
		return "Hello from Simple Events Sink Condition (Consumer)";
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
