package com.ermix.spring_boot_azure.customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public List<CustomerDTO> customers() {
        return customerService.listCustomers();
    }

    @GetMapping("/search")
    public List<CustomerDTO> searchCustomers(@RequestParam(name = "keyword", defaultValue = "") String keyword) {
        return customerService.searchCustomers("%" + keyword + "%");
    }

    @GetMapping("/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
        return customerService.getCustomer(customerId);
    }

    @PostMapping
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return customerService.saveCustomer(customerDTO);
    }

    @PutMapping("/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable Long customerId, @RequestBody CustomerDTO customerDTO) {
//        customerDTO.id(customerId);
        return customerService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }
}