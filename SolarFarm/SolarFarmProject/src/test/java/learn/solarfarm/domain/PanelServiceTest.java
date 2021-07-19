package learn.solarfarm.domain;

import learn.solarfarm.data.DataAccessException;
import learn.solarfarm.data.PanelRepo;
import learn.solarfarm.data.PanelRepoDouble;
import learn.solarfarm.model.Material;
import learn.solarfarm.model.Panel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PanelServiceTest {

    PanelService service;

    @BeforeEach
    void setup() {
        PanelRepoDouble repo = new PanelRepoDouble();
        service = new PanelService(repo);
    }

    @Test
    void sectionShouldNotBeNull() throws DataAccessException {
        Panel panel = new Panel();
        panel.setRow(1);
        panel.setColumn(1);
        panel.setYearInstalled(2020);
        panel.setMaterial(Material.CADMIUM_TELLURIDE);
        panel.setTracking(false);

        PanelResult result = service.add(panel);
        assertFalse(result.isSuccess());
    }

    @Test
    void materialShouldNotBeNull() throws DataAccessException {
        Panel panel = new Panel();
        panel.setSection("New");
        panel.setRow(1);
        panel.setColumn(1);
        panel.setYearInstalled(2020);
        panel.setTracking(false);

        PanelResult result = service.add(panel);
        assertFalse(result.isSuccess());
    }

    @Test
    void rowShouldBePositive() throws DataAccessException {
        Panel panel = new Panel();
        panel.setSection("New");
        panel.setRow(-1);
        panel.setColumn(1);
        panel.setYearInstalled(2020);
        panel.setMaterial(Material.CADMIUM_TELLURIDE);
        panel.setTracking(false);

        PanelResult result = service.add(panel);
        assertFalse(result.isSuccess());
    }

    @Test
    void colShouldBePositive() throws DataAccessException {
        Panel panel = new Panel();
        panel.setSection("New");
        panel.setRow(1);
        panel.setColumn(-1);
        panel.setYearInstalled(2020);
        panel.setMaterial(Material.CADMIUM_TELLURIDE);
        panel.setTracking(false);

        PanelResult result = service.add(panel);
        assertFalse(result.isSuccess());
    }

    @Test
    void rowShouldBeLessThan250() throws DataAccessException {
        Panel panel = new Panel();
        panel.setSection("New");
        panel.setRow(300);
        panel.setColumn(1);
        panel.setYearInstalled(2020);
        panel.setMaterial(Material.CADMIUM_TELLURIDE);
        panel.setTracking(false);

        PanelResult result = service.add(panel);
        assertFalse(result.isSuccess());
    }

    @Test
    void colShouldBeLessThan250() throws DataAccessException {
        Panel panel = new Panel();
        panel.setSection("New");
        panel.setRow(1);
        panel.setColumn(400);
        panel.setYearInstalled(2020);
        panel.setMaterial(Material.CADMIUM_TELLURIDE);
        panel.setTracking(false);

        PanelResult result = service.add(panel);
        assertFalse(result.isSuccess());
    }

    @Test
    void yearShouldBeInPast() throws DataAccessException {
        Panel panel = new Panel();
        panel.setSection("New");
        panel.setRow(1);
        panel.setColumn(1);
        panel.setYearInstalled(3000);
        panel.setMaterial(Material.CADMIUM_TELLURIDE);
        panel.setTracking(false);

        PanelResult result = service.add(panel);
        assertFalse(result.isSuccess());
    }
}