package io.agileinfra.hazelcast.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.Instant;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

/**
 * Created by <a href="mailto:louis.gueye@gmail.com">Louis Gueye</a>.
 */
public class ScheduleRequestTest {
	@Test
	public void test() throws JsonProcessingException {
		final ScheduleRequest request = ScheduleRequest.builder().id(UUID.randomUUID().toString()).at(Instant.now()).destination("foo.queue")
				.message("{\"foo\":\"baz\"}").build();
		final String value = Jackson2ObjectMapperBuilder.json().serializationInclusion(JsonInclude.Include.NON_NULL) // Don’t include null values
				.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // use ISODate and not Timestamps
				.modules(new JavaTimeModule()).build().writeValueAsString(request);
		assertTrue(true);
		// System.out.println("value = " + value);
		// System.out
		// .println("{\"id\":\"91dbac95-1dec-4ad1-8a05-65317de2f3a7\",\"at\":\"2018-12-20T12:10:07.260Z\",\"destination\":\"foo.queue\",\"message\":\"{\\\"foo\\\":\\\"baz\\\"}\",\"submitted\":false}");
	}

}