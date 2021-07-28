package learn.hotel.domain;

import learn.hotel.data.ReservationRepository;
import learn.hotel.models.Reservation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository repo;

    public ReservationService(ReservationRepository repo) {
        this.repo = repo;
    }

    public List<Reservation> getReservations(String hostID) {
        List<Reservation> reservations = repo.getReservations(hostID);
        return reservations;
    }

    public List<Reservation> sortReservationsByDate(String id) {
        List<Reservation> reservations = repo.sortReservationsByDate(id);
        return reservations;
    }
}
