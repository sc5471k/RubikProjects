package learn.field_agent.data;

import com.mysql.cj.jdbc.MysqlDataSource;
import learn.field_agent.data.mappers.AgencyAgentMapper;
import learn.field_agent.data.mappers.LocationMapper;
import learn.field_agent.models.AgencyAgent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
public class AgencyAgentJdbcTemplateRepository implements AgencyAgentRepository {

    private final JdbcTemplate jdbcTemplate;

    public AgencyAgentJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 1. Dangerous initialization during construction
    private DataSource dataSource = initDataSource();

    private DataSource initDataSource() {
        MysqlDataSource result = new MysqlDataSource();
        // 2. connection string is:
        // [db-tech]:[db-vendor]://[host]:[port]/[database-name]
        result.setUrl("jdbc:mysql://localhost:3366/field_agent_test");
        // 3. username
        result.setUser("root");
        // 4. password
        result.setPassword("root");
        return result;
    }

    @Override
    public boolean add(AgencyAgent agencyAgent) {

        final String sql = "insert into agency_agent (agency_id, agent_id, identifier, security_clearance_id, "
                + "activation_date, is_active) values "
                + "(?,?,?,?,?,?);";

        return jdbcTemplate.update(sql,
                agencyAgent.getAgencyId(),
                agencyAgent.getAgent().getAgentId(),
                agencyAgent.getIdentifier(),
                agencyAgent.getSecurityClearance().getSecurityClearanceId(),
                agencyAgent.getActivationDate(),
                agencyAgent.isActive()) > 0;
    }

    @Override
    public boolean update(AgencyAgent agencyAgent) {

        final String sql = "update agency_agent set "
                + "identifier = ?, "
                + "security_clearance_id = ?, "
                + "activation_date = ?, "
                + "is_active = ? "
                + "where agency_id = ? and agent_id = ?;";

        return jdbcTemplate.update(sql,
                agencyAgent.getIdentifier(),
                agencyAgent.getSecurityClearance().getSecurityClearanceId(),
                agencyAgent.getActivationDate(),
                agencyAgent.isActive(),
                agencyAgent.getAgencyId(),
                agencyAgent.getAgent().getAgentId()) > 0;

    }

    @Override
    public boolean deleteByKey(int agencyId, int agentId) {

        final String sql = "delete from agency_agent "
                + "where agency_id = ? and agent_id = ?;";

        return jdbcTemplate.update(sql, agencyId, agentId) > 0;
    }

//    public boolean checkSecurityIDExistence(int securityID) {
//        boolean exist = false;
//        int count = 0;
//        final String sql = "select agency_id, identifier, security_clearance_id, activation_date, is_active " +
//                "from agency_agent " +
//                "where security_clearance_id = '" + securityID + "';";
//
//        try (Connection conn = dataSource.getConnection();
//             Statement statement = conn.createStatement();
//             ResultSet rs = statement.executeQuery(sql)) {
//
//            while (rs.next()) {
//                count++;
//            }
//
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//
////        AgencyAgent agencyAgent = jdbcTemplate.query(sql, new AgencyAgentMapper(), securityID).stream()
////                .findFirst()
////                .orElse(null);
//
//        if(count > 0) {
//            exist = true;
//        }
//        return exist;
//    }

    public boolean checkSecurityIDExistence(int securityID) throws SQLException {
        final String sql = "select agency_id, identifier, security_clearance_id, activation_date, is_active " +
                "from agency_agent " +
                "where security_clearance_id = ?;";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, securityID);

            try(ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
            return false;
    }
}
