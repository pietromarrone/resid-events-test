package it.demanio.resid.events;

import com.resid.events.Event;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class EventB extends Event<String> {

	private String sender = "SimpleConsumerConditionSource";

	public EventB(String text) {
		super("TYPE-B", text);
	}

}
