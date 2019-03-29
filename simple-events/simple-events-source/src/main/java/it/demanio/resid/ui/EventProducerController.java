package it.demanio.resid.ui;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.demanio.resid.events.DomainEventPublisher;
import it.demanio.resid.events.Event;
import it.demanio.resid.ui.dto.EventDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController()
class EventsReceivedController {

	@Autowired
	DomainEventPublisher publisher;

	@GetMapping
	String hello() {
		return "Hello from Simple Events Source (Producer)";
	}

	@PostMapping("publish")
	ResponseEntity<Void> send(@RequestBody EventDto eventDto) {
		log.info("Sending Event {}", eventDto);
		publisher.publish(new Event(UUID.randomUUID(), eventDto.getText()));

		return ResponseEntity.ok().build();
	}

	@PostMapping("publish/{num}")
	ResponseEntity<Void> send(@PathVariable int num, @RequestBody EventDto eventDto) {
		log.info("Sending {} Events as {}", num, eventDto);
		for (int i = 0; i < num; i++) {
			publisher.publish(new Event(UUID.randomUUID(), num + "-" + eventDto.getText()));
		}

		return ResponseEntity.ok().build();
	}

}
