package it.demanio.resid.events;

import java.time.Instant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class EventA implements DomainEvent {

	private EventHeader header = EventHeaderBuilder.headerBuilder() //
			.eventType("TYPE-A") //
			.sender("SimpleConsumerConditionSource") //
			.build();

	private final Instant timestamp = Instant.now();
	private final String text;
}
