package com.project.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Address implements Serializable {

    private String firstName;
    private String lastName;
    private String address;
    private String postalCode;
    private String city;
    private String province;
    private String country;
}
