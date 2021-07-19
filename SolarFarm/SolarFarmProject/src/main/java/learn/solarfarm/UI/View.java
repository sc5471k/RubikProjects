package learn.solarfarm.UI;

import learn.solarfarm.model.Material;
import learn.solarfarm.model.Panel;

import java.util.List;
import java.util.Scanner;

public class View {

    private final TextIO io;
    private final Scanner console = new Scanner(System.in);

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

    public Panel chooseDelete(List<Panel> panels) {
        String section = io.readString("Section: ");
        int row = io.readInt("Row: ");
        int col = io.readInt("Column: ");
        Panel result = null;
        if (panels.size() > 0) {
            for (Panel p : panels) {
                if (p.getSection().equals(section) && p.getRow() == row && p.getColumn() == col) {
                    result = p;
                    break;
                }
            }
        }
        return result;
    }

    public Panel editPanel(Panel p) {
        displayHeader("Update");
        io.println("Editing " + p.getSection() + "-" + p.getRow() + "-" + p.getColumn());

        io.println("Material (" + p.getMaterial() + "): ");
        Material material = readMaterial();
        p.setMaterial(material);

        int year = io.readInt("Year Installed (" + p.getYearInstalled() + "): ");
        p.setYearInstalled(year);

        String tracked = io.readString("Tracked (" + (p.isTracking() ? "y" : "n") + ") [y/n]: ");
        if (tracked.trim().length() > 0) {
            p.setTracking(tracked.equalsIgnoreCase("y"));
        }
        return p;
    }

    public void displayErrors(List<String> errors) {
        //for every error in errors print error

        displayHeader("Errors:");
        for (String error : errors) {
            io.println(error);
        }
    }

    public void displayPanels(List<Panel> panels) {
        if (panels.size() == 0) {
            displayHeader("No panels found.");
        } else {
            displayHeader("Panels:");
            for (Panel p : panels) {
                io.println("Panels in " + p.getSection());
                System.out.printf("%4s  %4s   %-4s  %-21s   %4s%n", "Row", "Col", "Year", "Material", "Tracking");
                System.out.printf("%4s  %4s   %-4s  %-21s   %4s%n", p.getRow(), p.getColumn(), p.getYearInstalled(), p.getMaterial(), p.isTracking());
            }
        }
    }

    public void displayMessage(String message) {
        io.println("");
        io.println(message);
    }

    public Panel createPanel() {
        displayHeader("Add a Panel");
        Panel result = new Panel();
        result.setSection(io.readString("Section: "));
        result.setRow(io.readInt("Row: "));
        result.setColumn(io.readInt("Column: "));
        result.setYearInstalled(io.readInt("Year Installed: "));
        io.println("Material: ");
        result.setMaterial(readMaterial());
        result.setTracking(io.readBoolean("Tracked [y/n]: "));
        return result;
    }

    private String readString(String message) {
        System.out.print(message);
        return console.nextLine();
    }

    private String readRequiredString(String message) {
        String result;
        do {
            result = readString(message);
            if (result.trim().length() == 0) {
                System.out.println("Value is required.");
            }
        } while (result.trim().length() == 0);
        return result;
    }

    private int readInt(String message) {
        String input = null;
        int result = 0;
        boolean isValid = false;
        do {
            try {
                input = readRequiredString(message);
                result = Integer.parseInt(input);
                isValid = true;
            } catch (NumberFormatException ex) {
                System.out.printf("%s is not a valid number.%n", input);
            }
        } while (!isValid);

        return result;
    }

    private int readInt(String message, int min, int max) {
        int result;
        do {
            result = readInt(message);
            if (result < min || result > max) {
                System.out.printf("Value must be between %s and %s.%n", min, max);
            }
        } while (result < min || result > max);
        return result;
    }

    private Material readMaterial() {
        int index = 1;
        for (Material material : Material.values()) {
            System.out.printf("%s. %s%n", index++, material);
        }
        index--;
        String msg = String.format("Select Material [1-%s]:", index);
        return Material.values()[readInt(msg, 1, index) - 1];
    }
}
