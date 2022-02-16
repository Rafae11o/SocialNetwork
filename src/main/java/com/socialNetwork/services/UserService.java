package com.socialNetwork.services;

import com.socialNetwork.dto.UserInfo;
import com.socialNetwork.entities.user.User;
import com.socialNetwork.exceptions.UserFriendlyException;
import com.socialNetwork.repositories.UserRepository;
import com.socialNetwork.security.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public boolean checkPassword(String password, User user){
        return PasswordEncoder.bCryptPasswordEncoder().matches(password, user.getPassword());
    }

    public void create(UserInfo userInfo) throws UserFriendlyException {
        User user = new User(userInfo);
        userRepository.save(user);
    }

}
