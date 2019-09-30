package com.demanio.resid.events;

import java.time.Instant;

import it.demanio.events.Event;
import it.demanio.events.Header;
import it.demanio.events.HeaderBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class EventB extends Event {

	private Header header = HeaderBuilder.builder() //
			.eventType("TYPE-B") //
			.sender("SimpleConsumerConditionSource") //
			.build();

	private final Instant timestamp = Instant.now();
	private final String text;
}
