package org.example.finalproject.Controller;


import org.example.finalproject.Entity.Event;
import org.example.finalproject.Entity.User;
import org.example.finalproject.Service.AdminService;
import org.example.finalproject.Service.EventService;
import org.example.finalproject.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.time.LocalDate;
import java.util.List;

@EnableJpaRepositories
@org.springframework.stereotype.Controller
public class AdminController {

    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;


    @GetMapping("/admin")
    public String getAll(Model model) {

        List<Event> events=eventService.getAllEvents();
        List<User> users = userService.getAllUsers();

        model.addAttribute("events", events);
        model.addAttribute("users", users);
        return "adminPage";
    }

    @PostMapping("/admin/addEvent")
    public String addEvent(@ModelAttribute("event") Event event,
                           @RequestParam("file") MultipartFile file) throws IOException {
        if(event.getDate().isBefore(LocalDate.now())) {
            return "redirect:/admin?error=due_dateShouldNotBeInThePast";
        }
        adminService.savePhoto(file);
        event.setImage(file.getOriginalFilename());
        eventService.saveEvent(event);
        return "redirect:/admin";
    }
    @GetMapping("/admin/changeEvent/{id}")
    public String getChangeEventPage(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid event ID: " + id));
        model.addAttribute("event", event);
        return "changeEventPage";
    }

    @PostMapping("/admin/changeEvent")
    public String changeEvent(
            @ModelAttribute("event") Event event,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        if (event.getDate().isBefore(LocalDate.now())) {
            return "redirect:/admin?error=due_dateShouldNotBeInThePast";
        }

        if (file != null && !file.isEmpty()) {
            adminService.savePhoto(file);
            event.setImage(file.getOriginalFilename());
        }

        eventService.updateEvent(event);
        return "redirect:/admin";
    }






    @DeleteMapping("/admin/deleteUser")
    public String deleteUser(@RequestParam("id") Long id,
                             Model model) {

        if(userService.getUserById(id).get().getRole().equals("ROLE_ADMIN")) {
            return "redirect:/admin?error=adminCannotBeDeleted";
        }
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/deleteEvent")
    public String deleteEvent(@RequestParam("id") Long id) {
        eventService.deleteEvent(id);
        return "redirect:/admin";
    }
}
