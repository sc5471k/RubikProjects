package learn.field_agent.data;

import learn.field_agent.models.SecurityClearance;

import java.sql.SQLException;
import java.util.List;

public interface SecurityClearanceRepository {
    List<SecurityClearance> findAll();

    SecurityClearance findById(int securityClearanceId);

    SecurityClearance add(SecurityClearance securityClearance);

    boolean update(SecurityClearance securityClearance);

    boolean deleteById(int securityID) throws SQLException;
}
