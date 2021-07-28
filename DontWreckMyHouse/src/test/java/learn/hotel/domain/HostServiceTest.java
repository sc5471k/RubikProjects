package learn.hotel.domain;

import learn.hotel.data.HostRepositoryDouble;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HostServiceTest {

    HostService service = new HostService(
            new HostRepositoryDouble());

    @Test
    void findAll() {
    }

    @Test
    void findByID() {
    }
}