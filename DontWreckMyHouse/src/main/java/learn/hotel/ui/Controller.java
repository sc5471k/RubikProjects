package learn.hotel.ui;

import learn.hotel.data.DataException;
import learn.hotel.domain.GuestService;
import learn.hotel.domain.HostService;
import learn.hotel.domain.ReservationService;
import learn.hotel.models.Guest;
import learn.hotel.models.Host;
import learn.hotel.models.Reservation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Controller {

    private final View view;
    private final HostService hostService;
    private final ReservationService reservationService;
    private final GuestService guestService;

    public Controller(View view, HostService hostService, ReservationService reservationService, GuestService guestService) {
        this.view = view;
        this.hostService = hostService;
        this.reservationService = reservationService;
        this.guestService = guestService;
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
        String hostID = "";
        Host host = null;

        view.displayHeader(MainMenuOption.VIEW_RESERVATION.getMessage());
        int option = view.chooseHostOption();

        view.displayHeader("\nSearch Host");
        if(option == 1) {
            List<Host> hosts = hostService.findAll();
            view.displayHosts(hosts);
        }

        hostID = view.getHostID();
        host = hostService.findByID(hostID);
        view.displayHost(host);

        List<Reservation> reservations = reservationService.getReservations(hostID);
        //reservations = reservationService.sortReservationsByDate(hostID);
        view.displayHeader("\nReservations - " + host.getLastName());
        view.displayReservations(reservations);
    }

    private void addReservation() {
    }

    private void updateReservation() {
    }

    private void deleteReservation() {
    }
}
