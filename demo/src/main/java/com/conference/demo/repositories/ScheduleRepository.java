package com.conference.demo.repositories;

import com.conference.demo.models.Schedule;
import org.springframework.data.repository.CrudRepository;

public interface ScheduleRepository extends CrudRepository<Schedule, Long> {
}
