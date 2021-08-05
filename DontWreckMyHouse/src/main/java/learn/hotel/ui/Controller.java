package learn.hotel.ui;

import learn.hotel.data.DataException;
import learn.hotel.domain.HostService;
import learn.hotel.domain.ReservationService;
import learn.hotel.domain.Result;
import learn.hotel.models.Host;
import learn.hotel.models.Reservation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Controller {

    private final View view;
    private final HostService hostService;
    private final ReservationService reservationService;

    public Controller(View view, HostService hostService, ReservationService reservationService) {
        this.view = view;
        this.hostService = hostService;
        this.reservationService = reservationService;
    }

    public void run() {
        view.displayHeader("Welcome to Don't Wreck My Home");
        try {
            runAppLoop();
        } catch (DataException ex) {
            view.displayException(ex);
        }
        view.displayHeader("Goodbye.");
    }

    private void runAppLoop() throws DataException {
        MainMenuOption option;
        do {
            option = view.selectMainMenuOption();
            switch (option) {
                case VIEW_RESERVATION:
                    viewReservation();
                    break;
                case ADD_RESERVATION:
                    addReservation();
                    break;
                case UPDATE_RESERVATION:
                    updateReservation();
                    break;
                case DELETE_RESERVATION:
                    deleteReservation();
                    break;
            }
        } while (option != MainMenuOption.EXIT);
    }

    private void viewReservation() {
        String hostID;
        Host host;

        int option = view.chooseHostOption();

        view.displayHeader("\nSearch Host");
        if(option == 1) {
            List<Host> hosts = hostService.findAll();
            view.displayHosts(hosts);
        }

        hostID = view.getHostID();
        host = hostService.findByID(hostID);
        view.displayHost(host);

        if(host != null) {
            List<Reservation> reservations = reservationService.getReservations(hostID);
            view.displayHeader("\nReservations - " + host.getLastName());
            view.displayReservations(reservations);
        }
        else {
            return;
        }
    }

    private void addReservation() throws DataException {
        String hostID;
        Host host;

        view.displayHeader(MainMenuOption.ADD_RESERVATION.getMessage());
        int option = view.chooseHostOption();

        view.displayHeader("\nSearch Host");
        if(option == 1) {
            List<Host> hosts = hostService.findAll();
            view.displayHosts(hosts);
        }

        hostID = view.getHostID();
        host = hostService.findByID(hostID);
        view.displayHost(host);

        if(host != null) {
            List<Reservation> futureReservations = reservationService.getFutureReservations(hostID);
            view.displayHeader("\nReservations - " + host.getLastName());
            view.displayReservations(futureReservations);

            Reservation reservation = view.makeReservation(host);
            Result<Reservation> result = reservationService.add(reservation);

            if(result.isSuccess()) {
                String successMessage = String.format("Reservation %s created.", result.getPayload().getReservationID());
                view.displayStatus(true, successMessage);
            }
            else {
                view.displayStatus(false, result.getErrorMessages());
            }
        }
        else {
            return;
        }
    }

    private void updateReservation() throws DataException {
        Host host;

        view.displayHeader("Edit a Reservation");
        int guestID = view.getGuestID();
        String hostID = view.getHostID();
        host = hostService.findByID(hostID);

        if(host != null) {
            List<Reservation> reservations = reservationService.getReservationFromHostGuestID(hostID, guestID);
            view.displayHeader("\nReservations - " + host.getLastName());
            view.displayReservations(reservations);

            if(reservations.size() != 0) {
                int reservationID =view.getReservationID();
                view.displayHeader("\nEditing Reservation " + reservationID);

                Reservation reservation = view.makeReservation(host);
                reservation.setReservationID(reservationID);
                Result<Reservation> result = reservationService.update(reservation);

                if(result.isSuccess()) {
                    String successMessage = String.format("Reservation %s updated.", result.getPayload().getReservationID());
                    view.displayStatus(true, successMessage);
                }
                else {
                    view.displayStatus(false, result.getErrorMessages());
                }
            }
            else {
                return;
            }
        }
        else {
            return;
        }
    }

    private void deleteReservation() throws DataException {
        Host host;

        view.displayHeader("Cancel a Reservation");

        int guestID = view.getGuestID();
        String hostID = view.getHostID();
        host = hostService.findByID(hostID);

        if(host != null) {
            List<Reservation> reservations = reservationService.getReservationFromHostGuestID(hostID, guestID);
            view.displayHeader("\nReservations - " + host.getLastName());
            view.displayReservations(reservations);

            if(reservations.size() != 0) {
                int reservationID = view.getReservationID();
                reservations = reservationService.getReservationFromReservationHostID(reservationID, hostID);
                Result result = reservationService.delete(reservations);

                if (result.isSuccess()) {
                    String successMessage = String.format("Reservation %s cancelled.", reservationID);
                    view.displayStatus(true, successMessage);
                } else {
                    view.displayStatus(false, result.getErrorMessages());
                }
            }
            else {
                return;
            }
        }
        else {
            return;
        }
    }
}
