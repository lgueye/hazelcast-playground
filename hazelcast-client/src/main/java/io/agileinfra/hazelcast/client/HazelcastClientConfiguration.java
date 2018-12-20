package io.agileinfra.hazelcast.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.File;
import java.io.IOException;

/**
 * Created by <a href="mailto:louis.gueye@gmail.com">Louis Gueye</a>.
 */
@Configuration
public class HazelcastClientConfiguration {

	@Bean
	public ObjectMapper objectMapper() {
		return Jackson2ObjectMapperBuilder.json().serializationInclusion(JsonInclude.Include.NON_NULL) // Don’t include null values
				.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // use ISODate and not Timestamps
				.modules(new JavaTimeModule()).build();
	}

	@Bean
	public HazelcastInstance getHazelcastClientInstance(@Value("${spring.hazelcast.config}") final File configFile) throws IOException {
		ClientConfig clientConfig = new XmlClientConfigBuilder(configFile).build();
		return com.hazelcast.client.HazelcastClient.newHazelcastClient(clientConfig);
	}
}
