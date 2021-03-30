package com.conference.demo.repositories;

import com.conference.demo.models.Presentation;
import com.conference.demo.models.Room;
import com.conference.demo.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PresentationRepository extends CrudRepository<Presentation, Long> {

    @Query("SELECT presentation from Presentation presentation Where presentation.schedule.room.roomNumber = :room")
    public Iterable<Presentation> getPresentationsByRoom(@Param("room") String room);
}
