package com.ermix.spring_boot_azure.banck_account;

import lombok.Builder;

@Builder
public record DebitDTO(
        String accountId,
        double amount,
        String description
) {
}
