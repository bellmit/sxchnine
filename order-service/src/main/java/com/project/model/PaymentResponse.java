package com.project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    private String paymentMethodId;
    private String paymentIntentId;
    private String paymentClientSecret;
    private boolean actionRequired;
    private String nextAction;
    private String status;
    private ErrorReason errorReason;
}