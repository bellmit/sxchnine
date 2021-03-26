package com.project.model;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class OrderIdKey {

    private String orderId;
    private String userEmail;
    private LocalDateTime orderTime;
}
