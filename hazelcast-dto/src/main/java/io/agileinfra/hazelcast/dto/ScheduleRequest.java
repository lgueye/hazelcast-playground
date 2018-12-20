package io.agileinfra.hazelcast.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Created by <a href="mailto:louis.gueye@gmail.com">Louis Gueye</a>.
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleRequest {
	private String id;
	private Instant at;
	private String destination;
	private String message;
	private boolean submitted;
}
