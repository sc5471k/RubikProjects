package learn.field_agent.data;

import learn.field_agent.models.Agent;
import learn.field_agent.models.Alias;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AliasJdbcTemplateRepositoryTest {
    @Autowired
    AliasJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodStates;

    @BeforeEach
    void setup() {
        knownGoodStates.set();
    }

    @Test
    void shouldFindAll() {
        List<Alias> aliasList = repository.findAll();
        assertNotNull(aliasList);
        assertTrue(aliasList.size() > 0);
        System.out.println(aliasList.size());
    }

    @Test
    void shouldFindById() {
        Alias alias = makeAlias();
        repository.add(alias);

        Alias find = repository.findById(1);
        assertNotNull(find);
    }

    @Test
    void findAgentByAlias() {
    }

    @Test
    void shouldAdd() {
        Alias alias = makeAlias();
        Alias actual = repository.add(alias);

        assertNotNull(actual);
        assertEquals(1, actual.getAliasID());
    }

    @Test
    void shouldUpdate() {
        Alias alias = makeAlias();
        alias.setAliasID(1);
        alias.setName("New Alias");
        assertTrue(repository.update(alias));

        alias.setAliasID(5);
        assertFalse(repository.update(alias));
    }

    @Test
    void shouldDeleteByID() {
        Alias alias = makeAlias();
        repository.add(alias);

        boolean result = repository.deleteById(2);
        assertTrue(result);
    }

    Alias makeAlias() {
        Alias alias = new Alias();
        alias.setName("New");
        alias.setPersona("N");
        alias.setAgentId(1);
        return alias;
    }
}