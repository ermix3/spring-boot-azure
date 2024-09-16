package com.ermix.spring_boot_azure.banck_account;

import lombok.Builder;

@Builder
public record TransferRequestDTO(
        String accountSource,
        String accountDestination,
        double amount,
        String description
) {
}
