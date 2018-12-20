package io.agileinfra.hazelcast.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by <a href="mailto:louis.gueye@gmail.com">Louis Gueye</a>.
 */
@SpringBootApplication
public class HazelcastClient {
	public static void main(String[] args) {
		SpringApplication.run(HazelcastClient.class, args);
	}
}
