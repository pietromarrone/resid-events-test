package it.demanio.resid.events;

import java.time.Instant;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EventWithoutHeader {

	private String nome;
	private String cognome;
	private String codiceFiscale;
	private final Instant timestamp = Instant.now();

}
