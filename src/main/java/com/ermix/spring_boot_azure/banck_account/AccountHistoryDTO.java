package com.ermix.spring_boot_azure.banck_account;

import lombok.Builder;

import java.util.List;

@Builder
public record AccountHistoryDTO(
        String accountId,
        double balance,
        int currentPage,
        int totalPages,
        int pageSize,
        List<AccountOperationDTO> accountOperationDTOS
) {
}
