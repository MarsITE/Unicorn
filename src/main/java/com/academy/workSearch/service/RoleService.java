package com.academy.workSearch.service;

import com.academy.workSearch.dao.CrudDAO;
import com.academy.workSearch.model.Role;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

//@Service
@Transactional
@AllArgsConstructor
public class RoleService {

    @Autowired
    private final CrudDAO<Role> roleDAO;

    public List<Role> findAll() {
        return roleDAO.findAll();
    }

    public void save(Role role) {
        roleDAO.save(role);
    }

    public Role get(UUID id) {
        return roleDAO.get(id);
    }

    public void delete(UUID id) {
        roleDAO.delete(id);
    }
}
