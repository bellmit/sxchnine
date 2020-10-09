package com.project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Card {

    private String type;
    private String number;
    private int expMonth;
    private int expYear;
    private int cvc;
    private String paymentIntentId;
    private String paymentMethodId;
}
