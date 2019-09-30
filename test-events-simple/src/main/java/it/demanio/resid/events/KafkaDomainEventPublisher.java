package it.demanio.resid.events;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Component
public class KafkaDomainEventPublisher implements DomainEventPublisher {

	@Autowired
	private Source source;

	@Override
	public void publish(DomainEvent domainEvent) {
		Map<String, Object> headers = new HashMap<>();
		headers.put("type", domainEvent.getType());
		source.output().send(new GenericMessage<>(domainEvent, headers));
	}
}
