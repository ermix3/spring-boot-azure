package com.ermix.spring_boot_azure.banck_account;

import com.ermix.spring_boot_azure.customer.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@DiscriminatorColumn(name = "TYPE", length = 4)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class BankAccount {
    @Id
    private String id;

    private double balance;
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "bankAccount")
    private List<AccountOperation> accountOperations;
}