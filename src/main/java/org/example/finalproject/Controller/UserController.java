package org.example.finalproject.Controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.finalproject.Entity.Event;
import org.example.finalproject.Entity.Ticket;
import org.example.finalproject.Entity.User;
import org.example.finalproject.Repository.TicketRepository;
import org.example.finalproject.Service.EventService;
import org.example.finalproject.Service.TicketService;
import org.example.finalproject.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;


@EnableJpaRepositories
@org.springframework.stereotype.Controller
public class UserController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String registerPage(){
        return "registerPage";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("username") String username,
                               @ModelAttribute("password") String password,
                               Model model) {
        userService.registerUser(username, password);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "loginPage";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("username") String username,
                            @ModelAttribute("password") String password,
                            Model model) {
        Authentication authentication = userService.authenticateUser(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "redirect:/home";
    }

    @PostMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "redirect:/login";
    }

    @GetMapping("/user")
    public String userPage(Model model) {
        if(userService.isAdmin(SecurityContextHolder.getContext().getAuthentication().getName())){
            return "redirect:/admin";
        }
        Optional<User> optionalUser = userService.findbyUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Ticket> tickets=ticketService.getTicketsByUser(optionalUser.get());
        model.addAttribute("tickets", tickets);
        return "userPage";
    }

    @PostMapping("/user/password")
    public String newPassword(@RequestParam("password") String password) {
        userService.changePassword(password);
        return "redirect:/user";
    }

    @GetMapping("/forget")
    public String getForget(){
        return "forgetPage";
    }

    @PostMapping("/forget")
    public String forget(@RequestParam("username") String username){
        Optional<User> optionalUser = userService.findbyUsername(username);
        if(optionalUser.isPresent()) {
            userService.resetPassword(optionalUser.get());
            return "redirect:/login";
        }
        return "redirect:/forget?error=userNotFound";
    }
}
