package learn.solarfarm.domain;

import learn.solarfarm.model.Panel;

import java.util.ArrayList;
import java.util.List;

public class PanelResult {

    private final ArrayList<String> messages = new ArrayList<>();
    private Panel panel;

    public List<String> getErrorMessages() {
        return new ArrayList<>(messages);
    }

    public void addErrorMessage(String message) {
        messages.add(message);
    }

    public boolean isSuccess() {
        // If an error message exists, the operation failed.
        return messages.size() == 0;
    }

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }
}
