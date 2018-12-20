package io.agileinfra.hazelcast.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;
import com.hazelcast.core.IMap;
import io.agileinfra.hazelcast.dto.ScheduleRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by <a href="mailto:louis.gueye@gmail.com">Louis Gueye</a>.
 */
@RequestMapping("/schedule")
@RestController
@Slf4j
public class ScheduleResource {

	private final Integer port;
	private final HazelcastInstance hazelcast;
	private final ScheduledExecutorService scheduledExecutorService;
	private final ObjectMapper objectMapper;
	private static final Duration OFFSET = Duration.ofSeconds(30);

	public ScheduleResource(@Value("${server.port}") final Integer port, final HazelcastInstance hazelcast,
			final ScheduledExecutorService scheduledExecutorService, final ObjectMapper objectMapper) {
		this.port = port;
		this.hazelcast = hazelcast;
		this.scheduledExecutorService = scheduledExecutorService;
		this.objectMapper = objectMapper;
	}

	@PostMapping
	public void schedule(@RequestBody final ScheduleRequest detached) throws IOException {
		final String scheduleId = detached.getId();
		final IMap<String, String> schedules = hazelcast.getMap("schedules");
		String value = schedules.get(scheduleId);
		ScheduleRequest persisted = null;
		if (value == null) {
			value = objectMapper.writeValueAsString(detached.toBuilder().submitted(false).build());
			schedules.put(scheduleId, value);
		} else {
			persisted = objectMapper.readValue(value, ScheduleRequest.class);
		}
		if (persisted != null && persisted.isSubmitted()) {
			log.info("Member [localhost:{}] ignored job {}: the job was already executed", port, scheduleId);
			return;
		}
		scheduledExecutorService.schedule(() -> {
			ILock lock = hazelcast.getLock(scheduleId);
			try {
				if (!lock.tryLock()) {
					log.info("Member [localhost:{}] ignored job {}: job is already being executed (locked)", port, scheduleId);
					return;
				}
				final ScheduleRequest schedule = objectMapper.readValue(schedules.get(scheduleId), ScheduleRequest.class);
				log.info("Job execution attempt for {}", scheduleId);
				if (schedule == null || schedule.isSubmitted()) {
					log.info("Member [localhost:{}] ignored job {}: the job was already submitted", port, scheduleId);
					return;
				}
				// do the actual job
				schedules.put(scheduleId, objectMapper.writeValueAsString(schedule.toBuilder().submitted(true).build()));
				log.info("Member [localhost:{}] scheduled job {}", port, scheduleId);

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (lock.isLockedByCurrentThread()) {
						lock.unlock();
					}
				} catch (Exception e) {
					log.info("Member [localhost:{}] tried to unlock lock but was probably not the owner {}", port, scheduleId);
				}
			}
		}, Duration.between(Instant.now(), detached.getAt().plus(OFFSET)).toMillis(), TimeUnit.MILLISECONDS);

	}
}
