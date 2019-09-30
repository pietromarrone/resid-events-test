package it.demanio.resid.events;

import java.time.Instant;

import lombok.Data;

@Data
public class PayloadText {
	private String text;
	private Instant timestamp;
}
