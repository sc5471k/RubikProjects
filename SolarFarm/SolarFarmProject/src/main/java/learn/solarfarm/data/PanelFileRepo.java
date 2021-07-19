package learn.solarfarm.data;

import learn.solarfarm.model.Material;
import learn.solarfarm.model.Panel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PanelFileRepo implements PanelRepo {

    private final String filePath;
    private final String delimiter = "~";

    public PanelFileRepo(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Panel> findAll() throws DataAccessException {
        ArrayList<Panel> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                Panel p = lineToMemory(line);
                if (p != null) {
                    result.add(p);
                }
            }
        } catch (FileNotFoundException ex) {
            //nothing, can make file
        } catch (IOException ex) {
            throw new DataAccessException("Could not open the file path: " + filePath, ex);
        }
        return result;
    }

    @Override
    public List<Panel> findSection(String section) throws DataAccessException {
        ArrayList<Panel> result = new ArrayList<>();
        for (Panel panel : findAll()) {
            if (panel.getSection().equals(section)) {
                result.add(panel);
            }
        }
        return result;
    }

    @Override
    public Panel findPanel(String section, int row, int col) throws DataAccessException {
        for (Panel panel : findAll()) {
            if (panel.getSection().equals(section) && panel.getRow() == row && panel.getColumn() == col) {
                return panel;
            }
        }
        return null;
    }

    @Override
    public Panel add(Panel panel) throws DataAccessException {
        List<Panel> all = findAll();
        int nextId = getNextId(all);
        panel.setPanelID(nextId);
        all.add(panel);
        writeToFile(all);
        return panel;
    }

    @Override
    public boolean update(Panel panel) throws DataAccessException {
        List<Panel> all = findAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getPanelID() == panel.getPanelID()) {
                all.set(i, panel);
                writeToFile(all);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(String section, int row, int col) throws DataAccessException {
        List<Panel> all = findAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getSection().equals(section) && all.get(i).getRow() == row
                    && all.get(i).getColumn() == col) {
                all.remove(i);
                writeToFile(all);
                return true;
            }
        }
        return false;
    }

    private String cleanField(String field) {
        // If the file delimiter, a carriage return, or a newline was written to the file,
        // it would ruin our ability to read the memory.
        // Here, we ensure those characters don't end up in the file.
        return field.replace(delimiter, "")
                .replace("/r", "")
                .replace("/n", "");
    }

    //de-serialisation
    private Panel lineToMemory(String line) {
        String[] fields = line.split(delimiter, -1);

        if (fields.length == 7) {
            Panel panel = new Panel();
            panel.setPanelID(Integer.parseInt(fields[0]));
            panel.setSection(fields[1]);
            panel.setRow(Integer.parseInt(fields[2]));
            panel.setColumn(Integer.parseInt(fields[3]));
            panel.setYearInstalled(Integer.parseInt(fields[4]));
            panel.setMaterial(Material.valueOf(fields[5]));
            panel.setTracking("true".equals(fields[6]));
            return panel;
        }
        return null;
    }

    //serialisation
    private String memoryToLine(Panel panel) {
        return panel.getPanelID() + delimiter +
                cleanField(panel.getSection()) + delimiter +
                panel.getRow() + delimiter +
                panel.getColumn() + delimiter +
                panel.getYearInstalled() + delimiter +
                panel.getMaterial() + delimiter +
                panel.isTracking();
    }

    private int getNextId(List<Panel> panels) {
        int maxId = 0;
        for (Panel panel : panels) {
            if (maxId < panel.getPanelID()) {
                maxId = panel.getPanelID();
            }
        }
        return maxId + 1;
    }

    private void writeToFile(List<Panel> panels) throws DataAccessException {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            for (Panel panel : panels) {
                writer.println(memoryToLine(panel));
            }
        } catch (IOException ex) {
            throw new DataAccessException("Could not write to file path: " + filePath, ex);
        }
    }
}
