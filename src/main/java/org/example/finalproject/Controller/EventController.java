package org.example.finalproject.Controller;

import org.example.finalproject.Entity.Event;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import org.example.finalproject.Service.EventService;

import java.util.List;


@EnableJpaRepositories
@org.springframework.stereotype.Controller
public class EventController {
    @Autowired
    private EventService eventService;



    @GetMapping("/event")
    public String listEvents(Model model) {
        List<Event> events=eventService.getAllEvents();
        for (Event event : events) {
            int remainingTickets = eventService.getRemainingTickets(event);
            event.setRemainingTickets(remainingTickets);
        }
        model.addAttribute("events", events);
        return "eventPage";
    }

}
