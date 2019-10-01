package it.demanio.resid.events;

import java.math.BigDecimal;

import it.demanio.events.Event;
import it.demanio.events.Header;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class EventA extends Event {

  private String text;
  private BigDecimal numero = BigDecimal.TEN;

  public EventA() {
    header = new Header("TYPE-A", "TestEventsHelper");
  }

  public EventA(String text) {
    this();
    this.text = text;
  }
}
