package io.agileinfra.hazelcast.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by <a href="mailto:louis.gueye@gmail.com">Louis Gueye</a>.
 */
@SpringBootApplication
public class HazelcastServer {
	public static void main(String[] args) {
		SpringApplication.run(HazelcastServer.class, args);
	}
}
