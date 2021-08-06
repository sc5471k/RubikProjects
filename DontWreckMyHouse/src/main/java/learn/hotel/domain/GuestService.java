package learn.hotel.domain;

import learn.hotel.data.GuestRepository;
import learn.hotel.models.Guest;
import learn.hotel.models.Reservation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService {

    private final GuestRepository repo;

    public GuestService(GuestRepository repo) {
        this.repo = repo;
    }

    public List<Guest> findAll() {
        return repo.findAll();
    }

    public Guest findByID(int id) {
        return repo.findByID(id);
    }

    public Result<Reservation> validateGuestExistence(Reservation reservation, Result<Reservation> result) {
        //The guest must already exist in the "database". Cannot be created.
        Guest guest = repo.findByID(reservation.getGuestID());
        if (guest == null) {
            result.addErrorMessage("Guest must already exist");
        }

        return result;
    }
}
