package learn.hotel.ui;

import learn.hotel.data.DataException;
import learn.hotel.domain.GuestService;
import learn.hotel.domain.HostService;
import learn.hotel.models.Guest;
import learn.hotel.models.Host;
import learn.hotel.models.Reservation;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class View {

    private final ConsoleIO io;
    //CHANGE
    private final GuestService guestService;
    private final HostService hostService;

    private final String hostAlignFormat = "| %-15s | %-23s | %-10s | %-15s | %-14s |%n";

    public View(ConsoleIO io, GuestService guestService, HostService hostService) {
        this.io = io;
        this.guestService = guestService;
        this.hostService = hostService;
    }

    public MainMenuOption selectMainMenuOption() {
        displayHeader("Main Menu");
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (MainMenuOption option : MainMenuOption.values()) {
            if (!option.isHidden()) {
                io.printf("%s. %s%n", option.getValue(), option.getMessage());
            }
            min = Math.min(min, option.getValue());
            max = Math.max(max, option.getValue());
        }

        String message = String.format("Select [%s-%s]: ", min, max - 1);
        return MainMenuOption.fromValue(io.readInt(message, min, max));
    }

    public int chooseHostOption() {
        displayHeader(MainMenuOption.VIEW_RESERVATION.getMessage());
        io.println("1. Search for host");
        io.println("2. Search host by ID");

        return io.readInt("Select an option [1-2]: ");
    }

    public String getHostID() {
        return io.readString("Enter Host ID: ");
    }

    public int getGuestID() {
        return io.readInt("Enter Guest ID: ");
    }

    public int getReservationID() {
        return io.readInt("Enter Reservation ID: ");
    }

    public Reservation makeReservation(Host host) {
        LocalDate startDate = io.readLocalDate("Start date [MM/dd/yyyy]: ");
        LocalDate endDate = io.readLocalDate("End date [MM/dd/yyyy]: ");

        BigDecimal total = hostService.calculateTotal(host, startDate, endDate);

        displayHeader("Summary");
        io.println("Start: " + startDate);
        io.println("End: " + endDate);
        io.println("Total: " + total);
        String confirm = io.readString("Is this okay? [y/n]: ").toLowerCase();

        Reservation reservation = new Reservation();
        if(confirm.equals("y")) {
            reservation.setStartDate(startDate);
            reservation.setEndDate(endDate);
            reservation.setHostID(host.getHostID());
            reservation.setGuestID(io.readInt("Guest ID: "));
            reservation.setTotal(total);
        }
        return reservation;
    }

    //DISPLAY-----------------------------------------------------------------------------------------------------------
    public void displayHeader(String message) {
        io.println("");
        io.println(message);
        io.println("=".repeat(message.length()));
    }

    public void displayException(DataException ex) {
        displayHeader("A critical error occurred:");
        io.println(ex.getMessage());
    }

    public void displayHosts(List<Host> hosts) {
        if (hosts == null || hosts.isEmpty()) {
            io.println("No hosts found.");
            return;
        }
        io.printf("+--------------------------------------+-------------------------+------------+-----------------+----------------+%n");
        io.printf("| HostID                               | Last Name               | Postcode   | Standard Rate   | Weekend Rate   |%n");
        io.printf("+--------------------------------------+-------------------------+------------+-----------------+----------------+%n");
        for (Host host : hosts) {
            io.printf(hostAlignFormat, host.getHostID(), host.getLastName(), host.getPostalCode(),
                    host.getStandardRate(), host.getWeekendRate());
            io.printf("+--------------------------------------+-------------------------+------------+-----------------+----------------+%n");
        }
    }

    public void displayHost(Host host) {
        if (host == null ) {
            io.println("Host does not exist.");
            return;
        }
        io.printf("+--------------------------------------+-------------------------+------------+-----------------+----------------+%n");
        io.printf("| HostID                               | Last Name               | Postcode   | Standard Rate   | Weekend Rate   |%n");
        io.printf("+--------------------------------------+-------------------------+------------+-----------------+----------------+%n");
        io.printf(hostAlignFormat, host.getHostID(), host.getLastName(), host.getPostalCode(),
                host.getStandardRate(), host.getWeekendRate());
        io.printf("+--------------------------------------+-------------------------+------------+-----------------+----------------+%n");
    }

    public void displayReservations(List<Reservation> reservations) {
        Guest guest;

        if (reservations == null || reservations.isEmpty()) {
            io.println("Host has no reservations");
            return;
        }
        io.printf("+-------------------+---------------+-------------+-----------------------------+----------+%n");
        io.printf("| Reservation ID    | Start Date    | End Date    | Guest Email                 | Total    |%n");
        io.printf("+-------------------+---------------+-------------+-----------------------------+----------+%n");
        for (Reservation reservation : reservations) {
            //CHANGE
            guest = guestService.findByID(reservation.getGuestID());
            //CHANGE
            String reservationAlignFormat = "| %-17s | %-13s | %-11s | %-27s | %-8s |%n";
            io.printf(reservationAlignFormat, reservation.getReservationID(), reservation.getStartDate(),
                    reservation.getEndDate(), guest.getEmail(), reservation.getTotal());
            io.printf("+-------------------+---------------+-------------+-----------------------------+----------+%n");
        }
    }

    public void displayStatus(boolean success, String message) {
        displayStatus(success, List.of(message));
    }

    public void displayStatus(boolean success, List<String> messages) {
        displayHeader(success ? "Success" : "Error");
        for (String message : messages) {
            io.println(message);
        }
    }
}
