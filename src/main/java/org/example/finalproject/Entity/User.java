package org.example.finalproject.Entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String password;
    private String role;

    public User() {
    }

    public User( Long id, String password, String role, String username) {
        this.id = id;
        this.password = password;
        this.role = role;
        this.username = username;
    }

}

