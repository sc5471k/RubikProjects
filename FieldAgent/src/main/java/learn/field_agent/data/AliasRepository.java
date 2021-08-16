package learn.field_agent.data;

import learn.field_agent.models.Alias;

import java.util.List;

public interface AliasRepository {

    List<Alias> findAll();
    Alias findAgentByAlias(int aliasID);
    Alias add(Alias alias);
    boolean update(Alias alias);
    boolean deleteById(int aliasID);
}
