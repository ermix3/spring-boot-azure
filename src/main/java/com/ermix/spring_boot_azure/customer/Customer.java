package com.ermix.spring_boot_azure.customer;

import com.ermix.spring_boot_azure.banck_account.BankAccount;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    @OneToMany(mappedBy = "customer")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<BankAccount> bankAccounts;

    public static Customer fromCustomerDTO(CustomerDTO customerDTO) {
        return Customer.builder()
                .id(customerDTO.id())
                .name(customerDTO.name())
                .email(customerDTO.email())
                .build();
    }
}