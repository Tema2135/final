package org.example.finalproject.Repository;


import org.example.finalproject.Entity.Event;
import org.example.finalproject.Entity.Ticket;
import org.example.finalproject.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    int countByEvent(Event event);
    List<Ticket> findByUser(User user);
}
