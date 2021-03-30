package com.conference.demo.controllers;

import com.conference.demo.models.Role;
import com.conference.demo.models.User;
import com.conference.demo.repositories.RoleRepository;
import com.conference.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/users")
    public String users(Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/users/{id}/edit")
    public String userEdit(@PathVariable(value = "id") long id, Model model) {
        if(!userRepository.existsById(id)) {
            return "redirect:/home";
        }

        Optional<User> user = userRepository.findById(id);
        ArrayList<User> userList = new ArrayList<>();
        user.ifPresent(userList::add);
        model.addAttribute("user", userList);

        Iterable<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        return "user-edit";
    }

    @PostMapping("/users/{id}/edit")
    public String userEditPost(@PathVariable(value = "id") long id, Model model, @RequestParam String name, @RequestParam List<String> roles) {
        User user = userRepository.findById(id).orElseThrow();
        user.setName(name);
        Set<Role> roleSet = new HashSet<>();

        for (String el : roles) {
            Role role = roleRepository.getRoleByName(el);
            roleSet.add(role);
        }

        user.setRoles(roleSet);
        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/delete")
    public String userDelete(@PathVariable(value = "id") long id, Model model) {
        if(!userRepository.existsById(id)) {
            return "redirect:/home";
        }

        Optional<User> user = userRepository.findById(id);
        ArrayList<User> userList = new ArrayList<>();
        user.ifPresent(userList::add);
        model.addAttribute("user", userList);
        return "user-delete";
    }

    @PostMapping("/users/{id}/delete")
    public String userDeletePost(@PathVariable(value = "id") long id, Model model) {
        User user = userRepository.findById(id).orElseThrow();
        user.getRoles().clear();
        userRepository.save(user);
        userRepository.deleteById(id);
        return "redirect:/users";
    }
}
