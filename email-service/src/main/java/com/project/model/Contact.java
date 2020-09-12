package com.project.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Contact {

    private String fullName;
    private String email;
    private String phoneNumber;
    private String message;
}
