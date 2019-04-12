package it.demanio.resid.events;

import com.resid.events.Event;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class EventA extends Event<String> {

	private String sender = "SimpleConsumerConditionSource";

	public EventA(String text) {
		super("TYPE-A", text);
	}

}
