package io.agileinfra.hazelcast.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import io.agileinfra.hazelcast.dto.ScheduleRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by <a href="mailto:louis.gueye@gmail.com">Louis Gueye</a>.
 */
@RequestMapping("/schedules")
@RestController
@Slf4j
@RequiredArgsConstructor
public class ScheduleResource {

	private final HazelcastInstance hazelcast;
	private final ObjectMapper objectMapper;

	@GetMapping
	public List<ScheduleRequest> readSchedules() {
		return hazelcast.getMap("schedules").values().stream().map(obj -> {
			try {
				return objectMapper.readValue((String) obj, ScheduleRequest.class);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}).filter(Objects::nonNull).collect(Collectors.toList());

	}
}
