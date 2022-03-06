package com.socialNetwork.dto.user;

import com.socialNetwork.entities.user.User;
import lombok.Data;

@Data
public class UserInfo {
    private Long id;
    private String name;
    private String login;

    public UserInfo(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.login = user.getLogin();
    }

}
