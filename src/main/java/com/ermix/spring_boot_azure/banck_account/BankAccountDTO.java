package com.ermix.spring_boot_azure.banck_account;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class BankAccountDTO {
    private String type;
}
