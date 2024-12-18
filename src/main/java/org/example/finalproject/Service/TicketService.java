package org.example.finalproject.Service;


import ch.qos.logback.core.util.DelayStrategy;
import org.example.finalproject.Entity.Event;
import org.example.finalproject.Entity.Ticket;
import org.example.finalproject.Entity.User;
import org.example.finalproject.Repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    public boolean checkQuantity(Event event){
        int sold=ticketRepository.countByEvent(event);
        if(sold<event.getQuantity()) {
            return true;
        }
        else{
            return false;
        }
    }
    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getTicketsByUser(User user) {
        return ticketRepository.findByUser(user);
    }
}
