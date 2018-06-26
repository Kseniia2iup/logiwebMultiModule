package ru.tsystems.javaschool.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.javaschool.dto.User;
import ru.tsystems.javaschool.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String login) {
            User user = userService.findByLogin(login);
            LOGGER.info("User : {}", user);
            if (user == null) {
                LOGGER.debug("User not found");
                throw new UsernameNotFoundException("Username not found");
            }
            return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(),
                    true, true, true, true, getGrantedAuthorities(user));
    }


    private List<GrantedAuthority> getGrantedAuthorities(User user){
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+user.getRole()));
        LOGGER.info("authorities : {} for user : {}",authorities, user.getLogin());
        return authorities;
    }
}
