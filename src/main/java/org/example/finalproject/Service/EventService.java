package org.example.finalproject.Service;

import org.example.finalproject.Entity.Event;
import org.example.finalproject.Entity.User;
import org.example.finalproject.Repository.EventRepository;
import org.example.finalproject.Repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;


    public Page<Event> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }



    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public void saveEvent(Event event) {
        eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public int getRemainingTickets(Event event) {
        int totalTickets = event.getQuantity();

        int soldTickets = ticketRepository.countByEvent(event);

        return totalTickets - soldTickets;
    }
}
