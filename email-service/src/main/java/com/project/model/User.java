package com.project.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {

    private String id;
    private String gender;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String plainPassword;
    private String phoneNumber;
    private Address address;
    private String role;
    private boolean forgotPassword;
}
