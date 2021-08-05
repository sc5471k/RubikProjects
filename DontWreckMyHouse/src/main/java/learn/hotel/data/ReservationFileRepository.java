package learn.hotel.data;

import learn.hotel.models.Reservation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ReservationFileRepository implements ReservationRepository{

    private static final String HEADER = "id,start_date,end_date,guest_id,total";
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

    @Override
    public List<Reservation> getFutureReservations(String id) {
        return getReservations(id).stream()
                .filter(reservation -> reservation.getStartDate().isAfter(LocalDate.now()))
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> getReservationFromHostGuestID(String hostID, int guestID) {
        return getReservations(hostID).stream()
                .filter(reservation -> reservation.getGuestID() == guestID)
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> getReservationFromReservationHostID(int reservationID, String hostID) {
        return   getReservations(hostID).stream()
                .filter(reservation -> reservation.getReservationID() == reservationID)
                .collect(Collectors.toList());
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        if (reservation == null) {
            return null;
        }

        List<Reservation> all = getReservations(reservation.getHostID());

        int nextId = all.stream()
                .mapToInt(Reservation::getReservationID)
                .max()
                .orElse(0) + 1;

        reservation.setReservationID(nextId);

        all.add(reservation);
        writeAll(all, reservation.getHostID());

        return reservation;
    }

    @Override
    public boolean update(Reservation reservation) throws DataException {
        List<Reservation> all = getReservations(reservation.getHostID());
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getGuestID() == reservation.getGuestID()) {
                all.set(i, reservation);
                writeAll(all, reservation.getHostID());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(List<Reservation> reservations) throws DataException {
        Reservation reservation = reservations.get(0);
        List<Reservation> all = getReservations(reservation.getHostID());
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getReservationID() == reservation.getReservationID() && all.get(i).getHostID().equals(reservation.getHostID())) {
                all.remove(i);
                writeAll(all, reservation.getHostID());
                return true;
            }
        }
        return false;
    }

    private void writeAll(List<Reservation> reservations, String id) throws DataException {
        try (PrintWriter writer = new PrintWriter(getFilePath(id))) {

            writer.println(HEADER);

            for (Reservation r : reservations) {
                writer.println(serialize(r));
            }
        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }

    private String serialize(Reservation reservation) {
        return String.format("%s,%s,%s,%s,%s",
                reservation.getReservationID(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getGuestID(),
                reservation.getTotal());
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
