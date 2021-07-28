package learn.hotel.data;

import learn.hotel.models.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestFileRepositoryTest {

    static final String SEED_PATH = "./data/guest-seed.csv";
    static final String TEST_PATH = "./data/guest-test.csv";

    GuestFileRepository repo = new GuestFileRepository(TEST_PATH);

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_PATH);
        Path testPath = Paths.get(TEST_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void findAll() {
        List<Guest> all = repo.findAll();
        assertEquals(4, all.size());
    }

    @Test
    void findByEmail() {
        Guest guest = repo.findByEmail("slomas0@mediafire.com");
        assertNotNull(guest);
        assertEquals(1, guest.getGuestID());
        assertEquals("Sullivan", guest.getFirstName());
        assertEquals("Lomas", guest.getLastName());
        assertEquals("(702) 7768761", guest.getPhone());
        assertEquals("NV", guest.getState());
    }
}