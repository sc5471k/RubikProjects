package learn.hotel.domain;

import learn.hotel.data.DataException;
import learn.hotel.data.ReservationRepository;
import learn.hotel.models.Reservation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository repo;
    private final HostService hostService;
    private final GuestService guestService;

    public ReservationService(ReservationRepository repo, HostService hostService, GuestService guestService) {
        this.repo = repo;
        this.hostService = hostService;
        this.guestService = guestService;
    }

    public List<Reservation> getReservations(String hostID) {
        List<Reservation> reservations = repo.getReservations(hostID);
        reservations.sort(Comparator.comparing(Reservation::getStartDate));
        return reservations;
    }

    public Result<Reservation> add(Reservation reservation) throws DataException {
        Result<Reservation> result = validate(reservation, "Add");
        if (!result.isSuccess()) {
            return result;
        }
        result.setPayload(repo.add(reservation));

        return result;
    }

    public Result<Reservation> update(Reservation reservation) throws DataException {
        Result<Reservation> result = validate(reservation, "Update");
        if (!result.isSuccess()) {
            return result;
        }
        if (repo.update(reservation)) {
            result.setPayload(reservation);
        } else {
            String message = String.format("Reservation %s was not found.", reservation.getReservationID());
            result.addErrorMessage(message);
        }

        return result;
    }

    public Result delete(Reservation reservation) throws DataException {
        Result<Reservation> result = validateFutureDateDelete(reservation);
        if (!result.isSuccess()) {
            return result;
        }
        if (!repo.delete(reservation)) {
            String message = String.format("Reservation %s was not found.", reservation.getReservationID());
            result.addErrorMessage(message);
        }
        return result;
    }

    public List<Reservation> getFutureReservations(String hostID) {
        List<Reservation> futureReservations = repo.getFutureReservations(hostID);
        futureReservations.sort(Comparator.comparing(Reservation::getStartDate));
        return futureReservations;
    }

    public List<Reservation> getReservationFromHostGuestID(String hostID, int guestID) {
        return repo.getReservationFromHostGuestID(hostID, guestID);
    }

    public List<Reservation> getReservationFromHostGuestIDFutureDates(String hostID, int guestID) {
        return repo.getReservationFromHostGuestIDFutureDates(hostID, guestID);
    }

    public Reservation getReservationFromReservationHostID(int reservationID, String hostID) {
        return repo.getReservationFromReservationHostID(reservationID, hostID);
    }

    //validate for add and update
    private Result<Reservation> validate(Reservation reservation, String option) {
        Result<Reservation> result = validateNulls(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        result = hostService.validateHostExistence(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        result = guestService.validateGuestExistence(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        validateStartDate(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        validateDateOverlap(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        if (option.equals("Add")) {
            validateFutureDateAdd(reservation, result);
            if (!result.isSuccess()) {
                return result;
            }
        }

        return result;
    }

    private Result<Reservation> validateNulls(Reservation reservation) {
        //Guest, host, and start and end dates are required.
        //guest is int so cant be null
        Result<Reservation> result = new Result<>();

        if (reservation == null) {
            result.addErrorMessage("Nothing to save.");
        }
        if (reservation.getHostID() == null) {
            result.addErrorMessage("Host is required.");
        }
        if (reservation.getStartDate() == null) {
            result.addErrorMessage("Start date is required.");
        }
        if (reservation.getEndDate() == null) {
            result.addErrorMessage("End date is required.");
        }
        return result;
    }

    public void validateStartDate(Reservation reservation, Result<Reservation> result) {
        //The start date must come before the end date
        if (reservation.getStartDate().isAfter(reservation.getEndDate())) {
            result.addErrorMessage("Start date must come before the end date.");
        }
    }

    public void validateDateOverlap(Reservation reservation, Result<Reservation> result) {
        //The reservation may never overlap existing reservation dates.
        List<Reservation> reservations = repo.getReservations(reservation.getHostID());
        for (Reservation r : reservations) {
            if (r.getStartDate() == reservation.getStartDate() && r.getEndDate() == reservation.getEndDate()) {
                result.addErrorMessage("The reservation may never overlap existing reservation dates");
            }
        }
    }

    public void validateFutureDateAdd(Reservation reservation, Result<Reservation> result) {
        //The start date must be in the future.

        if (reservation.getStartDate().isBefore(LocalDate.now())) {
            result.addErrorMessage("Reservation date must be in the future.");
        }
    }

    public Result<Reservation> validateFutureDateDelete(Reservation reservation) {
        //You cannot cancel a reservation that's in the past.
        Result<Reservation> result = new Result<>();

        if (reservation.getStartDate().isBefore(LocalDate.now()) || reservation.getEndDate().isBefore(LocalDate.now())) {
            result.addErrorMessage("Reservation date must be in the future.");
        }
        return result;
    }

    public Result<Reservation> validateReservation(List<Reservation> reservations, int reservationID, Result<Reservation> result) {
        for (Reservation reservation : reservations) {
            if (reservation.getReservationID() != reservationID) {
                result.addErrorMessage("Reservation can't be found");
            }
        }
        return result;
    }
}
