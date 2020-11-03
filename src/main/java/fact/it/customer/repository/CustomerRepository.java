package fact.it.customer.repository;

import fact.it.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAllByRoomCode(String code);
    Customer findAllByFirstNameAndLastName(String firstName, String lastName);
}
