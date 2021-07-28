package learn.hotel.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Reservation {
    private int reservationID;
    private LocalDate startDate;
    private LocalDate endDate;
    private String hostID;
    private int guestID;
    private BigDecimal total;

    public Reservation(int reservationID, LocalDate startDate, LocalDate endDate, String hostID, int guestID, BigDecimal total) {
        this.reservationID = reservationID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hostID = hostID;
        this.guestID = guestID;
        this.total = total;
    }

    public Reservation() {

    }

    public int getReservationID() {
        return reservationID;
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getHostID() {
        return hostID;
    }

    public void setHostID(String hostID) {
        this.hostID = hostID;
    }

    public int getGuestID() {
        return guestID;
    }

    public void setGuestID(int guestID) {
        this.guestID = guestID;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
