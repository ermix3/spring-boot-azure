package com.ermix.spring_boot_azure.customer;

import lombok.Builder;

@Builder
public record CustomerDTO(
        Long id,
        String name,
        String email
) {
    public static CustomerDTO fromCustomer(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail()
        );
    }
}
