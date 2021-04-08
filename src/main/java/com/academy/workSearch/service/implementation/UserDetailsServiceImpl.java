package com.academy.workSearch.service.implementation;

import com.academy.workSearch.dao.UserDAO;
import com.academy.workSearch.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service(value = "UserDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username)  {
        userDAO.setClazz(User.class);
        return userDAO.getByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }
}
