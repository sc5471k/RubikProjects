package learn.field_agent.data.mappers;

import learn.field_agent.models.AliasAgent;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AliasAgentMapper implements RowMapper<AliasAgent> {

    @Override
    public AliasAgent mapRow(ResultSet resultSet, int i) throws SQLException {
        AliasAgent aliasAgent = new AliasAgent();
        aliasAgent.setAliasId(resultSet.getInt("alias_id"));
        aliasAgent.setName(resultSet.getString("name"));
        aliasAgent.setPersona(resultSet.getString("persona"));
        aliasAgent.setAgentId(resultSet.getInt("agent_id"));
        aliasAgent.setFirstName(resultSet.getString("first_name"));
        aliasAgent.setMiddleName(resultSet.getString("middle_name"));
        aliasAgent.setLastName(resultSet.getString("last_name"));
        if (resultSet.getDate("dob") != null) {
            aliasAgent.setDob(resultSet.getDate("dob").toLocalDate());
        }
        aliasAgent.setHeightInInches(resultSet.getInt("height_in_inches"));
        return aliasAgent;
    }
}
