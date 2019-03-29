package it.demanio.resid.sink;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class Event {
	private UUID eventId;
	private Instant producedAt;
	private String text;
}
