package it.demanio.resid.events;

public interface DomainEvent {

	String getId();

	String getSender();

	String getType();

}
