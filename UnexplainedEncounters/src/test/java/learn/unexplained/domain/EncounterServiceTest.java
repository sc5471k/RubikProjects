package learn.unexplained.domain;

import learn.unexplained.data.DataAccessException;
import learn.unexplained.data.EncounterRepositoryDouble;
import learn.unexplained.models.Encounter;
import learn.unexplained.models.EncounterType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EncounterServiceTest {

    EncounterService service = new EncounterService(new EncounterRepositoryDouble());

    @Test
    void shouldNotAddNull() throws DataAccessException {
        EncounterResult expected = makeResult("encounter cannot be null");
        EncounterResult actual = service.add(null);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddEmptyWhen() throws DataAccessException {
        Encounter encounter = new Encounter(0, EncounterType.CREATURE, " ", "test desc", 1);
        EncounterResult expected = makeResult("when is required");
        EncounterResult actual = service.add(encounter);
        assertEquals(expected, actual);
    }


    @Test
    void shouldNotAddEmptyDescription() throws DataAccessException {
        Encounter encounter = new Encounter(0, EncounterType.CREATURE, "2/2/2019", "  ", 1);
        EncounterResult expected = makeResult("description is required");
        EncounterResult actual = service.add(encounter);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddNullDescription() throws DataAccessException {
        Encounter encounter = new Encounter(0, EncounterType.CREATURE, "2/2/2019", null, 1);
        EncounterResult expected = makeResult("description is required");
        EncounterResult actual = service.add(encounter);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddZeroOccurrences() throws DataAccessException {
        Encounter encounter = new Encounter(0, EncounterType.CREATURE, "2/2/2019", "test description", 0);
        EncounterResult expected = makeResult("occurrences must be greater than 0");
        EncounterResult actual = service.add(encounter);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddDuplicate() throws DataAccessException {
        Encounter encounter = new Encounter(0, EncounterType.CREATURE, "1/1/2015", "test description", 1);
        EncounterResult expected = makeResult("duplicate encounter is not allowed");
        EncounterResult actual = service.add(encounter);
        assertEquals(expected, actual);
    }

    @Test
    void shouldAdd() throws DataAccessException {
        Encounter encounter = new Encounter(0, EncounterType.CREATURE, "2/2/2019", "test description", 1);
        EncounterResult expected = new EncounterResult();
        expected.setPayload(encounter);

        EncounterResult actual = service.add(encounter);
        assertEquals(expected, actual);
    }

    private EncounterResult makeResult(String message) {
        EncounterResult result = new EncounterResult();
        result.addErrorMessage(message);
        return result;
    }

    @Test
    void findByType() throws DataAccessException {
        Encounter encounter = service.findByType(EncounterType.CREATURE);
        assertNotNull(encounter);
        assertEquals(EncounterType.CREATURE, encounter.getType());

        encounter = service.findByType(EncounterType.VISION);
        assertEquals(null, encounter);
    }

    @Test
    void update() throws DataAccessException {
        Encounter encounter = service.findByType(EncounterType.CREATURE);
        encounter.setDescription("New test");
        encounter.setOccurrences(2);
        assertTrue(service.update(encounter));

        encounter.setOccurrences(0);
        encounter.setDescription("");
        encounter.setWhen("");
        assertFalse(service.update(encounter));

        encounter = service.findByType(EncounterType.CREATURE);
        assertNotNull(encounter);
        assertEquals("New test", encounter.getDescription());
        assertEquals(2, encounter.getOccurrences());

        Encounter doesNotExist = new Encounter();
        doesNotExist.setType(EncounterType.VISION);
        assertFalse(service.update(doesNotExist));
    }

    //get 1 expected 0
    @Test
    void deleteById() throws DataAccessException {
        int count = service.findAll().size();

        assertTrue(service.deleteById(2));
        assertFalse(service.deleteById(50));
        assertEquals(count -1, service.findAll().size());
    }
}