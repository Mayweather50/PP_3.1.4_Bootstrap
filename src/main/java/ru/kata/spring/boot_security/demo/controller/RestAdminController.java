package ru.kata.spring.boot_security.demo.controller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.services.RoleServices;
import ru.kata.spring.boot_security.demo.services.UserServices;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/admin")
public class RestAdminController implements ErrorController {
    private UserServices userServices;
    private RoleServices roleServices;

    @Autowired
    public RestAdminController(UserServices userServices, RoleServices roleServices) {
        this.userServices = userServices;
        this.roleServices = roleServices;
    }


    @CrossOrigin("http://127.0.0.1:8080")
    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userServices.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @CrossOrigin
    public ResponseEntity<User> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userServices.getUserById(id), HttpStatus.OK);
    }

    //@CrossOrigin(origins = "http://127.0.0.1:8080")
    @PostMapping("/user")
    public ResponseEntity<Void> addUser(@RequestBody User user) {
        Set<Role> roles = new HashSet<>();
        Role adminRole = roleServices.getRoleById(1L);
        roles.add(adminRole);
        user.setRoles(roles);
        userServices.addUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles() {
        return new ResponseEntity<>(roleServices.getAllRoles(), HttpStatus.OK);
    }





   @CrossOrigin
   @DeleteMapping("/delete/{id}")
   public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
       userServices.removeUser(id);
       return new ResponseEntity<>(HttpStatus.OK);
   }

    @CrossOrigin(origins = "http://127.0.0.1:8080")
    @PatchMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        userServices.updateUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/showUser")
    public ResponseEntity<User> showUser(Principal principal) {
        User user = userServices.findFirstByEmail(principal.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}