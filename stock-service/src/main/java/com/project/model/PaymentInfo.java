package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentInfo {

    private String noCreditCard;

    private String expDate;

    private int securityCode;

    private String lastName;

    private String firstName;

}
