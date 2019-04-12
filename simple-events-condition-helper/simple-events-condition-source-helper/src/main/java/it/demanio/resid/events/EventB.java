package it.demanio.resid.events;

import java.time.Instant;

import com.resid.events.Event;
import com.resid.events.EventHeader;
import com.resid.events.EventHeaderBuilder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class EventB extends Event {

	private EventHeader header = EventHeaderBuilder.headerBuilder() //
			.eventType("TYPE-B") //
			.sender("SimpleConsumerConditionSource") //
			.build();

	private final Instant timestamp = Instant.now();
	private final String text;
}
