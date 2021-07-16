package learn.solarfarm.UI;

import learn.solarfarm.data.DataAccessException;
import learn.solarfarm.model.Panel;

import java.util.List;

public class Controller {
    private final View view;

    public Controller(View view) {
        this.view = view;
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
                    //view
                    break;
                case 2:
                    //add
                    break;
                case 3:
                    //update
                    break;
                case 4:
                    //delete
                    break;
            }
        }
    }

    private List<Panel> getPanels(String sections) throws DataAccessException {


    }

    private void ViewPanels() throws DataAccessException {

    }
}

