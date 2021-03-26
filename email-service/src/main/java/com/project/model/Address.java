package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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
