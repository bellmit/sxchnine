package com.project.model;

import lombok.*;

@Data
public class PaymentInfo {

    private String noCreditCard;

    private String expDate;

    private int securityCode;

    private String lastName;

    private String firstName;

}
