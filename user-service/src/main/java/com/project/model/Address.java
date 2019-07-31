package com.project.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Address implements Serializable {

    private String address;
    private String postalCode;
    private String city;
    private String province;
    private String country;
}
