package learn.hotel.data;

import learn.hotel.domain.ReservationService;
import learn.hotel.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationFileRepositoryTest {

    static final String SEED_PATH = "./data/reservation-seed.csv";
    static final String TEST_PATH = "./data/reservation_data_test/1.csv";
    static final String TEST_DIR_PATH = "./data/reservation_data_test";

    ReservationFileRepository repo = new ReservationFileRepository(TEST_DIR_PATH);

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_PATH);
        Path testPath = Paths.get(TEST_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void getReservations() {
        List<Reservation> reservations = repo.getReservations("1");
        assertEquals(5, reservations.size());
    }

    @Test
    void getFutureReservations() {
        List<Reservation> reservations = repo.getFutureReservations("1");
        assertEquals(4, reservations.size());
    }

    @Test
    void getReservationFromHostGuestID() {
        List<Reservation> reservations = repo.getReservationFromHostGuestID("1", 42);
        assertEquals(4, reservations.get(0).getReservationID());
    }

    @Test
    void getReservationFromReservationHostID() {
        List<Reservation> reservations = repo.getReservationFromReservationHostID(1, "1");
        assertEquals(1, reservations.get(0).getReservationID());
        assertEquals(663, reservations.get(0).getGuestID());
        assertEquals(BigDecimal.valueOf(400), reservations.get(0).getTotal());
    }

    @Test
    void add() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setHostID("1");
        reservation.setReservationID(6);
        reservation.setStartDate(LocalDate.now());
        reservation.setEndDate(LocalDate.now().plusDays(5));
        reservation.setGuestID(5);
        reservation.setTotal(BigDecimal.valueOf(700));

        repo.add(reservation);
        List<Reservation> list = repo.getReservations("1");
        assertEquals(6, list.size());
    }

    @Test
    void update() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setHostID("1");
        reservation.setReservationID(5);
        reservation.setStartDate(LocalDate.now().plusDays(5));
        reservation.setEndDate(LocalDate.now().plusDays(10));
        reservation.setGuestID(1);
        reservation.setTotal(BigDecimal.valueOf(400));

        repo.update(reservation);
        List<Reservation> list = repo.getReservationFromHostGuestID("1", 1);
        assertEquals(LocalDate.now().plusDays(5), list.get(0).getStartDate());
        assertEquals(LocalDate.now().plusDays(10), list.get(0).getEndDate());
    }

    @Test
    void delete() throws DataException {
        int count = repo.getReservations("1").size();
        List<Reservation> list = repo.getReservationFromHostGuestID("1", 1);
        assertTrue(repo.delete(list));
        assertEquals(count -1, repo.getReservations("1").size());
    }
}