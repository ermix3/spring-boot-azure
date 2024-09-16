package com.ermix.spring_boot_azure.customer;

import java.util.List;

public interface CustomerService {

    List<CustomerDTO> listCustomers();

    List<CustomerDTO> searchCustomers(String keyword);

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);
}
