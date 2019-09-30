package it.demanio.resid.events;

import java.time.Duration;
import java.time.Instant;

import lombok.Data;

@Data
public class EventReceived<T> {
	final Instant receivedAt = Instant.now();
	Instant timestamp;
	T payload;

	public String getLatency() {
		return Duration.between(timestamp, receivedAt).toMillis() + "(ms)";
	}
}
