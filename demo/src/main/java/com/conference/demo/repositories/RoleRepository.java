package com.conference.demo.repositories;

import com.conference.demo.models.Presentation;
import com.conference.demo.models.Role;
import com.conference.demo.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    @Query("SELECT role from roles role Where role.name = :name")
    public Role getRoleByName(@Param("name") String name);
}
