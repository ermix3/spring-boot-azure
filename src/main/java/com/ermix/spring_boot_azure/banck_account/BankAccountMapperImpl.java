package com.ermix.spring_boot_azure.banck_account;

import com.ermix.spring_boot_azure.customer.Customer;
import com.ermix.spring_boot_azure.customer.CustomerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankAccountMapperImpl {

    public SavingBankAccountDTO fromSavingBankAccount(SavingAccount savingAccount) {
        var savingBankAccountDTO = SavingBankAccountDTO.builder();
        BeanUtils.copyProperties(savingAccount, savingBankAccountDTO);
        savingBankAccountDTO.customerDTO(CustomerDTO.fromCustomer(savingAccount.getCustomer()));
        savingBankAccountDTO.type(savingAccount.getClass().getSimpleName());
        return savingBankAccountDTO.build();
    }

    public SavingAccount fromSavingBankAccountDTO(SavingBankAccountDTO savingBankAccountDTO) {
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(savingBankAccountDTO, savingAccount);
        savingAccount.setCustomer(Customer.fromCustomerDTO(savingBankAccountDTO.getCustomerDTO()));
        return savingAccount;
    }

    public CurrentBankAccountDTO fromCurrentBankAccount(CurrentAccount currentAccount) {
        var currentBankAccountDTO = CurrentBankAccountDTO.builder();
        BeanUtils.copyProperties(currentAccount, currentBankAccountDTO);
        currentBankAccountDTO.customerDTO(CustomerDTO.fromCustomer(currentAccount.getCustomer()));
        currentBankAccountDTO.type(currentAccount.getClass().getSimpleName());
        return currentBankAccountDTO.build();
    }

    public CurrentAccount fromCurrentBankAccountDTO(CurrentBankAccountDTO currentBankAccountDTO) {
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentBankAccountDTO, currentAccount);
        currentAccount.setCustomer(Customer.fromCustomerDTO(currentBankAccountDTO.getCustomerDTO()));
        return currentAccount;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation) {
        var accountOperationDTO = AccountOperationDTO.builder();
        BeanUtils.copyProperties(accountOperation, accountOperationDTO);
        return accountOperationDTO.build();
    }
}
