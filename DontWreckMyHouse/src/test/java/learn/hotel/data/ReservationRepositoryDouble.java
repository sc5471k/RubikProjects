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

    @Override
    public List<Reservation> getFutureReservations(String id) {
        return null;
    }

    @Override
    public List<Reservation> getReservationFromHostGuestID(String hostID, int guestID) {
        return null;
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        return null;
    }

    @Override
    public boolean update(Reservation reservation) throws DataException {
        return false;
    }

    @Override
    public boolean delete(int reservationID, String hostID) throws DataException {
        return false;
    }
}
