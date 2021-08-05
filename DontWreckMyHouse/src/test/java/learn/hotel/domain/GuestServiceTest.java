package learn.hotel.domain;

import learn.hotel.data.GuestRepositoryDouble;
import learn.hotel.models.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuestServiceTest {

    GuestService service;

    @BeforeEach
    void setUp() {
        GuestRepositoryDouble repo = new GuestRepositoryDouble();
        service = new GuestService(repo);
    }

    @Test
    void validateGuestExistence() {
        Guest guest = service.findByID(1);
        assertEquals("Sammi", guest.getFirstName());
    }
}