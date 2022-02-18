package com.socialNetwork.helpers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Validator {

    private static Boolean devMode = false;

    public Validator(@Value("${dev-mode.enable}") Boolean devMode){
        Validator.devMode = devMode;
    }

    @Bean
    public static ValidatorImp getValidator() {
        return new ValidatorImp(devMode);
    }
}
