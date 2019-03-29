package it.demanio.resid.ui;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import it.demanio.resid.events.EventReceived;
import it.demanio.resid.events.EventRepository;

@RestController
class EventsReceivedController {

	@Autowired
	EventRepository eventRepository;

	@GetMapping
	String hello() {
		return "Hello from Simple Events Sink (Consumer)";
	}

	@GetMapping("events")
	List<EventReceived> getAllEvents() {
		return eventRepository.findAllEvents();
	}

}
