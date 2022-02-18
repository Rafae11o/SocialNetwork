package com.socialNetwork.entities.user;

import com.socialNetwork.dto.RegistrationInfo;
import com.socialNetwork.entities.BaseEntity;
import com.socialNetwork.entities.post.Post;
import com.socialNetwork.exceptions.UserFriendlyException;
import com.socialNetwork.helpers.Converter;
import com.socialNetwork.helpers.Validator;
import com.socialNetwork.security.PasswordEncoder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "login")
})
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User extends BaseEntity {

    @Column(name = "login", unique = true, length = 50, nullable = false)
    private String login;

    @Column(name = "name", length = 60, nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<Post> postList;

    public User(RegistrationInfo userInfo) throws UserFriendlyException {
        setLogin(userInfo.getLogin());
        setPassword(userInfo.getPassword());
        setName(userInfo.getName());
        setRole(UserRole.USER);
    }

    public void setLogin(String login){
        this.login = login.trim();
    }

    public void setName(String name) throws UserFriendlyException {
        String[] namePartsToToCheck = name.trim().split(" ");
        StringBuilder nameInCorrectFormat = new StringBuilder("");
        for(int i = 0; i<namePartsToToCheck.length; i++){
            if(!Validator.getValidator().containsOnlyLetters(namePartsToToCheck[i])){
                throw new UserFriendlyException("Name have to contain only letters");
            }
            nameInCorrectFormat.append(Converter.adjustCaseForName(namePartsToToCheck[i]));
            if(i < namePartsToToCheck.length - 1){
                nameInCorrectFormat.append(" ");
            }
        }
        this.name = nameInCorrectFormat.toString();
    }

    public void setPassword(String password) throws UserFriendlyException {
        if(!Validator.getValidator().isPasswordValid(password)){
            throw new UserFriendlyException("Password does not meet requirements");
        }
        this.password = PasswordEncoder.bCryptPasswordEncoder().encode(password.trim());
    }
}
