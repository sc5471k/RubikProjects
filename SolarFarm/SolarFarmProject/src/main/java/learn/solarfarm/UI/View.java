package learn.solarfarm.UI;

import java.util.List;

public class View {

    private final TextIO io;

    public View(TextIO io) {
        this.io = io;
    }

    public void displayHeader(String message) {
        int length = message.length();
        io.println("");
        io.println(message);
        io.println("=".repeat(length));
    }

    public int menuOptions() {
        displayHeader("Main Menu");
        io.println("0. Exit");
        io.println("1. Find Panels By Section");
        io.println("2. Add a Panel");
        io.println("3. Update a Panel");
        io.println("4. Remove a Panel");
        return io.readInt("Select [0-4]:", 0, 4);
    }

    public void displayErrors(List<String> errors) {
        //for every error in errors print error

        displayHeader("Errors:");
        for(String error : errors) {
            io.println(error);
        }
    }
}
