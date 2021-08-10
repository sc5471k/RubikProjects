package learn.unexplained.data;

import learn.unexplained.models.Encounter;
import learn.unexplained.models.EncounterType;

import java.util.List;

public class EncounterRepositoryDouble implements EncounterRepository {
    @Override
    public List<Encounter> findAll() throws DataAccessException {
        return List.of(new Encounter(2, EncounterType.CREATURE, "1/1/2015", "test description", 1));
    }

    @Override
    public List<Encounter> findByType(Encounter[] testEncounters, EncounterType encounterType) throws DataAccessException {
        return null;
    }

    @Override
    public Encounter add(Encounter encounter) throws DataAccessException {
        return encounter;
    }

    @Override
    public boolean deleteById(int encounterId) throws DataAccessException {
        return encounterId == 2;
    }

    @Override
    public boolean update(Encounter encounter) throws DataAccessException {
        return false;
    }

    @Override
    public Encounter findById(int encounterID) throws DataAccessException {
        return null;
    }
}
