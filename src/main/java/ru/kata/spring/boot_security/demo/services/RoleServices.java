package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.List;

public interface RoleServices {
    List<Role> getAllRoles();
    void deleteById(Long id);

    void addRole(Role role);

    Role getRoleById(Long id);
}
