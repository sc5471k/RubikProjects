package learn.hotel.data;

import learn.hotel.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostFileRepositoryTest {

    static final String SEED_PATH = "./data/host-seed.csv";
    static final String TEST_PATH = "./data/host-test.csv";

    HostFileRepository repo = new HostFileRepository(TEST_PATH);

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_PATH);
        Path testPath = Paths.get(TEST_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void findAll() {
        List<Host> all = repo.findAll();
        assertEquals(8, all.size());
    }

    @Test
    void shouldFindHostByID() {
        Host host = repo.findByID("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        assertNotNull(host);
        assertEquals("Yearnes", host.getLastName());
        assertEquals("eyearnes0@sfgate.com", host.getEmail());
        assertEquals("(806) 1783815", host.getPhone());
        assertEquals("3 Nova Trail", host.getAddress());
        assertEquals("Amarillo", host.getCity());
        assertEquals("TX", host.getState());
        assertEquals("79182", host.getPostalCode());
        assertEquals(new BigDecimal("340"), host.getStandardRate());
        assertEquals(new BigDecimal("425"), host.getWeekendRate());
    }
}