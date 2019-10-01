package it.demanio.resid.events;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.Data;

@Data
public class PayloadText {
  private String text;
  private BigDecimal numero;
  private Instant timestamp = Instant.now();
}
