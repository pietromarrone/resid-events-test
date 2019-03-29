package it.demanio.resid.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Mock Stateless Repository Utilizzato per non avere dipendenze da alcun
 * Database
 *
 * @author pmarrone
 */
@Component
public class EventRepository {

	private List<EventReceived> events = new ArrayList<>();

	public boolean add(EventReceived event) {
		return events.add(event);
	}

	public List<EventReceived> findAllEvents() {
		return Collections.unmodifiableList(events);
	}
}
