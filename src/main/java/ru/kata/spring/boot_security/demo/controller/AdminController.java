package ru.kata.spring.boot_security.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.services.RoleServices;
import ru.kata.spring.boot_security.demo.services.UserServices;

import java.util.Collection;
import java.util.List;
import java.util.Set;
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
        return "admin";
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

    @PatchMapping("/user/{id}")
    public String updateUser(@ModelAttribute("user")User user) {
        userServices.updateUser(user);
        return "redirect:/admin";

    }
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id")Long id) {
        userServices.removeUser(id);
        return "redirect:/admin";

    }
    @PostMapping("/user")
    public String addUser(@ModelAttribute("user")User user,@RequestParam(name = "roles", required = false) Long roleIds) {
        Collection<Role> setrole = roleServices.getAllRoles().stream().filter(x -> x.getId() == roleIds).collect(Collectors.toSet());
        user.setRoles(setrole);
        userServices.addUser(user);
        return "redirect:admin";
    }



}
