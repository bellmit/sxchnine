package com.project.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentInfo {

    private String type;
    private String paymentIntentId;
    private String noCreditCard;
    private String expDate;
    private Integer securityCode;
    private String lastName;
    private String firstName;
}
