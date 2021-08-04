package learn.hotel.data;

import learn.hotel.models.Reservation;

import java.util.List;

public interface ReservationRepository {

    List<Reservation> getReservations(String id);
    List<Reservation> sortReservationsByDate(String id);
    List<Reservation> getFutureReservations(String id);
    List<Reservation> getReservationFromHostGuestID(String hostID, int guestID);
    Reservation add(Reservation reservation) throws DataException;
    boolean update(Reservation reservation) throws DataException;
    boolean delete(int reservationID, String hostID) throws DataException;

}
