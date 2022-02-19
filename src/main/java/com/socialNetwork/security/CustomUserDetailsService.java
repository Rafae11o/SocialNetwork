package com.socialNetwork.security;

import com.socialNetwork.controllers.AuthController;
import com.socialNetwork.entities.user.User;
import com.socialNetwork.repositories.UserRepository;
import com.socialNetwork.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username).orElseThrow(() -> {
            logger.warn("User with this login does not exist");
            return new UsernameNotFoundException("User with this login does not exist");
        });
        return CustomUserDetails.fromUserEntityToCustomUserDetails(user);
    }
}
