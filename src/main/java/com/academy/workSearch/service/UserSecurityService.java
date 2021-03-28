package com.academy.workSearch.service;

import com.academy.workSearch.dao.UserDAO;
import com.academy.workSearch.model.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(value = "UserSecurityService")
@AllArgsConstructor
public class UserSecurityService implements UserDetailsService {

    @Autowired
    private final UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDAO.getByEmail(email);
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }
}
