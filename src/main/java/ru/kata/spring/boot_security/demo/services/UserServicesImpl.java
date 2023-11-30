package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserServicesImpl implements UserServices {
    private UserRepository userRepository;
    private RoleServices roleServices;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserServicesImpl(BCryptPasswordEncoder bCryptPasswordEncoder,UserRepository userRepository,RoleServices roleServices) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleServices = roleServices;
    }
    @Transactional
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @Transactional
    @Override
    public User getUserInfo () {
        Optional<User> identity = getAllUsers()
                .stream()
                .filter(x -> "admin user".equals(x.getUsername()) || "admin".equals(x.getUsername()))
                .findFirst();
            User info = identity.get();
        return info;
    }
    @Transactional
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NullPointerException("Значение не найдено в базе данных"));
    }
    @Transactional
    @Override
    public void addUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        String username = user.getRoles().stream()
                .map(role -> role.getName().replace("ROLE_", "").toLowerCase())
                .collect(Collectors.joining(" "));
        Set<Role> roles = user.getRoles().stream()
                .map(role -> "ROLE_" + role.getName())
                .map(roleServices::findByName)
                .collect(Collectors.toSet());
        user.setRoles(roles);
        user.setUsername(username);
        userRepository.save(user);
    }

    public void updateUser(User user, Long id) {
        if (userRepository.existsById(id)) {
            String username = user.getRoles().stream()
                    .map(x -> x.getName().replace("ROLE_", "").toLowerCase())
                    .collect(Collectors.joining(" "));
            Set<Role> roles = user.getRoles().stream()
                    .map(role -> "ROLE_" + role.getName())
                    .map(roleServices::findByName)
                    .collect(Collectors.toSet());
            user.setRoles(roles);
            user.setUsername(username);
            userRepository.save(user);
        }
    }

    @Transactional
    @Override
    public void removeUser(Long id) {
        userRepository.deleteById(id);
    }


    @Override
    @Transactional
    public User findFirstByEmail(String email) throws UsernameNotFoundException {
        User user = userRepository.findFirstByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with email %s not found!", email));
        }
        return user;
    }

    @Override
   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       User user = findFirstByEmail(email);
       if (user != null) {
           return new org.springframework.security.core.userdetails.User(user.getEmail(),
                   user.getPassword(), mapRolesToAuthorities(user.getRoles()));
       } else {
           throw new UsernameNotFoundException(String.format("User %s not found", email));
       }
   }
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r-> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }


}
