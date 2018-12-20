package io.agileinfra.hazelcast.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by <a href="mailto:louis.gueye@gmail.com">Louis Gueye</a>.
 */
@Configuration
public class HazelcastServerConfiguration {
	@Bean
	public ScheduledExecutorService scheduledExecutorService() {
		return Executors.newScheduledThreadPool(1);
	}
	@Bean
	public ObjectMapper objectMapper() {
		return Jackson2ObjectMapperBuilder.json().serializationInclusion(JsonInclude.Include.NON_NULL) // Don’t include null values
				.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // use ISODate and not Timestamps
				.modules(new JavaTimeModule()).build();
	}

}
