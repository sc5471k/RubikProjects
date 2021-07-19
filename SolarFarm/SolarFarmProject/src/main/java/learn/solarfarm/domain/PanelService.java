package learn.solarfarm.domain;

import learn.solarfarm.data.DataAccessException;
import learn.solarfarm.data.PanelRepo;
import learn.solarfarm.model.Material;
import learn.solarfarm.model.Panel;

import java.util.List;

public class PanelService {

    private final PanelRepo repo;

    public PanelService(PanelRepo repo) {
        this.repo = repo;
    }

    public List<Panel> findAll() throws DataAccessException {
        return repo.findAll();
    }

    public List<Panel> findBySection(String section) throws DataAccessException {
        return repo.findSection(section);
    }

    public Panel findByPanel(String section, int row, int col) throws DataAccessException {
        return repo.findPanel(section, row, col);
    }

    public PanelResult add(Panel panel) throws DataAccessException {
        PanelResult result = validate(panel);

        if (panel.getPanelID() > 0) {
            result.addErrorMessage("Panel `ID` should not be set.");
        }

        if (result.isSuccess()) {
            panel = repo.add(panel);
            result.setPanel(panel);
        }
        return result;
    }

    public PanelResult update(Panel panel) throws DataAccessException {
        PanelResult result = validate(panel);

        if (panel.getPanelID() <= 0) {
            result.addErrorMessage("Panel `id` is required.");
        }

        if (result.isSuccess()) {
            if (repo.update(panel)) {
                result.setPanel(panel);
            } else {
                String message = String.format("Panel id %s was not found.", panel.getPanelID());
                result.addErrorMessage(message);
            }
        }
        return result;
    }

    public PanelResult delete(String section, int row, int col) throws DataAccessException {
        PanelResult result = new PanelResult();
        if (!repo.delete(section, row, col)) {
            String message = String.format("Panel %s-%d-%d was not found.", section, row, col);
            result.addErrorMessage(message);
        }
        return result;
    }

    private PanelResult validate(Panel panel) {
        PanelResult result = new PanelResult();

        if (panel == null) {
            result.addErrorMessage("Panel cannot be null.");
            return result;
        }

        if (panel.getSection() == null || panel.getSection().isBlank()) {
            result.addErrorMessage("Section is required.");
        }

        if (panel.getRow() < 0) {
            result.addErrorMessage("Row value must be positive.");
        }

        if (panel.getRow() > 250) {
            result.addErrorMessage("Row value must be less than 250.");
        }

        if (panel.getColumn() < 0) {
            result.addErrorMessage("Column value must be positive.");
        }

        if (panel.getColumn() > 250) {
            result.addErrorMessage("Column value must be less than 250.");
        }

        if (panel.getYearInstalled() > 2021 || panel.getYearInstalled() < 1900 ) {
            result.addErrorMessage("Year must be in the past.");
        }

        if (panel.getMaterial() == null) {
            result.addErrorMessage("Material is required.");
        }
        return result;
    }

//set tracking
//section+row+column must not be duplicated
}
