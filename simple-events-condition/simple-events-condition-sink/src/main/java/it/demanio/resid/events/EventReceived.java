package it.demanio.resid.events;

import java.time.Duration;
import java.time.Instant;

import lombok.Data;

@Data
public class EventReceived {
	Instant receivedAt;
	Instant producedAt;
	String text;

	public String getLatency() {
		return Duration.between(producedAt, receivedAt).toMillis() + "(ms)";
	}

	public void received() {
		this.receivedAt = Instant.now();
	}
}
