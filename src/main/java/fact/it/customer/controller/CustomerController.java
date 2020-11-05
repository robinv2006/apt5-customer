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
        if (customerRepository.count() == 0) {
            customerRepository.save(new Customer("C01","R01", "Jeff", "Jeffen", "Antwerpen", "Straat1", 1));
            customerRepository.save(new Customer("C02","R02", "Jos", "Jossen", "Gent", "Straat2", 10));
        }

        System.out.println("Number of customers: " + customerRepository.count());
    }

    @GetMapping("/customers")
    public List<Customer> getCustomers(){
        return customerRepository.findAll();
    }

    /*@GetMapping("/customers/{code}")
    public List<Customer> getCustomerByRoom(@PathVariable String code){
        return customerRepository.findAllByRoomCode(code);
    }*/

    @GetMapping("/customers/{customerCode}")
    public Customer getCustomerByName(@PathVariable String customerCode){
        return customerRepository.findCustomerByCustomerCode(customerCode);
    }

    @GetMapping("/customers/rooms/{roomCode}")
    public List<Customer> getCustomerByRoomCode(@PathVariable String roomCode){
        return customerRepository.findCustomerByRoomCode(roomCode);
    }

    @PostMapping("/customers/new")
    public Customer addCustomer(@RequestBody Customer newCustomer){
        customerRepository.save(newCustomer);
        return newCustomer;
    }

    @PutMapping("/customers/{customerCode}")
    public Customer replaceCustomer(@RequestBody Customer updateCustomer, @PathVariable String customerCode){
        Customer customer = customerRepository.findCustomerByCustomerCode(customerCode);

        customer.setFirstName(updateCustomer.getFirstName());
        customer.setLastName(updateCustomer.getLastName());
        customer.setRoomCode(updateCustomer.getRoomCode());
        customer.setCity(updateCustomer.getCity());
        customer.setStreet(updateCustomer.getStreet());
        customer.setNumber(updateCustomer.getNumber());

        customerRepository.save(customer);

        return customer;
    }

    @DeleteMapping("/customers/{customerCode}")
    public void deleteCustomer(@PathVariable String customerCode){
        Customer customer = customerRepository.findCustomerByCustomerCode(customerCode);
        customerRepository.delete(customer);
    }


}
