package com.project.model;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@UserDefinedType("payment_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentInfo {

    @Column("no_credit_card")
    private String noCreditCard;

    @Column("exp_date")
    private String expDate;

    @Column("security_code")
    private int securityCode;

    @Column("last_name")
    private String lastName;

    @Column("first_name")
    private String firstName;
}
