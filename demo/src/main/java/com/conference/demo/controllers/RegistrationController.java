package com.conference.demo.controllers;

import com.conference.demo.models.Role;
import com.conference.demo.models.User;
import com.conference.demo.repositories.RoleRepository;
import com.conference.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model){
        User userFromDb = userRepository.getUserByLogin(user.getLogin());
        if (userFromDb != null){
            model.put("error", "User already exists!");
            return "registration";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));

        user.setEnabled(true);
        Role role = roleRepository.getRoleByName("LISTENER");

        user.setRoles(Collections.singleton(role));
        userRepository.save(user);
        return "redirect:/login";
    }

}
