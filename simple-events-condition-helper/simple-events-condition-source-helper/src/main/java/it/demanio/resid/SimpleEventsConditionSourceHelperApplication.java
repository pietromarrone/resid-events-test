package it.demanio.resid;

import static com.resid.events.EventHeaderBuilder.headerBuilder;

import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.resid.events.DomainEvent;
import com.resid.events.EventHeader;
import com.resid.events.configuration.EventProducerConfiguration;
import com.resid.events.publisher.DomainEventPublisher;

import it.demanio.resid.dto.EventDto;
import it.demanio.resid.events.EventA;
import it.demanio.resid.events.EventB;
import it.demanio.resid.events.EventWithoutHeader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@SpringBootApplication
@Import(EventProducerConfiguration.class)
public class SimpleEventsConditionSourceHelperApplication {

	@Autowired
	DomainEventPublisher publisher;

	public static void main(String[] args) {
		SpringApplication.run(SimpleEventsConditionSourceHelperApplication.class, args);
	}

	// Rest Controller
	@GetMapping
	String hello() {
		return "Hello from Simple Events Source (Producer)";
	}

	@PostMapping("publish")
	ResponseEntity<Void> sendOne(@RequestBody EventDto eventDto) {
		log.info("publish: {}", eventDto);

		DomainEvent e = eventFactory(eventDto);
		if (e != null) {
			log.info("Sending Event {}", e);
			publisher.publish(e);
		} else {
			sendWithHeader(eventDto);
		}

		return ResponseEntity.ok().build();
	}

	@PostMapping("publish/{num}")
	ResponseEntity<Void> sendMany(@PathVariable long num, @RequestBody EventDto eventDto) {
		log.info("publish/{}: {}", num, eventDto);

		new Thread(() -> {
			LongStream.range(0, num).forEach(i -> {
				DomainEvent e = eventFactory(eventDto);
				if (e != null) {
					log.info("Sending Event {}", e);
					publisher.publish(e);
				} else {
					sendWithHeader(eventDto);
				}
			});
		}).start();

		return ResponseEntity.ok().build();
	}

	@PostMapping("publish/{num}/sleep/{sleep}")
	ResponseEntity<Void> sendManyWithSleep(@PathVariable long num, @PathVariable long sleep,
			@RequestBody EventDto eventDto) throws InterruptedException {
		log.info("publish/{}/sleep/{}: {}", num, sleep, eventDto);

		new Thread(() -> {
			LongStream.range(0, num).forEach(i -> {
				try {
					TimeUnit.MILLISECONDS.sleep(sleep);
				} catch (InterruptedException e) {
				}
				DomainEvent e = eventFactory(eventDto);
				if (e != null) {
					log.info("Sending Event {}", e);
					publisher.publish(e);
				} else {
					sendWithHeader(eventDto);
				}
			});
		}).start();

		return ResponseEntity.ok().build();
	}

	private DomainEvent eventFactory(EventDto eventDto) {
		switch (eventDto.getType()) {
		case "TYPE-A":
			return new EventA(eventDto.getText());
		case "TYPE-B":
			return new EventB(eventDto.getText());
		default:
			return null;
		}
	}

	private void sendWithHeader(EventDto eventDto) {
		EventWithoutHeader other = new EventWithoutHeader();
		other.setNome("Nome " + eventDto.getText());
		other.setCognome("Cognome " + eventDto.getText());
		other.setCodiceFiscale("Codice Fiscale " + eventDto.getText());

		EventHeader header = headerBuilder() //
				.eventType("TYPE-OTHER") //
				.sender("SimpleConsumerConditionSource") //
				.build();

		log.info("Sending Event {} with Header {}", other, header);
		publisher.publish(header, other);
	}

}
