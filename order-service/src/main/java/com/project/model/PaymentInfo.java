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
    private String securityCode;
    private String fullName;
    private String postalCode;
}
