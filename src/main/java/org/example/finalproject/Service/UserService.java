package org.example.finalproject.Service;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.finalproject.Entity.User;
import org.example.finalproject.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JavaMailSender mailSender;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    public void registerUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_USER");
        userRepository.save(user);
    }

    public Authentication authenticateUser(String username, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
    }

    public Optional<User> findbyUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void changePassword(String password) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("temapernebaev@mail.ru");
        message.setTo(user.getUsername());
        message.setSubject("Востановление пароля");
        message.setText("Здравствуйте, " + user.getUsername() +
                "\nВаш пароль был изменен на "+password);

        mailSender.send(message);
    }

    public void resetPassword(User user) {
        String password= RandomStringUtils.randomAlphanumeric(5);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("temapernebaev@mail.ru");
        message.setTo(user.getUsername());
        message.setSubject("Востановление пароля");
        message.setText("Здравствуйте, " + user.getUsername() +
                "\nВаш пароль был изменен на "+password);
        mailSender.send(message);
    }

    public boolean isAdmin(String username) {
        Optional<User> user=userRepository.findByUsername(username);
        return user.isPresent() && user.get().getRole().equals("ROLE_ADMIN");
    }
}

