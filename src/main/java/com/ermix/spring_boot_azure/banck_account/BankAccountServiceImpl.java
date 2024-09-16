package com.ermix.spring_boot_azure.banck_account;

import com.ermix.spring_boot_azure.customer.Customer;
import com.ermix.spring_boot_azure.customer.CustomerNotFoundException;
import com.ermix.spring_boot_azure.customer.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
@AllArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {
    private BankAccountMapperImpl dtoMapper;
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);
        return dtoMapper.fromCurrentBankAccount(savedBankAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);
        return dtoMapper.fromSavingBankAccount(savedBankAccount);
    }


    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        if (bankAccount instanceof SavingAccount savingAccount) {
            return dtoMapper.fromSavingBankAccount(savingAccount);
        }

        if (bankAccount instanceof CurrentAccount currentAccount) {
            return dtoMapper.fromCurrentBankAccount(currentAccount);
        }

        throw new IllegalArgumentException("Unknown account type: " + bankAccount.getClass());
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        if (bankAccount.getBalance() < amount)
            throw new BalanceNotSufficientException("Balance not sufficient");
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource, amount, "Transfer to " + accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from " + accountIdSource);
    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        if (bankAccounts.isEmpty()) {
            log.warn("No bank account found");
            return List.of();
        }
        return bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount savingAccount) {
                return dtoMapper.fromSavingBankAccount(savingAccount);
            }

            if (bankAccount instanceof CurrentAccount currentAccount) {
                return dtoMapper.fromCurrentBankAccount(currentAccount);
            }

            throw new IllegalArgumentException("Unknown account type: " + bankAccount.getClass());
        }).toList();
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId) {
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        if (accountOperations.isEmpty()) {
            log.warn("No account operation found for account {}", accountId);
            return List.of();
        }
        return accountOperations
                .stream()
                .map(dtoMapper::fromAccountOperation)
                .collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount == null) throw new BankAccountNotFoundException("Account not Found");
        Page<AccountOperation> accountOperations = accountOperationRepository
                .findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));
        var accountHistoryDTO = AccountHistoryDTO.builder();
        List<AccountOperation> accountOperationsContent = accountOperations.getContent();
        if (accountOperationsContent.isEmpty()) {
            log.warn("No account operation found for account {}", accountId);
            return accountHistoryDTO.build();
        }
        List<AccountOperationDTO> accountOperationDTOS = accountOperationsContent
                .stream()
                .map(dtoMapper::fromAccountOperation)
                .collect(Collectors.toList());
        accountHistoryDTO.accountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.accountId(bankAccount.getId());
        accountHistoryDTO.balance(bankAccount.getBalance());
        accountHistoryDTO.currentPage(page);
        accountHistoryDTO.pageSize(size);
        accountHistoryDTO.totalPages(accountOperations.getTotalPages());
        return accountHistoryDTO.build();
    }
}