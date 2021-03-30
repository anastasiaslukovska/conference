package com.conference.demo.repositories;

import com.conference.demo.models.Role;
import com.conference.demo.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface UserRepository extends CrudRepository<User, Long>{
    @Query("SELECT user from User user Where user.login = :login")
    public User getUserByLogin(@Param("login") String login);
}
