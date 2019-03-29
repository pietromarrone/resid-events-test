package it.demanio.resid.events;

public interface DomainEventPublisher {

    void publish(DomainEvent event);

}
