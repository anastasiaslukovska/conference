package com.conference.demo.controllers;

import com.conference.demo.models.Presentation;
import com.conference.demo.models.Role;
import com.conference.demo.models.Schedule;
import com.conference.demo.models.User;
import com.conference.demo.repositories.PresentationRepository;
import com.conference.demo.repositories.ScheduleRepository;
import com.conference.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class MainController {

    @Autowired
    private PresentationRepository presentationRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String defhome(Model model) {
        return "redirect:/home";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/home")
    public String home(Model model) {
        Iterable<Presentation> presentations = presentationRepository.findAll();
        model.addAttribute("presentations", presentations);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        User authUser = userRepository.getUserByLogin(login);
        model.addAttribute("authUser", authUser);
        return "home";
    }

    @GetMapping("/home/{id}/register")
    public String homeRegister(@PathVariable(value = "id") long id, Model model) {
        if(!presentationRepository.existsById(id)) {
            return "redirect:/home";
        }
        Optional<Presentation> presentation = presentationRepository.findById(id);
        ArrayList<Presentation> presentationList = new ArrayList<>();
        presentation.ifPresent(presentationList::add);
        model.addAttribute("presentation", presentationList);

        return "presentation-register";
    }

    @PostMapping("/home/{id}/register")
    public String homeRegisterPost(@PathVariable(value = "id") long id, Model model) {
        Presentation presentation = presentationRepository.findById(id).orElseThrow();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String login = authentication.getName();

        User authUser = userRepository.getUserByLogin(login);

        presentation.getListeners().add(authUser);

        presentationRepository.save(presentation);

        return "redirect:/home";
    }

    @RestController
    @RequestMapping(path = "/schedule")
    public class scheduleController
    {
        @Autowired
        private ScheduleRepository scheduleRepository;

        @GetMapping(path="/", produces = "application/json")
        public Iterable<Schedule> getEmployees()
        {
            Iterable<Schedule> schedules = scheduleRepository.findAll();

            for (Schedule el : schedules) {
                Set<User> presenters = new HashSet<>();
                List<User> presentersList = el.getPresentation().getPresentersList();
                for (User presenter : presentersList) {
                    User user = new User();
                    user.setName(presenter.getName());
                    presenters.add(user);
                }
                el.getPresentation().setPresenters(presenters);
            }
            return schedules;
        }

    }
}