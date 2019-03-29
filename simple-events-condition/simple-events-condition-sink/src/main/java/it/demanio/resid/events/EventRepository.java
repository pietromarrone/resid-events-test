package it.demanio.resid.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * Mock Stateless Repository Utilizzato per non avere dipendenze da alcun
 * Database
 *
 * @author pmarrone
 */
@Component
public class EventRepository {

	private Map<String, List<EventReceived>> eventsMap;

	EventRepository() {
		eventsMap = new HashMap<>();
		eventsMap.put("A", new ArrayList<EventReceived>());
		eventsMap.put("B", new ArrayList<EventReceived>());
	}

	public boolean addA(EventReceived event) {
		return eventsMap.get("A").add(event);
	}

	public boolean addB(EventReceived event) {
		return eventsMap.get("B").add(event);
	}

	public List<EventReceived> findAllEventsA() {
		return Collections.unmodifiableList(eventsMap.get("A"));
	}

	public List<EventReceived> findAllEventsB() {
		return Collections.unmodifiableList(eventsMap.get("B"));
	}
}
