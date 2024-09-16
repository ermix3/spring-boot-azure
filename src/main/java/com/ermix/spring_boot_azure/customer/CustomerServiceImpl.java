package com.ermix.spring_boot_azure.customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public List<CustomerDTO> listCustomers() {
        log.info("List Customers");
        List<Customer> customers = customerRepository.findAll();
        if (customers.isEmpty()) {
            log.warn("No customers found in the database.");
            return List.of();
        }

        return customers.stream()
                .map(CustomerDTO::fromCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        log.info("Search Customer");
        List<Customer> customers = customerRepository.searchCustomer(keyword);
        if (customers.isEmpty()) {
            log.warn("No customers found in the database.");
            return List.of();
        }
        return customers.stream()
                .map(CustomerDTO::fromCustomer)
                .toList();
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        Customer customer = Customer.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerDTO.fromCustomer(savedCustomer);
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        log.info("Get Customer");
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer Not found"));
        return CustomerDTO.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Update Customer");
        Customer customer = Customer.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerDTO.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        log.info("Delete Customer");
        customerRepository.deleteById(customerId);
    }
}
