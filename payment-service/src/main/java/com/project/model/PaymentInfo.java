package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentInfo {

    private String type;
    private String paymentIntentId;
    private String noCreditCard;
    private String expDate;
    private String securityCode;
    private String fullName;
    private String postalCode;

}
