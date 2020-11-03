package fact.it.customer.controller;

import fact.it.customer.model.Customer;
import fact.it.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;

    @PostConstruct
    private void fillDatabase() {
        if (customerRepository.count() == 0){
            customerRepository.save(new Customer("H01C01", "Jeff", "Jeffen", "Antwerpen", "Straat1", 1));
            customerRepository.save(new Customer("H02C02", "Jos", "Jossen", "Gent", "Straat2", 10));
        }

        System.out.println("Number of customers: " + customerRepository.count());
    }

    @GetMapping("/customers")
    public List<Customer> getCustomers(){
        return customerRepository.findAll();
    }

    @GetMapping("/customers/{code}")
    public List<Customer> getCustomer(@PathVariable String code){
        return customerRepository.findAllByRoomCode(code);
    }

    @PostMapping("/customers/new")
    public Customer addCustomer(@RequestBody Customer newCustomer){
        customerRepository.save(newCustomer);
        return newCustomer;
    }

    @PutMapping("/customers/{firstName}/{lastName}")
    public Customer replaceCustomer(@RequestBody Customer updateCustomer, @PathVariable String firstName, @PathVariable String lastName){
        Customer customer = customerRepository.findAllByFirstNameAndLastName(firstName, lastName);

        customer.setFirstName(updateCustomer.getFirstName());
        customer.setLastName(updateCustomer.getLastName());
        customer.setRoomCode(updateCustomer.getRoomCode());
        customer.setCity(updateCustomer.getCity());
        customer.setStreet(updateCustomer.getStreet());
        customer.setNumber(updateCustomer.getNumber());

        return customer;
    }

    @DeleteMapping("/customers/{firstName}/{lastName}")
    public void deleteCustomer(@PathVariable String firstName, @PathVariable String lastName){
        Customer customer = customerRepository.findAllByFirstNameAndLastName(firstName, lastName);
        customerRepository.delete(customer);
    }


}
