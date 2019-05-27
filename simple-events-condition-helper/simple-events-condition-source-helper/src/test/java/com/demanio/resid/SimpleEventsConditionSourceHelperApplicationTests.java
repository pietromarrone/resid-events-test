package com.demanio.resid;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.demanio.resid.helper.publisher.DomainEventPublisher;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleEventsConditionSourceHelperApplicationTests {

	@Autowired
	DomainEventPublisher publisher;

	@Test
	public void contextLoads() {
		assertThat(publisher).isNotNull();
	}

}
