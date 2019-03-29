package it.demanio.resid.sink;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

import it.demanio.resid.events.EventReceived;
import it.demanio.resid.events.EventRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EventConsumer {

	@Autowired
	EventRepository eventRepository;

	@StreamListener(target = Sink.INPUT)
	public void handle(Event event) {
		log.info("Event received {}", event);
		eventRepository.add(new EventReceived(Instant.now(), event.getProducedAt(), event.getText()));
	}
}
