package learn.solarfarm;

import learn.solarfarm.UI.ConsoleIO;
import learn.solarfarm.UI.Controller;
import learn.solarfarm.UI.View;
import learn.solarfarm.data.PanelFileRepo;
import learn.solarfarm.domain.PanelService;

public class App {
    public static void main(String[] args) {

        PanelFileRepo repo = new PanelFileRepo("./data/panels.txt");
        PanelService service = new PanelService(repo);

        ConsoleIO io = new ConsoleIO();
        View view = new View(io);

        Controller controller = new Controller(view, service, io);
        controller.run();
    }
}
