package com.socialNetwork.config;

import com.socialNetwork.security.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter){
        this.jwtFilter = jwtFilter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/user/getFeedForUnauthorizedUser").permitAll() // For unauthorized user to see posts
                .antMatchers("/api/auth/*").permitAll()
                .antMatchers("/ws").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
