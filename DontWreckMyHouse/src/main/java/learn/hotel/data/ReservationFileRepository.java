package learn.hotel.data;

import learn.hotel.models.Reservation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
public class ReservationFileRepository implements ReservationRepository{

    private final String directory;

    public ReservationFileRepository(@Value("${ReservationFilePath}") String directory){
        this.directory = directory;
    }

    @Override
    public List<Reservation> getReservations(String id) {
        ArrayList<Reservation> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath(id)))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 5) {
                    result.add(deserialize(fields, id));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }

    public List<Reservation> sortReservationsByDate(String id) {
        return (List<Reservation>) getReservations(id).stream()
                .sorted(Comparator.comparing(Reservation::getEndDate));
    }

    private Reservation deserialize(String[] fields, String id) {
        Reservation result = new Reservation();
        result.setReservationID((Integer.parseInt(fields[0])));
        result.setStartDate(LocalDate.parse(fields[1]));
        result.setEndDate(LocalDate.parse(fields[2]));
        result.setHostID(id);
        result.setGuestID(Integer.parseInt(fields[3]));
        result.setTotal(new BigDecimal(fields[4]));

        return result;
    }

    private String getFilePath(String id) {
        return Paths.get(directory, id + ".csv").toString();
    }
}
