package learn.hotel.ui;

public enum MainMenuOption {

    EXIT(0, "Exit", false),
    VIEW_RESERVATION(1, "View Reservations", false),
    ADD_RESERVATION(2, "Add a Reservation", false),
    UPDATE_RESERVATION(3, "Update a Reservation", false),
    DELETE_RESERVATION(4, "Delete a Reservation", false);

    private int value;
    private String message;
    private boolean hidden;

    private MainMenuOption(int value, String message, boolean hidden) {
        this.value = value;
        this.message = message;
        this.hidden = hidden;
    }

    public static MainMenuOption fromValue(int value) {
        for (MainMenuOption option : MainMenuOption.values()) {
            if (option.getValue() == value) {
                return option;
            }
        }
        return EXIT;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public boolean isHidden() {
        return hidden;
    }
}
