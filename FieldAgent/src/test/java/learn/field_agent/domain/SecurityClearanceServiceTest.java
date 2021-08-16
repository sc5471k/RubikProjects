package learn.field_agent.domain;

import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SecurityClearanceServiceTest {

    @Autowired
    SecurityClearanceService service;

    @MockBean
    SecurityClearanceRepository repository;

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

    @Test
    void nameNotDuplicated() {
        SecurityClearance one = new SecurityClearance(5, "Secret");
        SecurityClearance two = new SecurityClearance(6, "Test");

        List<SecurityClearance> mockOut = new ArrayList<>();
        mockOut.add(one);
        mockOut.add(two);

        SecurityClearance duplicate = new SecurityClearance();
        duplicate.setName("Secret");

        when(repository.findAll()).thenReturn(mockOut);
        Result<SecurityClearance> actual = service.add(duplicate);
        assertEquals(ResultType.INVALID, actual.getType());
    }
}