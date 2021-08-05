package learn.hotel.domain;

import learn.hotel.data.GuestRepositoryDouble;
import learn.hotel.data.HostRepositoryDouble;
import learn.hotel.models.Guest;
import learn.hotel.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HostServiceTest {

    HostService service;

    @BeforeEach
    void setUp() {
        HostRepositoryDouble repo = new HostRepositoryDouble();
        service = new HostService(repo);
    }

    @Test
    void validateHostExistence() {
        Host host = service.findByID("1");
        assertEquals("Chong", host.getLastName());
    }
}