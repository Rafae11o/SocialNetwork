package com.socialNetwork.entities;

import com.socialNetwork.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "subscriptions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subscription extends BaseEntity{

    /**
     * List of subscriptions
     */
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    /**
     * List of subscribers
     */
    @OneToOne
    @JoinColumn(name = "subscriber_id", referencedColumnName = "id", nullable = false)
    private User subscriber;

}
