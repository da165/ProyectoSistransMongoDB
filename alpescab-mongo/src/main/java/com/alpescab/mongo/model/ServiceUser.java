package com.alpescab.mongo.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "service_users")
public class ServiceUser {
    @Id
    private String id;
    private String name;
    private String email;
    private String phone;
    private String nationalId;
    private List<PaymentMethod> paymentMethods;
}
