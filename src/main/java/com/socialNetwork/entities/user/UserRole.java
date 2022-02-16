package com.socialNetwork.entities.user;


public enum UserRole {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRoleName() {
        return role;
    }
}
