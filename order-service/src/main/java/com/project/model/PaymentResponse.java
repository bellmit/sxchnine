package com.project.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentResponse {

    private String paymentMethodId;
    private String paymentIntentId;
    private String paymentClientSecret;
    private String orderId;
    private boolean actionRequired;
    private String nextAction;
    private String status;
    private ErrorReason errorReason;
}