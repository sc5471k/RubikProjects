package learn.solarfarm.UI;

import learn.solarfarm.data.DataAccessException;
import learn.solarfarm.domain.PanelResult;
import learn.solarfarm.domain.PanelService;
import learn.solarfarm.model.Panel;
import java.util.List;

public class Controller {
    private final View view;
    private final PanelService service;
    private final TextIO io;

    public Controller(View view, PanelService service, TextIO io) {
        this.view = view;
        this.service = service;
        this.io = io;
    }

    public void run() {
        //run app, if error display
        view.displayHeader("Welcome to Solar Farm");
        try {
            runApp();
        } catch (DataAccessException ex) {
            view.displayErrors(List.of(ex.getMessage()));
        }
    }

    private void runApp() throws DataAccessException {

        for(int option = view.menuOptions();
            option > 0;
            option = view.menuOptions()) {

            switch(option) {
                case 1:
                    viewPanels();
                    break;
                case 2:
                    addPanel();
                    break;
                case 3:
                    updatePanel();
                    break;
                case 4:
                    deletePanel();
                    break;
            }
        }
    }

    private void viewPanels() throws DataAccessException {
        List<Panel> panels = getPanelsSection();
        view.displayPanels(panels);

    }

    private List<Panel> getPanelsSection() throws DataAccessException {
        view.displayHeader("View Panels by Section");
        String section = io.readString("Section Name: ");
        return service.findBySection(section);
    }

    private List<Panel> getPanelsDelete() throws DataAccessException {
        view.displayHeader("Remove a Panel");
        return service.findAll();
    }

    private Panel getPanelsUpdate() throws DataAccessException {
        view.displayHeader("Update a Panel");
        String section = io.readString("Section Name: ");
        int row = io.readInt("Row: ");
        int col = io.readInt("Col: ");
        return service.findByPanel(section, row, col);
    }

    private void addPanel() throws DataAccessException {
        Panel p = view.createPanel();
        PanelResult result = service.add(p);
        if (result.isSuccess()) {
            view.displayMessage("Panel " + result.getPanel().getSection() + "-" + result.getPanel().getRow() + "-" +
                    result.getPanel().getColumn() +" added.");
        } else {
            view.displayErrors(result.getErrorMessages());
        }
    }

    private void updatePanel() throws DataAccessException {
        Panel panel = getPanelsUpdate();
        if (panel == null) {
            view.displayMessage("Panel not found.");
            return;
        }
        panel = view.editPanel(panel);
        PanelResult result = service.update(panel);
        if (result.isSuccess()) {
            io.println("[Success]");
            view.displayMessage(String.format("Panel %s-%d-%d was updated.", panel.getSection(), panel.getRow(), panel.getColumn()));
        } else {
            view.displayErrors(result.getErrorMessages());
        }
    }

    private void deletePanel() throws DataAccessException {
        List<Panel> panels = getPanelsDelete();
        Panel p = view.chooseDelete(panels);
        if (p != null && service.delete(p.getSection(), p.getRow(), p.getColumn()).isSuccess()) {
            io.println("[Success]");
            view.displayMessage(String.format("Panel %s-%d-%d removed.", p.getSection(), p.getRow(), p.getColumn()));
        } else {
            io.println("[Err]");
            view.displayMessage("Panel was not found.");
        }
    }
}

