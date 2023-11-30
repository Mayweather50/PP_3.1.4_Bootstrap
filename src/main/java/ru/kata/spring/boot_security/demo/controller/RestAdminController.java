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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class RestAdminController implements ErrorController {
    private UserServices userServices;
    private RoleServices roleServices;
    private Logger logger = LoggerFactory.getLogger(RestAdminController.class);

    @Autowired
    public RestAdminController(UserServices userServices, RoleServices roleServices) {
        this.userServices = userServices;
        this.roleServices = roleServices;
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userServices.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getByUserId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userServices.getUserById(id), HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<Void> addUser(@RequestBody User user) {
        userServices.addUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles() {
        return new ResponseEntity<>(roleServices.getAllRoles(), HttpStatus.OK);
    }

   @DeleteMapping("/delete/{id}")
   public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
       userServices.removeUser(id);
       return new ResponseEntity<>(HttpStatus.OK);
   }

    @PatchMapping("/users/{id}")
    public ResponseEntity<Void> updateUser(@RequestBody User user,@PathVariable("id")Long id) {
        try {
            logger.info("Обновление пользователя с ID: " + user.getId());
            userServices.updateUser(user,id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Произошла ошибка при обновлении пользователя", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/showUser")
    public ResponseEntity<User> showUser(Principal principal) {
        User user = userServices.findFirstByEmail(principal.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}