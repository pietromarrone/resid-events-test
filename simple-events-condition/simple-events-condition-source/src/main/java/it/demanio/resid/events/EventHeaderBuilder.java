package it.demanio.resid.events;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder(builderClassName = "builder", builderMethodName = "headerBuilder")
public class EventHeaderBuilder implements EventHeader {
	@NonNull
	String eventType;
	@NonNull
	String sender;
}
