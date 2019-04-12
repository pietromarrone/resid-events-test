package it.demanio.resid.events;

import java.time.Duration;
import java.time.Instant;

import lombok.Data;

@Data
public class EventReceived<T> {
	Instant receivedAt;
	Instant timestamp;
	T payload;
	String sender;

	public String getLatency() {
		return Duration.between(timestamp, receivedAt).toMillis() + "(ms)";
	}

	public void received() {
		this.receivedAt = Instant.now();
	}
}
