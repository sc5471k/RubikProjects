package learn.hotel.domain;

import learn.hotel.data.DataException;
import learn.hotel.data.ReservationRepository;
import learn.hotel.models.Reservation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository repo;

    public ReservationService(ReservationRepository repo) {
        this.repo = repo;
    }

    public List<Reservation> getReservations(String hostID) {
        List<Reservation> reservations = repo.getReservations(hostID);
        Collections.sort(reservations, Comparator.comparing(Reservation::getStartDate));
        return reservations;
    }

    public Result<Reservation> add(Reservation reservation) throws DataException {
        Result<Reservation> result = validate(reservation);

        //CHANGE
        //result = validateFutureDate(reservation);
        //CHANGE
        if (!result.isSuccess()) {
            return result;
        }
        result.setPayload(repo.add(reservation));

        return result;
    }

    public Result<Reservation> update(Reservation reservation) throws DataException {
        Result<Reservation> result = validate(reservation);
        if (!result.isSuccess()) {
            return result;
        }
        if (repo.update(reservation)) {
            result.setPayload(reservation);
        } else {
            String message = String.format("Reservation id %s was not found.", reservation.getReservationID());
            result.addErrorMessage(message);
        }

        return result;
    }

    public Result delete(int reservationID, String hostID) throws DataException {
        Result result = new Result()    ;
        if (!repo.delete(reservationID, hostID)) {
            String message = String.format("Reservation %s was not found.", reservationID);
            result.addErrorMessage(message);
        }
        return result;
    }

    public List<Reservation> getFutureReservations(String hostID) {
        List<Reservation> futureReservations = repo.getFutureReservations(hostID);
        Collections.sort(futureReservations, Comparator.comparing(Reservation::getStartDate));
        return futureReservations;
    }

    public List<Reservation> getReservationFromHostGuestID(String hostID, int guestID) {
        List<Reservation> reservations = repo.getReservationFromHostGuestID(hostID, guestID);
        return reservations;
    }

    private Result<Reservation> validate(Reservation reservation) {
        Result<Reservation> result = validateNulls(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        validateFields(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        validateExistence(reservation, result);

        return result;
    }

    private Result<Reservation> validateNulls(Reservation reservation) {
        Result<Reservation> result = new Result<>();

        if(reservation == null) {
            result.addErrorMessage("Nothing to save.");
            return result;
        }
        if (reservation.getStartDate() == null) {
            result.addErrorMessage("Start date is required.");

        }
        if (reservation.getEndDate() == null) {
            result.addErrorMessage("End date is required.");
        }
        return result;
    }

    private void validateFutureDate(Reservation reservation) {
        //The start date must be in the future.
        if (reservation.getStartDate().isBefore(LocalDate.now())) {
            //result.addErrorMessage("Forage date must be in the future.");
        }
    }

    private void validateFields(Reservation reservation, Result<Reservation> result) {
        //The start date must come before the end date
        if (reservation.getStartDate().isAfter(reservation.getEndDate())) {
            result.addErrorMessage("Start date must come before the end date.");
        }

        //The reservation may never overlap existing reservation dates.

    }

    private void validateExistence(Reservation reservation, Result<Reservation> result) {
        //The guest and host must already exist in the "database". Guests and hosts cannot be created.

        //guest = repo.findByID(reservation.getGuestID());

    }
}
