package com.project.model;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.io.Serializable;

@UserDefinedType("address")
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Address implements Serializable {

    private String address;
    private String postalCode;
    private String city;
    private String province;
    private String country;
}
