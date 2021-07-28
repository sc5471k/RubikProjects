package learn.hotel.domain;

import learn.hotel.data.GuestRepository;
import learn.hotel.models.Guest;
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

    public Guest findByEmail(String email) {
        Guest guest = repo.findByEmail(email);
        return guest;
    }
}
