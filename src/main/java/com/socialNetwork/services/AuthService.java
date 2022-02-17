package com.socialNetwork.services;

import com.socialNetwork.dto.RegistrationInfo;
import com.socialNetwork.entities.user.User;
import com.socialNetwork.exceptions.UserFriendlyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;

    @Autowired
    public AuthService(UserService userService){
        this.userService = userService;
    }

    /**
     *
     * @param userInfo - dto with name, login, password
     * @throws UserFriendlyException
     */
    public void createUser(RegistrationInfo userInfo) throws UserFriendlyException {
        userService.create(userInfo);
    }

    /**
     *
     * @param login user login
     * @param password user password
     */
    public void login(String login, String password) throws UserFriendlyException {
        User user = userService.getUserByLogin(login).orElseThrow(
                () -> new UserFriendlyException("User with this login does not exist!"));
        if(!userService.checkPassword(password, user)) {
            throw new UserFriendlyException("Wrong password!");
        }
    }
}
