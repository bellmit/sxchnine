package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentInfo {

    private int noCreditCard;
    private String expDate;
    private int securityCode;
    private String lastName;
    private String firstName;
}
