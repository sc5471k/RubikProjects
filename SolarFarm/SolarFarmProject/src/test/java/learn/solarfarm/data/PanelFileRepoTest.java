package learn.solarfarm.data;

import learn.solarfarm.model.Material;
import learn.solarfarm.model.Panel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PanelFileRepoTest {

    static final String SEED_FILE_PATH = "./data/panel-seed.csv";
    static final String TEST_FILE_PATH = "./data/panel-test.csv";

    PanelFileRepo repo = new PanelFileRepo(TEST_FILE_PATH);

    @BeforeEach
        //establish known good state
    void setupTest() throws IOException {
        //seed has known good state, populate test file and restore to good state before each test
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);

        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void findAll() throws DataAccessException {
        List<Panel> actual = repo.findAll();
        assertEquals(28, actual.size());
    }

    @Test
    void findSection() throws DataAccessException {
        List<Panel> panel = repo.findSection("Main");
        assertNotNull(panel);
        assertEquals(12, panel.size());
    }

    @Test
    void findPanel() throws DataAccessException {
        Panel panel = repo.findPanel("Upper Hill", 1, 1);
        assertNotNull(panel);
        assertEquals(2012, panel.getYearInstalled());
        assertFalse(panel.isTracking());

        panel = repo.findPanel("Not real", 4, 4);
        assertNull(panel);
    }

    @Test
    void add() throws DataAccessException{
        Panel panel = new Panel();
        panel.setSection("New");
        panel.setRow(1);
        panel.setColumn(1);
        panel.setYearInstalled(2020);
        panel.setMaterial(Material.CADMIUM_TELLURIDE);
        panel.setTracking(false);

        Panel actual = repo.add(panel);
        assertEquals(29, actual.getPanelID());

        List<Panel> all = repo.findAll();
        assertEquals(29, all.size());

        actual = all.get(28);
        assertEquals(29, actual.getPanelID());
        assertEquals("New", actual.getSection());
        assertEquals(1, actual.getRow());
        assertEquals(1, actual.getColumn());
        assertEquals(2020, actual.getYearInstalled());
        assertEquals(Material.CADMIUM_TELLURIDE, actual.getMaterial());
        assertEquals(false, actual.isTracking());
    }

    @Test
    void update() throws DataAccessException {
        Panel panel = repo.findPanel("Main", 1, 1);
        panel.setMaterial(Material.COPPER_INDIUM_GALLIUM_SELENIDE);
        panel.setYearInstalled(1992);
        panel.setTracking(false);
        assertTrue(repo.update(panel));

        panel = repo.findPanel("Main", 1, 1);
        assertNotNull(panel);
        assertEquals(Material.COPPER_INDIUM_GALLIUM_SELENIDE, panel.getMaterial());
        assertEquals(1992, panel.getYearInstalled());
        assertFalse(panel.isTracking());

        Panel notExist = new Panel();
        notExist.setPanelID(2044);
        assertFalse(repo.update(notExist));
    }

    @Test
    void delete() throws DataAccessException {
        int count = repo.findAll().size();
        assertTrue(repo.delete("Main", 1, 1));
        assertFalse(repo.delete("Fake", 1, 1));
        assertEquals(count -1, repo.findAll().size());
    }
}