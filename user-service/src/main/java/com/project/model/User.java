package com.project.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("user")
public class User implements Serializable {

    @Id
    private String id;
    @Indexed
    private Gender gender;
    @Indexed
    private String firstName;
    @Indexed
    private String lastName;
    @Indexed
    private String email;
    @Indexed
    private String password;
    @Indexed
    private String phoneNumber;
    @Indexed
    private Address address;
}
