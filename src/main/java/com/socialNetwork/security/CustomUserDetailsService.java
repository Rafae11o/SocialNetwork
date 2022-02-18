package com.socialNetwork.security;

import com.socialNetwork.entities.user.User;
import com.socialNetwork.repositories.UserRepository;
import com.socialNetwork.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username).orElseThrow(() ->
            new UsernameNotFoundException("User with this login does not exist"));
        return CustomUserDetails.fromUserEntityToCustomUserDetails(user);
    }
}
