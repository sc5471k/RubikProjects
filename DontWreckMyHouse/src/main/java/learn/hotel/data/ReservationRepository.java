package learn.hotel.data;

import learn.hotel.models.Reservation;

import java.util.List;

public interface ReservationRepository {

    List<Reservation> getReservations(String id);
    List<Reservation> sortReservationsByDate(String id);
}
