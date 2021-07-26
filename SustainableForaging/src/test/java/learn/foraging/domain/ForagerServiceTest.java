package learn.foraging.domain;

import learn.foraging.data.DataException;
import learn.foraging.data.ForagerRepositoryDouble;
import learn.foraging.models.Forager;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ForagerServiceTest {

    ForagerService service = new ForagerService(
            new ForagerRepositoryDouble());

    @Test
    void findByState() {

    }

    @Test
    void findAll() {
        List<Forager> foragers = service.findAll();
        assertNotNull(foragers);
    }

    @Test
    void findByLastName() {
    }

    @Test
    void add() throws DataException {
        Forager forager = new Forager();
        forager.setFirstName("Sammi");
        forager.setLastName("Chong");
        forager.setState("OH");
        Result<Forager> result = service.add(forager);

        assertTrue(result.isSuccess());

        Forager forager2 = new Forager();
        forager2.setFirstName("Sammi");
        forager2.setState("OH");
        result = service.add(forager2);

        assertFalse(result.isSuccess());
    }

    @Test
    void noDuplication() throws DataException {
        Forager forager = new Forager();
        forager.setFirstName("Sammi");
        forager.setLastName("Chong");
        forager.setState("OH");
        Result<Forager> result = service.add(forager);

        Forager forager2 = new Forager();
        forager2.setFirstName("Sammi");
        forager2.setLastName("Chong");
        forager2.setState("OH");
        result = service.add(forager2);

        assertFalse(result.isSuccess());

    }
}