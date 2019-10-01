package it.demanio.resid.events;

import it.demanio.events.Event;
import it.demanio.events.Header;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class EventB extends Event {

  private final Header header = new Header("TYPE-B", "TestEventsHelperSpring1");
  private String text;

  public EventB() {
  }

  public EventB(String text) {
    this();
    this.text = text;
  }
}
