package com.conference.demo.repositories;

import com.conference.demo.models.Room;
import com.conference.demo.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends CrudRepository<Room, Long> {

    @Query("SELECT room from Room room Where room.roomNumber = :roomNumber")
    public Room getRoomByRoomNumber(@Param("roomNumber") String roomNumber);
}
