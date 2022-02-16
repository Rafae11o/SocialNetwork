package com.socialNetwork.security;

import com.socialNetwork.entities.user.User;
import com.socialNetwork.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public CustomUserDetailsService(UserService userService){
        this.userService = userService;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByLogin(username).orElseThrow(() ->
            new UsernameNotFoundException("User with this login does not exist"));
        return CustomUserDetails.fromUserEntityToCustomUserDetails(user);
    }
}
