package learn.hotel.domain;

import learn.hotel.data.HostRepositoryDouble;
import learn.hotel.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @Test
    void shouldNotFindHostExistence() {
        Host host = service.findByID("3");
        assertNull(host);
    }

    @Test
    void calculateTotal() {
        Host host = service.findByID("1");
        LocalDate startDate = LocalDate.parse("2021-08-05");
        LocalDate endDate = LocalDate.parse("2021-08-09");

        BigDecimal total = service.calculateTotal(host, startDate, endDate);
        assertEquals(BigDecimal.valueOf(60), total);
    }
}