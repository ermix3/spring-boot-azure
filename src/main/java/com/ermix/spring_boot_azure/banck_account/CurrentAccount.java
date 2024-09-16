package com.ermix.spring_boot_azure.banck_account;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("CA")
@EqualsAndHashCode(callSuper=false)
public class CurrentAccount extends BankAccount {
    private double overDraft;
}