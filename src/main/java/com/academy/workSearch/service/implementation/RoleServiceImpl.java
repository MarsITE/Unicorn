package com.academy.workSearch.service.implementation;

import com.academy.workSearch.dao.CrudDAO;
import com.academy.workSearch.exceptionHandling.exceptions.NoSuchEntityException;
import com.academy.workSearch.model.Role;
import com.academy.workSearch.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.academy.workSearch.exceptionHandling.MessageConstants.NO_ROLE;

@Service
@Transactional
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {


    private final CrudDAO<Role> roleDAO;

    public List<Role> findAll() {
        return roleDAO.findAll();
    }

    public Role save(Role role) {
       return roleDAO.save(role);
    }

    public Role get(UUID id) {
        return roleDAO.get(id).orElseThrow(()-> new NoSuchEntityException(NO_ROLE + id));
    }

    public Role delete(UUID id) {
        Role role = roleDAO.get(id).orElseThrow(()-> new NoSuchEntityException(NO_ROLE + id));
        roleDAO.delete(id);
        return role;
    }
}
