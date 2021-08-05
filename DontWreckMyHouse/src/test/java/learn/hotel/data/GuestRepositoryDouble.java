package learn.hotel.data;

import learn.hotel.models.Guest;
import learn.hotel.models.Host;

import java.util.ArrayList;
import java.util.List;

public class GuestRepositoryDouble implements GuestRepository{

    private final ArrayList<Guest> guests = new ArrayList<>();

    public GuestRepositoryDouble() {
        guests.add(new Guest(1, "Sammi", "Chong", "sammi@hotmail.com", "0918233", "OH"));
        guests.add(new Guest(2, "Second", "Last", "second@hotmail.com", "0914394", "CA"));
    }
    @Override
    public List<Guest> findAll() {
        return new ArrayList<>(guests);
    }

    @Override
    public Guest findByEmail(String email) {
        return findAll().stream()
                .filter(i -> i.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Guest findByID(int id) {
        return findAll().stream()
                .filter(i -> i.getGuestID() == id)
                .findFirst()
                .orElse(null);
    }
}
