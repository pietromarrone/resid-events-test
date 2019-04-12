package it.demanio.resid.events;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(Source.class)
public class KafkaDomainEventPublisher implements DomainEventPublisher {

	@Autowired
	private MessageChannel output;

	@Override
	public void publish(DomainEvent domainEvent) {
		publish(domainEvent.getHeader(), domainEvent);
	}

	@Override
	public void publish(EventHeader header, Object domainEvent) {
		Map<String, Object> headers = new HashMap<>();
		headers.put("resid_type", header.getEventType());
		headers.put("resid_sender", header.getSender());
		output.send(new GenericMessage<>(domainEvent, headers));
	}

}
