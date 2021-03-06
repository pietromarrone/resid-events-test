package it.demanio.resid.events;

import java.time.Instant;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Event implements DomainEvent {

	private UUID eventId;
	private Instant producedAt;
	private String text;

	public Event(UUID eventId, String text) {
		this.eventId = eventId;
		this.text = text;
		this.producedAt = Instant.now();
	}

	@Override
	public String getType() {
		return "simple-event";
	}
}
