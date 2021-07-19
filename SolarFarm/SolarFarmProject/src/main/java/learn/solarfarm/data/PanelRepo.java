package learn.solarfarm.data;

import learn.solarfarm.model.Panel;

import java.util.List;

public interface PanelRepo {

    //read
    List<Panel> findAll() throws DataAccessException;
    List<Panel> findSection(String section) throws DataAccessException;
    Panel findPanel(String section, int row, int col) throws DataAccessException;

    //create
    Panel add(Panel panel) throws DataAccessException;

    //update
    boolean update(Panel panel) throws DataAccessException;

    //delete
    boolean delete(String section, int row, int col) throws DataAccessException;
}
