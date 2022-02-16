package com.socialNetwork.security;

import com.socialNetwork.entities.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private Long id;
    private String login;
    private String password;
    private Collection<? extends GrantedAuthority> grantedAuthorities;

    public static CustomUserDetails fromUserEntityToCustomUserDetails(User user){
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.id = user.getId();
        customUserDetails.login = user.getLogin();
        customUserDetails.password = user.getPassword();
        customUserDetails.grantedAuthorities = Collections.singleton(new SimpleGrantedAuthority(user.getRole().getRoleName()));
        return customUserDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true ;
    }

    public Long getId() {
        return id;
    }
}
