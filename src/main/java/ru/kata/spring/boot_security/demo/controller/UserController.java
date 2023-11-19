package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.services.UserServices;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {
    private UserServices userServices;

    @Autowired
    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @GetMapping("")
    public String userPage(Principal principal, Model model ) {
        User user = userServices.findFirstByEmail(principal.getName());
        model.addAttribute("userPrincipal", user);
        return "user_page";
    }
}
