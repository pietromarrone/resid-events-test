package it.demanio.resid.events;

import java.time.Instant;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class EventB implements DomainEvent {

	private String id;
	private UUID eventId;
	private Instant producedAt;
	private String text;
	private String sender = "SimpleConsumerConditionSource";

	public EventB(UUID eventId, String text) {
		this.id = UUID.randomUUID().toString();
		this.eventId = eventId;
		this.text = text;
		this.producedAt = Instant.now();
	}

	@Override
	public String getType() {
		return "TYPE-B";
	}
}
