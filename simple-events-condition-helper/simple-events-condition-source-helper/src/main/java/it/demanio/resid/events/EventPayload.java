package it.demanio.resid.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventPayload {
	private String nome;
	private String cognome;
	private String codiceFiscale;

}
