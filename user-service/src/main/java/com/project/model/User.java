package com.project.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User implements Serializable {

    private static final long serialVersionUID = -1;

    private String id;
    private Gender gender;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private Address address;
    private String role;
}
