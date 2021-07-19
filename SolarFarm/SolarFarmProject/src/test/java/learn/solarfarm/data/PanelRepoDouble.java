package learn.solarfarm.data;

import learn.solarfarm.model.Material;
import learn.solarfarm.model.Panel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PanelRepoDouble implements PanelRepo{

    private ArrayList<Panel> panels = new ArrayList<Panel>();

    public PanelRepoDouble() {
        panels.add(new Panel(1, "Main", 1, 1, 2012, Material.AMORPHOUS_SILICON, false));
        panels.add(new Panel(2, "Main", 2, 1, 2014, Material.MONOCRYSTALLINE_SILICON, false));
        panels.add(new Panel(3, "Main", 3, 1, 2020, Material.MULTICRYSTALLINE_SILICON, true));
    }

    @Override
    public List<Panel> findAll() throws DataAccessException {
        return new ArrayList<>(panels);
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
        return null;
    }

    @Override
    public boolean update(Panel panel) throws DataAccessException {
        return false;
    }

    @Override
    public boolean delete(String section, int row, int col) throws DataAccessException {
        return false;
    }
}