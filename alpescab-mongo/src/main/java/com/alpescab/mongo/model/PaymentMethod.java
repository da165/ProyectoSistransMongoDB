package com.alpescab.mongo.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethod {
    private String cardNumber;
    private String cardHolderName;
    private String expiration;
    private String cvv;
}
