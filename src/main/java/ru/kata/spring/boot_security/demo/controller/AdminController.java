package ru.kata.spring.boot_security.demo.controller;
import org.hibernate.annotations.Fetch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.services.RoleServices;
import ru.kata.spring.boot_security.demo.services.UserServices;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserServices userServices;
    private RoleServices roleServices;

    @Autowired
    public AdminController(UserServices userServices, RoleServices roleServices) {
        this.userServices = userServices;
        this.roleServices = roleServices;
    }

     @GetMapping("")
    public String getAllUsers(Model model) {
        model.addAttribute("users",userServices.getAllUsers());
        model.addAttribute("user",new User());
        model.addAttribute("Allroles",roleServices.getAllRoles());
        User userInfo = userServices.getUserInfo();
        model.addAttribute("resultInfo", userInfo);
        return "admin";
    }

    @GetMapping("/details/{id}")
    public String getUserById(@PathVariable("id")Long id,Model model) {
        model.addAttribute("user",userServices.getUserById(id));
        return "/user";
    }


    @PostMapping("/update/{id}")
    public String updateUser(@ModelAttribute("user") User user, @PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userServices.getUserById(id));
        userServices.updateUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userServices.removeUser(id);
        return "redirect:/admin";
    }


    @PostMapping("/user")
    public String addUser(@ModelAttribute("user") User user) {
        userServices.addUser(user);
        return "redirect:/admin";
    }

}
