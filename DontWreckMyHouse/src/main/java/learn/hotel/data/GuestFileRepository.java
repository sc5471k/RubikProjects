package learn.hotel.data;

import learn.hotel.models.Guest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GuestFileRepository implements GuestRepository{
    private final String directory;

    public GuestFileRepository(@Value("${GuestFilePath}") String directory) {
        this.directory = directory;
    }

    @Override
    public List<Guest> findAll() {
        ArrayList<Guest> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(directory))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 6) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
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

    private Guest deserialize(String[] fields) {
        Guest guest = new Guest();
        guest.setGuestID(Integer.parseInt(fields[0]));
        guest.setFirstName(fields[1]);
        guest.setLastName(fields[2]);
        guest.setEmail(fields[3]);
        guest.setPhone(fields[4]);
        guest.setState(fields[5]);
        return guest;
    }
}
