package it.demanio.resid.events;

import java.time.Duration;
import java.time.Instant;

import lombok.Value;

@Value
public class EventReceived {
	Instant receivedAt;
	Instant producedAt;
	String text;

	public String getLatency() {
		return Duration.between(producedAt, receivedAt).toMillis() + "(ms)";
	}
}
