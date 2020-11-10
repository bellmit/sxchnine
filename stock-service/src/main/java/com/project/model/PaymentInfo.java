package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentInfo implements Serializable {

    private String type;
    private String paymentIntentId;
    private String noCreditCard;
    private String expDate;
    private int securityCode;
    private String lastName;
    private String firstName;

}
