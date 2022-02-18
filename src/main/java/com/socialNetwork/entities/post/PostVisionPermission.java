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

    /**
     * @return all values of enumeration;
     */
    public static List<String> names() {
        PostVisionPermission[] permissions = values();
        List<String> names = new ArrayList<>();
        for (PostVisionPermission postVisionPermission : permissions) {
            names.add(postVisionPermission.name());
        }
        return names;
    }
}
