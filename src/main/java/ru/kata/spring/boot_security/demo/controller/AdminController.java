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
public class AdminController implements ErrorController {
    private UserServices userServices;
    private RoleServices roleServices;

    @Autowired
    public AdminController(UserServices userServices, RoleServices roleServices) {
        this.userServices = userServices;
        this.roleServices = roleServices;
    }



     @GetMapping("")
    public String getAllUsers(Model model, Principal principal) {
        User user = new User();
        User result = userServices.findFirstByEmail(principal.getName());
        model.addAttribute("users",userServices.getAllUsers());
        model.addAttribute("user",user);
        model.addAttribute("Allroles",roleServices.getAllRoles());
        Optional<User> identity = userServices.getAllUsers()
                .stream()
                .filter(x -> "admin user".equals(x.getUsername()) || "admin".equals(x.getUsername()))
                .findFirst();
        if(!identity.isEmpty()) {
            User info = identity.get();
            model.addAttribute("resultInfo", info);
        }

        return "admin";
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("usersAll",userServices.getAllUsers());
        return "index";
    }

    @GetMapping("/details/{id}")
    public String getUserById(@PathVariable("id")Long id,Model model) {
        model.addAttribute("user",userServices.getUserById(id));
        return "/user";
    }


    @GetMapping("/user/edit/{id}")
    public String editUser(Model model, @PathVariable(name = "id")Long id) {
        model.addAttribute("user",userServices.getUserById(id));
        model.addAttribute("role",roleServices.getAllRoles());
        return "/edit";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@ModelAttribute("user") User user, @PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userServices.getUserById(id));
        String username = user.getRoles().stream()
                .map(role -> role.getName().replace("ROLE_", "").toLowerCase())
                .collect(Collectors.joining(" "));
        user.setUsername(username);
        userServices.updateUser(user);
        return "redirect:/admin";
    }





    @RequestMapping("/error")
    @ResponseBody
    String error(HttpServletRequest request) {
        return "<h1>Error occurred</h1>";
    }


    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userServices.removeUser(id);
        return "redirect:/admin";
    }


    @PostMapping("/user")
    public String addUser(@ModelAttribute("user") User user) {
        String username = user.getRoles().stream()
                .map(role -> role.getName().replace("ROLE_", "").toLowerCase())
                .collect(Collectors.joining(" "));
        user.setUsername(username);
        userServices.addUser(user);
        return "redirect:/admin";
    }











}
