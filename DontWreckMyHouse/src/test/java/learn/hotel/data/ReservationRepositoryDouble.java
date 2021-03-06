package learn.hotel.data;

import learn.hotel.models.Reservation;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationRepositoryDouble implements ReservationRepository {

    private final ArrayList<Reservation> reservations = new ArrayList<>();
    private static final String HEADER = "id,start_date,end_date,guest_id,total";
    private final String directory = "./data/reservation_data_test";

    public ReservationRepositoryDouble() {
        Reservation reservation = new Reservation();
        reservation.setReservationID(1);
        reservation.setStartDate(LocalDate.parse("2021-10-12"));
        reservation.setEndDate(LocalDate.parse("2021-10-14"));
        reservation.setGuestID(663);
        reservation.setTotal(BigDecimal.valueOf(400));

        reservations.add(reservation);
    }

    @Override
    public List<Reservation> getReservations(String id) {
        return new ArrayList<>(reservations);
    }

    @Override
    public List<Reservation> getFutureReservations(String id) {
        return reservations.stream()
                .filter(reservation -> reservation.getStartDate().isAfter(LocalDate.now()))
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> getReservationFromHostGuestID(String hostID, int guestID) {
        return reservations.stream()
                .filter(reservation -> reservation.getGuestID() == guestID)
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .collect(Collectors.toList());
    }

    @Override
    public Reservation getReservationFromReservationHostID(int reservationID, String hostID) {
        return reservations.stream()
                .filter(reservation -> reservation.getReservationID() == reservationID)
                .findFirst().get();
    }

    @Override
    public List<Reservation> getReservationFromHostGuestIDFutureDates(String hostID, int guestID) {
        return reservations.stream()
                .filter(reservation -> reservation.getGuestID() == guestID)
                .filter(reservation -> reservation.getStartDate().isAfter(LocalDate.now()))
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .collect(Collectors.toList());
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        if (reservation == null) {
            return null;
        }

        List<Reservation> all = reservations;

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
        List<Reservation> all = reservations;
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
    public boolean delete(Reservation reservation) throws DataException {
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

    private String serialize(Reservation reservation) {
        return String.format("%s,%s,%s,%s,%s",
                reservation.getReservationID(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getGuestID(),
                reservation.getTotal());
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

    private String getFilePath(String id) {
        return Paths.get(directory, id + ".csv").toString();
    }
}
