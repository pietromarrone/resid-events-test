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

	private Map<String, List<Object>> eventsMap;

	EventRepository() {
		eventsMap = new HashMap<>();
		eventsMap.put("A", new ArrayList<>());
		eventsMap.put("B", new ArrayList<>());
	}

	public boolean addA(Object event) {
		return eventsMap.get("A").add(event);
	}

	public boolean addB(Object event) {
		return eventsMap.get("B").add(event);
	}

	public List<Object> findAllEventsA() {
		return Collections.unmodifiableList(eventsMap.get("A"));
	}

	public List<Object> findAllEventsB() {
		return Collections.unmodifiableList(eventsMap.get("B"));
	}
}
