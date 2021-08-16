package learn.field_agent.data;

import learn.field_agent.models.Agency;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SecurityClearanceJdbcTemplateRepositoryTest {

    @Autowired
    SecurityClearanceJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAllSecurityClearances() {
        List<SecurityClearance> securityClearanceList = repository.findAll();
        assertNotNull(securityClearanceList);
        assertTrue(securityClearanceList.size() > 0);
        System.out.println(securityClearanceList.size());
    }

    @Test
    void shouldFindById() {
        SecurityClearance secret = new SecurityClearance(1, "Secret");
        SecurityClearance topSecret = new SecurityClearance(2, "Top Secret");

        SecurityClearance actual = repository.findById(1);
        assertEquals(secret, actual);

        actual = repository.findById(2);
        assertEquals(topSecret, actual);

        actual = repository.findById(3);
        assertEquals(null, actual);
    }

    @Test
    void shouldAddSecurityClearance() {
        SecurityClearance securityClearance = new SecurityClearance();
        securityClearance.setName("Adding Clearance");
        SecurityClearance actual = repository.add(securityClearance);

        assertNotNull(actual);
        assertEquals(3, actual.getSecurityClearanceId());
    }

    @Test
    void shouldUpdate() {
        SecurityClearance securityClearance = makeSecurityClearance();
        securityClearance.setSecurityClearanceId(1);
        assertTrue(repository.update(securityClearance));
        securityClearance.setSecurityClearanceId(13);
        assertFalse(repository.update(securityClearance));
    }

    @Test
    void shouldDelete() {
        boolean result = repository.deleteById(2);
        assertTrue(result);

        SecurityClearance find = repository.findById(2);
        assertEquals(null, find);
    }

    @Test
    void shouldNotDeleteIfExistInAgentAgency(){
        boolean result = repository.deleteById(1);
        assertFalse(result);
    }

    SecurityClearance makeSecurityClearance() {
        SecurityClearance securityClearance = new SecurityClearance();
        securityClearance.setName("Test");
        return securityClearance;
    }
}