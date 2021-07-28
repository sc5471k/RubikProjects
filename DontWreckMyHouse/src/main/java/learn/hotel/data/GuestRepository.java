package learn.hotel.data;

import learn.hotel.models.Guest;

import java.util.List;

public interface GuestRepository {

    List<Guest> findAll();

    Guest findByEmail(String email);

    Guest findByID(int id);
}
