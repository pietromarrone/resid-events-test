package it.demanio.resid.events;

import com.resid.events.DomainEvent;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EventOther implements DomainEvent {

	private String sender = "SimpleConsumerConditionSource";
	private String messageType;

	private String nome;
	private String cognome;
	private String codiceFiscale;

//	public EventOther(String type, T eventPayload) {
//		super(type, eventPayload);
//	}

}
