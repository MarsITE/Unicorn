package com.academy.workSearch.service.implementation;

import com.academy.workSearch.dao.UserDAO;
import com.academy.workSearch.model.User;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.academy.workSearch.exceptionHandling.MessageConstants.NO_SUCH_ENTITY;

@AllArgsConstructor
@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserDAO userDAO;

    /**
     *
     * @param username email authored user
     * @return authored user
     * @throws UsernameNotFoundException if user does not exists
     */
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        userDAO.setClazz(User.class);
        return userDAO.getByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(NO_SUCH_ENTITY));
    }
}
