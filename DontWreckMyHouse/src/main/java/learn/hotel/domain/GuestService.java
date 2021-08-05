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
        List<Guest> guests = repo.findAll();
        return guests;
    }

    public Guest findByID(int id) {
        Guest guest = repo.findByID(id);
        return guest;
    }

    public Result<Reservation> validateGuestExistence(Reservation reservation, Result<Reservation> result) {
        //The guest must already exist in the "database". Cannot be created.
        Guest guest = repo.findByID(reservation.getGuestID());
        if(guest.getGuestID() != reservation.getGuestID()) {
            result.addErrorMessage("Guest must already exist");
        }

        return result;
    }
}
