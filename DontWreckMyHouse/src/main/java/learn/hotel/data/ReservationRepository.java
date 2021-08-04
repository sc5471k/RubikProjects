package learn.hotel.data;

import learn.hotel.models.Reservation;

import java.util.List;

public interface ReservationRepository {

    List<Reservation> getReservations(String id);
    List<Reservation> sortReservationsByDate(String id);
    List<Reservation> getFutureReservations(String id);
    List<Reservation> getReservationFromHostGuestID(String hostID, int guestID);
    List<Reservation> getReservationFromReservationHostID(int reservationID, String hostID);
    Reservation add(Reservation reservation) throws DataException;
    boolean update(Reservation reservation) throws DataException;
    boolean delete(List<Reservation> reservation) throws DataException;

}
