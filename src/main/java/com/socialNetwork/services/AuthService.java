package com.socialNetwork.services;

import com.socialNetwork.controllers.UserController;
import com.socialNetwork.dto.RegistrationInfo;
import com.socialNetwork.entities.user.User;
import com.socialNetwork.exceptions.UserFriendlyException;
import com.socialNetwork.repositories.UserRepository;
import com.socialNetwork.security.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /**
     *
     * @param userInfo - dto with name, login, password
     * @throws UserFriendlyException
     */
    public void createUser(RegistrationInfo userInfo) throws UserFriendlyException {
        if(userRepository.existsByLogin(userInfo.getLogin())){
            throw new UserFriendlyException("User with this login already exist");
        }
        User user = new User(userInfo);
        userRepository.save(user);
    }

    /**
     *
     * @param login user login
     * @param password user password
     */
    public void login(String login, String password) throws UserFriendlyException {
        User user = userRepository.findByLogin(login).orElseThrow(
                () -> new UserFriendlyException("User with this login does not exist!"));
        if(!PasswordEncoder.bCryptPasswordEncoder().matches(password, user.getPassword())) {
            throw new UserFriendlyException("Wrong password!");
        }
    }
}
