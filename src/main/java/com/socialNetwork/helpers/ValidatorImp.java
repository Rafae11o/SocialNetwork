package com.socialNetwork.helpers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

public class ValidatorImp {
    private final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");

    private final Pattern CONTAINS_ONLY_CHARACTERS =
            Pattern.compile("^[a-zA-Z]{2,}$");

    private final Boolean devMode;

    public ValidatorImp(Boolean devMode) {
        this.devMode = devMode;
    }

    public boolean isPasswordValid(String password){
        return devMode || VALID_PASSWORD_REGEX.matcher(password).find();
    }

    public boolean containsOnlyLetters(String word){
        return devMode || CONTAINS_ONLY_CHARACTERS.matcher(word).find();
    }
}
