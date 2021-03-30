package com.conference.demo.controllers;

import com.conference.demo.models.*;
import com.conference.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.sql.Time;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class PresentationsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PresentationRepository presentationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/presentations")
    public String presentations(Model model) {
        Iterable<Presentation> presentations = presentationRepository.findAll();
        model.addAttribute("presentations", presentations);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        User authUser = userRepository.getUserByLogin(login);
        model.addAttribute("authUser", authUser);
        return "presentations";
    }

    @GetMapping("/presentations/add")
    public String presentationsAdd(Model model) {
        Iterable<User> presenters = userRepository.findAll();
        model.addAttribute("presenters", presenters);
        return "presentation-add";
    }

    @PostMapping("/presentations/add")
    public String presentationsPostAdd(@RequestParam String title, @RequestParam String description,
                                       @RequestParam String room, @RequestParam Date date,
                                       @RequestParam String beginning_time, @RequestParam String ending_time,
                                       @RequestParam List<String> presenters, Model model) throws ParseException {
        beginning_time += ":00";
        ending_time += ":00";
        Time begTime = Time.valueOf(beginning_time);
        Time endTime = Time.valueOf(ending_time);
        Set<User> presentersSet = new HashSet<>();

        for (String userLogin : presenters) {
            User user = userRepository.getUserByLogin(userLogin);
            presentersSet.add(user);
        }

        Schedule schedule;
        if(roomRepository.getRoomByRoomNumber(room) == null) {
            Room newRoom = new Room();
            newRoom.setRoomNumber(room);
            schedule = new Schedule(date, begTime, endTime, newRoom);
        }
        else {
            schedule = new Schedule(date, begTime, endTime, roomRepository.getRoomByRoomNumber(room));
        }

        Iterable<Presentation> presentations = presentationRepository.getPresentationsByRoom(room);

        Iterator<Presentation> iterator = presentations.iterator();
        while(iterator.hasNext()) {
            Presentation nextPresentation = iterator.next();
            if (nextPresentation.getSchedule().getStartDate().compareTo(date) == 0) {
                if((nextPresentation.getSchedule().getEndingTime().compareTo(begTime) > 0 &&
                        nextPresentation.getSchedule().getBeginningTime().compareTo(begTime) < 0) ||
                        (nextPresentation.getSchedule().getBeginningTime().compareTo(endTime) < 0 &&
                                nextPresentation.getSchedule().getBeginningTime().compareTo(begTime) > 0)) {
                    model.addAttribute("error", "Someone already has a presentation at that time. Choose another one.");
                    Iterable<User> allPresenters = userRepository.findAll();
                    model.addAttribute("presenters", allPresenters);
                    return "presentation-add";
                }
            }
        }

        Presentation presentation = new Presentation(title, description, presentersSet, schedule);
        presentationRepository.save(presentation);
        return "redirect:/presentations";
    }

    @GetMapping("/presentations/{id}/edit")
    public String presentationEdit(@PathVariable(value = "id") long id, Model model) {
        if(!presentationRepository.existsById(id)) {
            return "redirect:/home";
        }

        Optional<Presentation> presentation = presentationRepository.findById(id);
        ArrayList<Presentation> presentationList = new ArrayList<>();
        presentation.ifPresent(presentationList::add);
        model.addAttribute("presentation", presentationList);

        Iterable<User> presenters = userRepository.findAll();
        model.addAttribute("presenters", presenters);
        return "presentation-edit";
    }

    @PostMapping("/presentations/{id}/edit")
    public String userEditPost(@PathVariable(value = "id") long id, Model model, @RequestParam String title,
                               @RequestParam String description, @RequestParam List<String> presenters,
                               @RequestParam Date date, @RequestParam String beginning_time,
                               @RequestParam String ending_time, @RequestParam String room) {
        Presentation presentation = presentationRepository.findById(id).orElseThrow();
        presentation.setTitle(title);
        presentation.setDescription(description);
        Schedule prevSchedule = presentation.getSchedule();
        if(beginning_time.length() == 5) {
            beginning_time += ":00";
        }
        if(ending_time.length() == 5) {
            ending_time += ":00";
        }
        Time begTime = Time.valueOf(beginning_time);
        Time endTime = Time.valueOf(ending_time);
        Set<User> presentersSet = new HashSet<>();

        for (String userLogin : presenters) {
            User user = userRepository.getUserByLogin(userLogin);
            presentersSet.add(user);
        }

        Schedule schedule;
        if(roomRepository.getRoomByRoomNumber(room) == null) {
            Room newRoom = new Room();
            newRoom.setRoomNumber(room);
            schedule = new Schedule(date, begTime, endTime, newRoom);
        }
        else {
            schedule = new Schedule(date, begTime, endTime, roomRepository.getRoomByRoomNumber(room));
        }

        presentation.setSchedule(schedule);
        presentation.setPresenters(presentersSet);

        Iterable<Presentation> presentations = presentationRepository.getPresentationsByRoom(room);

        Iterator<Presentation> iterator = presentations.iterator();
        while(iterator.hasNext()) {
            Presentation nextPresentation = iterator.next();
            if (nextPresentation.getSchedule().getStartDate().compareTo(date) == 0) {
                if((nextPresentation.getSchedule().getEndingTime().compareTo(begTime) > 0 &&
                        nextPresentation.getSchedule().getBeginningTime().compareTo(begTime) < 0) ||
                        (nextPresentation.getSchedule().getBeginningTime().compareTo(endTime) < 0 &&
                                nextPresentation.getSchedule().getBeginningTime().compareTo(begTime) > 0)) {
                    model.addAttribute("error", "Someone already has a presentation at that time. Choose another one.");
                    Iterable<User> allPresenters = userRepository.findAll();
                    model.addAttribute("presenters", allPresenters);
                    return "presentation-add";
                }
            }
        }

        presentationRepository.save(presentation);
        scheduleRepository.delete(prevSchedule);
        return "redirect:/users";
    }

    @GetMapping("/presentations/{id}/delete")
    public String presentationDelete(@PathVariable(value = "id") long id, Model model) {
        if(!presentationRepository.existsById(id)) {
            return "redirect:/presentations";
        }

        Optional<Presentation> presentation = presentationRepository.findById(id);
        ArrayList<Presentation> presentationList = new ArrayList<>();
        presentation.ifPresent(presentationList::add);
        model.addAttribute("presentation", presentationList);
        return "presentation-delete";
    }

    @PostMapping("/presentations/{id}/delete")
    public String presentationDeletePost(@PathVariable(value = "id") long id, Model model) {
        Presentation presentation = presentationRepository.findById(id).orElseThrow();
        presentation.getPresenters().clear();
        presentationRepository.save(presentation);
        presentationRepository.deleteById(id);
        return "redirect:/presentations";
    }
}
