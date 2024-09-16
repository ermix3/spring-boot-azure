package com.ermix.spring_boot_azure.banck_account;

import lombok.Builder;

import java.util.Date;

@Builder
public record AccountOperationDTO(
        Long id,
        Date operationDate,
        double amount,
        OperationType type,
        String description
) {
}
