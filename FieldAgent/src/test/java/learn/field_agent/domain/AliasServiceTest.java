package learn.field_agent.domain;

import learn.field_agent.data.AliasRepository;
import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.Alias;
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
class AliasServiceTest {
    @Autowired
    AliasService service;

    @MockBean
    AliasRepository repository;

    @Test
    void add() {
        Alias alias = new Alias();
        alias.setPersona("N");
        alias.setName("New");
        alias.setAgentId(1);

        Alias mockOut = new Alias();
        alias.setPersona("A");
        alias.setName("Ant");
        alias.setAgentId(2);

        when(repository.add(alias)).thenReturn(mockOut);
        Result<Alias> actual = service.add(alias);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void nameRequired() {
        Alias alias = new Alias();
        alias.setPersona("N");
        alias.setAgentId(1);

        Result<Alias> actual = service.add(alias);
        assertEquals(ResultType.INVALID, actual.getType());
    }
    //Persona is not required unless a name is duplicated. The persona differentiates between duplicate names.

    @Test
    void validatePersonaNotRequired() {
        Alias alias = new Alias();
        alias.setName("New");
        alias.setAgentId(1);

        Result<Alias> actual = service.add(alias);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void validatePersonaDuplicatedName() {
        Alias alias = new Alias();
        alias.setName("Sammi");
        alias.setPersona("S");
        alias.setAgentId(1);

        Alias alias2 = new Alias();
        alias.setName("Sammi");
        alias.setAgentId(2);


        List<Alias> mockOut = new ArrayList<>();
        mockOut.add(alias);
        mockOut.add(alias2);

        when(repository.findAll()).thenReturn(mockOut);
        Result<Alias> actual = service.add(alias);
        assertEquals(ResultType.INVALID, actual.getType());
    }
}