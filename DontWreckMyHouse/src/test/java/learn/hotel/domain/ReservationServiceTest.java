package learn.hotel.domain;

import learn.hotel.data.GuestRepositoryDouble;
import learn.hotel.data.HostRepositoryDouble;
import learn.hotel.data.ReservationRepositoryDouble;
import learn.hotel.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

class ReservationServiceTest {

    GuestService guestService;
    HostService hostService;
    ReservationService service;

    @BeforeEach
    void setUp() {
        GuestRepositoryDouble guestRepo = new GuestRepositoryDouble();
        HostRepositoryDouble hostRepo = new HostRepositoryDouble();
        ReservationRepositoryDouble repo = new ReservationRepositoryDouble();

        guestService = new GuestService(guestRepo);
        hostService = new HostService(hostRepo);
        service = new ReservationService(repo, hostService, guestService);
    }

    @Test
    void validateReservation() {
        Result<Reservation> result = new Result<>();
        Reservation reservation = new Reservation();
        List<Reservation> reservationList = new ArrayList<>();

        reservation.setHostID("1");
        reservation.setReservationID(6);
        reservation.setStartDate(LocalDate.now().plusDays(5));
        reservation.setEndDate(LocalDate.now().plusDays(10));
        reservation.setGuestID(5);
        reservation.setTotal(BigDecimal.valueOf(700));
        reservationList.add(reservation);

        reservation.setHostID("2");
        reservation.setReservationID(8);
        reservation.setStartDate(LocalDate.now().plusDays(10));
        reservation.setEndDate(LocalDate.now().plusDays(15));
        reservation.setGuestID(5);
        reservation.setTotal(BigDecimal.valueOf(700));
        reservationList.add(reservation);

        service.validateReservation(reservationList, 10, result);
        assertFalse(result.isSuccess());
    }

    //    The start date must come before the end date.
    @Test
    void validateStartDate() {
        Result<Reservation> result = new Result<>();
        Reservation reservation = new Reservation();

        reservation.setHostID("1");
        reservation.setReservationID(6);
        reservation.setStartDate(LocalDate.now().plusDays(5));
        reservation.setEndDate(LocalDate.now());
        reservation.setGuestID(5);
        reservation.setTotal(BigDecimal.valueOf(700));

        service.validateStartDate(reservation, result);
        assertFalse(result.isSuccess());
    }

    //    The reservation may never overlap existing reservation dates.
    @Test
    void validateDateOverlap() {
        Result<Reservation> result = new Result<>();
        Reservation reservation = new Reservation();
        List<Reservation> reservationList = new ArrayList<>();

        reservation.setHostID("1");
        reservation.setReservationID(6);
        reservation.setStartDate(LocalDate.now().plusDays(5));
        reservation.setEndDate(LocalDate.now().plusDays(10));
        reservation.setGuestID(5);
        reservation.setTotal(BigDecimal.valueOf(700));
        reservationList.add(reservation);

        reservation.setHostID("1");
        reservation.setReservationID(8);
        reservation.setStartDate(LocalDate.now().plusDays(5));
        reservation.setEndDate(LocalDate.now().plusDays(10));
        reservation.setGuestID(5);
        reservation.setTotal(BigDecimal.valueOf(700));
        reservationList.add(reservation);

        service.validateDateOverlap(reservation, result);
        assertFalse(result.isSuccess());
    }

    //    The start date must be in the future.
    @Test
    void validateFutureStartDate() {
        Result<Reservation> result = new Result<>();
        Reservation reservation = new Reservation();

        reservation.setHostID("1");
        reservation.setReservationID(6);
        reservation.setStartDate(LocalDate.now().minusDays(5));
        reservation.setEndDate(LocalDate.now().minusDays(3));
        reservation.setGuestID(5);
        reservation.setTotal(BigDecimal.valueOf(700));

        service.validateFutureDateAdd(reservation, result);
        assertFalse(result.isSuccess());
    }

    //    You cannot cancel a reservation that's in the past.
    @Test
    void validateCancel() {
        Result<Reservation> result;
        Reservation reservation = new Reservation();

        reservation.setHostID("1");
        reservation.setReservationID(6);
        reservation.setStartDate(LocalDate.now().minusDays(5));
        reservation.setEndDate(LocalDate.now().minusDays(3));
        reservation.setGuestID(5);
        reservation.setTotal(BigDecimal.valueOf(700));

        result = service.validateFutureDateDelete(reservation);
        assertFalse(result.isSuccess());
    }
}