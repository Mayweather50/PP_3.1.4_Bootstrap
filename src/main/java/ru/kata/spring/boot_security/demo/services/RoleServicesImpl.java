package ru.kata.spring.boot_security.demo.services;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;

import java.util.List;

@Service
public class RoleServicesImpl implements RoleServices {
    private final RoleRepository roleRepository;

    public RoleServicesImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public void addRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.getById(id);
    }

    //@PersistenceContext
    //private EntityManager entityManager;
    //@Autowired
    //public RoleServicesImpl(EntityManager entityManager) {
//
    //    this.entityManager = entityManager;
    //}
//
    //@Override
    //public List<Role> getAllRoles() {
    //    TypedQuery<Role> roleTypedQuery = entityManager.createQuery("select r from Role r",Role.class);
    //    return roleTypedQuery.getResultList();
    //}
//
    //@Override
    //public void deleteById(Long id) {
    //    Role role = entityManager.find(Role.class,id);
    //    if(role != null) {
    //        entityManager.remove(role);
    //    }
    //}
//
    //@Override
    //public void addRole(Role role) {
    //    entityManager.persist(role);
    //}
//
    //@Override
    //public Role getRoleById(Long id) {
    //    Role role = entityManager.find(Role.class,id);
    //    return role;
    //}
}
