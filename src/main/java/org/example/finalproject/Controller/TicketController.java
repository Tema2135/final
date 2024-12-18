package org.example.finalproject.Controller;


import org.example.finalproject.Entity.Event;
import org.example.finalproject.Entity.Ticket;
import org.example.finalproject.Entity.User;
import org.example.finalproject.Service.TicketService;
import org.example.finalproject.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.example.finalproject.Service.EventService;

import java.time.LocalDateTime;


@EnableJpaRepositories
@org.springframework.stereotype.Controller
public class TicketController {
    @Autowired
    private EventService eventService;

    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserService userService;

    @PostMapping("/event/buy")
    public String buyTicket(@RequestParam("id") Long eventId, Model model) {

        Event event = eventService.getEventById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid event ID: " + eventId));
        if(!ticketService.checkQuantity(event)){
            return "redirect:/event?error=allTicketsSold";
        }

        User user = userService.findbyUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();

        Ticket ticket = new Ticket();
        ticket.setEvent(event);
        ticket.setUser(user);
        ticket.setPurchaseDate(LocalDateTime.now());

        ticketService.saveTicket(ticket);

        return "redirect:/event";
    }
}
