package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {

    private String firstName;
    private String lastName;
    private String address;
    private String postalCode;
    private String city;
    private String province;
    private String country;
}
