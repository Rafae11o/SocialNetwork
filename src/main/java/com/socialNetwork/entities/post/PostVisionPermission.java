package com.socialNetwork.entities.post;

import java.util.ArrayList;
import java.util.List;

public enum PostVisionPermission {

    EVERYONE("EVERYONE"),
    AUTHORIZED_USERS("AUTHORIZED_USERS"),
    SUBSCRIBED_USERS("SUBSCRIBED_USERS");


    private final String permission;

    PostVisionPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    public static List<String> names() {
        PostVisionPermission[] permissions = values();
        List<String> names = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            names.add(permissions[i].name());
        }
        return names;
    }
}
