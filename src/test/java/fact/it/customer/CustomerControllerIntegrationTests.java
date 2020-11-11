package fact.it.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import fact.it.customer.model.Customer;
import fact.it.customer.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer1 = new Customer("C01", "R01", "Jeff", "Jeffen", "Antwerpen", "Straat1", 1);
    private Customer customer2 = new Customer("C02", "R02", "Jos", "Jossen", "Gent", "Straat2", 10);

    @BeforeEach
    public void beforeAllTests() {
        customerRepository.deleteAll();
        customerRepository.save(customer1);
        customerRepository.save(customer2);
    }

    @AfterEach
    public void afterAllTests() {
        //Watch out with deleteAll() methods when you have other data in the test database!
        customerRepository.deleteAll();
    }

    private ObjectMapper mapper = new ObjectMapper();

    //test
    @Test
    public void whenGetCustomers_thenReturnJsonReview() throws Exception {
        mockMvc.perform(get("/customers"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].customerCode", is("C01")))
                .andExpect(jsonPath("$[0].roomCode", is("R01")))
                .andExpect(jsonPath("$[0].firstName", is("Jeff")))
                .andExpect(jsonPath("$[0].lastName", is("Jeffen")))
                .andExpect(jsonPath("$[0].city", is("Antwerpen")))
                .andExpect(jsonPath("$[0].street", is("Straat1")))
                .andExpect(jsonPath("$[0].number", is(1)))
                .andExpect(jsonPath("$[1].customerCode", is("C02")))
                .andExpect(jsonPath("$[1].roomCode", is("R02")))
                .andExpect(jsonPath("$[1].firstName", is("Jos")))
                .andExpect(jsonPath("$[1].lastName", is("Jossen")))
                .andExpect(jsonPath("$[1].city", is("Gent")))
                .andExpect(jsonPath("$[1].street", is("Straat2")))
                .andExpect(jsonPath("$[1].number", is(10)));
    }

    @Test
    public void givenCustomer_whenGetCustomerByCustomerCode_thenReturnJsonReview() throws Exception {

        mockMvc.perform(get("/customers/{customerCode}", "C01"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerCode", is("C01")))
                .andExpect(jsonPath("$.roomCode", is("R01")))
                .andExpect(jsonPath("$.firstName", is("Jeff")))
                .andExpect(jsonPath("$.lastName", is("Jeffen")))
                .andExpect(jsonPath("$.city", is("Antwerpen")))
                .andExpect(jsonPath("$.street", is("Straat1")))
                .andExpect(jsonPath("$.number", is(1)));
    }

    @Test
    public void givenCustomer_whenGetCustomerByRoomCode_thenReturnJsonReview() throws Exception {
        mockMvc.perform(get("/customers/rooms/{roomCode}", "R01"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].customerCode", is("C01")))
                .andExpect(jsonPath("$[0].roomCode", is("R01")))
                .andExpect(jsonPath("$[0].firstName", is("Jeff")))
                .andExpect(jsonPath("$[0].lastName", is("Jeffen")))
                .andExpect(jsonPath("$[0].city", is("Antwerpen")))
                .andExpect(jsonPath("$[0].street", is("Straat1")))
                .andExpect(jsonPath("$[0].number", is(1)));
    }

    @Test
    public void whenPostCustomer_thenReturnJsonReview() throws Exception {
        Customer customer3 = new Customer("C03", "R03", "Jan", "Janssens", "Hasselt", "Straat3", 15);

        mockMvc.perform(post("/customers/new")
                .content(mapper.writeValueAsString(customer3))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerCode", is("C03")))
                .andExpect(jsonPath("$.roomCode", is("R03")))
                .andExpect(jsonPath("$.firstName", is("Jan")))
                .andExpect(jsonPath("$.lastName", is("Janssens")))
                .andExpect(jsonPath("$.city", is("Hasselt")))
                .andExpect(jsonPath("$.street", is("Straat3")))
                .andExpect(jsonPath("$.number", is(15)));
    }

    @Test
    public void givenCustomer_whenPutCustomer_thenReturnJsonReview() throws Exception {

        Customer updatedCustomer = new Customer("C01", "R01", "Jeff!!!!!", "Jeffen", "Antwerpen", "Straat1", 1);

        mockMvc.perform(put("/customers")
                .content(mapper.writeValueAsString(updatedCustomer))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerCode", is("C01")))
                .andExpect(jsonPath("$.roomCode", is("R01")))
                .andExpect(jsonPath("$.firstName", is("Jeff!!!!!")))
                .andExpect(jsonPath("$.lastName", is("Jeffen")))
                .andExpect(jsonPath("$.city", is("Antwerpen")))
                .andExpect(jsonPath("$.street", is("Straat1")))
                .andExpect(jsonPath("$.number", is(1)));
    }

    @Test
    public void givenCustomer_whenDeleteCustomer_thenStatusOk() throws Exception {
        mockMvc.perform(delete("/customers/{customerCode}", "C02")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoCustomer_whenDeleteCustomer_thenStatusNotFound() throws Exception {
        mockMvc.perform(delete("/customers/{customerCode}", "C04")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
