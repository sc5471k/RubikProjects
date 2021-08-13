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
        securityClearance.setSecurityClearanceId(3);
        assertTrue(repository.update(securityClearance));
        securityClearance.setSecurityClearanceId(13);
        assertFalse(repository.update(securityClearance));
    }

    @Test
    void shouldDelete() throws SQLException {
//        delete FROM field_agent_test.security_clearance where security_clearance_id = 3;
//        alter table security_clearance auto_increment = 0;

//        SecurityClearance securityClearance = new SecurityClearance();
//        securityClearance.setName("Testing Clearance");
//        SecurityClearance actual = repository.add(securityClearance);
//
//        assertNotNull(actual);
//        assertEquals(3, actual.getSecurityClearanceId());

        boolean result = repository.deleteById(3);
        assertTrue(result);

        SecurityClearance find = repository.findById(3);
        assertEquals(null, find);
    }

    @Test
    void shouldNotDeleteIfExistInAgentAgency() throws SQLException {
        SecurityClearance secret = new SecurityClearance(1, "Secret");
        boolean result = repository.deleteById(1);
        assertFalse(result);

        SecurityClearance find = repository.findById(1);
        assertEquals(secret, find);
    }

    SecurityClearance makeSecurityClearance() {
        SecurityClearance securityClearance = new SecurityClearance();
        securityClearance.setName("Test");
        return securityClearance;
    }
}