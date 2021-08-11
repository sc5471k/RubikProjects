package learn.field_agent.domain;

import learn.field_agent.data.AgencyRepository;
import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.Agency;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SecurityServiceTest {

    @Autowired
    SecurityService service;

    @MockBean
    SecurityClearanceRepository repository;

    @Test
    void findAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void shouldAdd() {
        SecurityClearance securityClearance = new SecurityClearance(0, "NameTest");
        SecurityClearance mockOut = new SecurityClearance(5, "NameTest");

        when(repository.add(securityClearance)).thenReturn(mockOut);
        Result<SecurityClearance> actual = service.add(securityClearance);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void clearanceNameRequired() {
        SecurityClearance securityClearance = new SecurityClearance(35, null);

        Result<SecurityClearance> actual = service.add(securityClearance);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    @Test
    void nameNotDuplicated() {
//        SecurityClearance securityClearance = new SecurityClearance();
//        securityClearance.setName("New");
//        service.add(securityClearance);

        SecurityClearance duplicate = new SecurityClearance();
        duplicate.setName("Secret");
        Result<SecurityClearance> actual = service.add(duplicate);
        assertEquals(ResultType.INVALID, actual.getType());
    }
}