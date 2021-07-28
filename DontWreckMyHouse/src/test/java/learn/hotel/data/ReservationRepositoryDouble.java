package learn.hotel.data;

import learn.hotel.models.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepositoryDouble implements ReservationRepository {

    private final ArrayList<Reservation> reservations = new ArrayList<>();

    public ReservationRepositoryDouble() {
        Reservation reservation = new Reservation();
        reservation.setReservationID(1);
        reservation.setStartDate(LocalDate.parse("2021-10-12"));
        reservation.setEndDate(LocalDate.parse("2021-10-14"));
        reservation.setGuestID(663);
        reservation.setTotal(BigDecimal.valueOf(400));
    }
    @Override
    public List<Reservation> getReservations(String id) {
        return null;
    }

    @Override
    public List<Reservation> sortReservationsByDate(String id) {
        return null;
    }
}
