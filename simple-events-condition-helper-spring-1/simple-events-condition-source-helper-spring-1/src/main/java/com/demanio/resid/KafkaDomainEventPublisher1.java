package com.demanio.resid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import it.demanio.events.Header;
import it.demanio.events.ResidEvent;
import it.demanio.resid.helper.publisher.DomainEventPublisher;

@Component
public class KafkaDomainEventPublisher1 implements DomainEventPublisher {

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Value("${spring.kafka.topic}")
  private String topic;

  public KafkaDomainEventPublisher1() {
    System.out.println();
  }

  @Override
  public void publish(ResidEvent domainEvent) {
    publish(domainEvent.getHeader(), domainEvent);
  }

  @Override
  public void publish(Header header, Object domainEvent) {
    Message<Object> message = MessageBuilder.withPayload(domainEvent)
        .setHeader(KafkaHeaders.TOPIC, topic)
//        .setHeader(KafkaHeaders.PARTITION_ID, 0)
        .setHeader("resid_type", header.getEventType())
        .setHeader("resid_sender", header.getSender())
        .build();

    kafkaTemplate.send(message);
  }

}
