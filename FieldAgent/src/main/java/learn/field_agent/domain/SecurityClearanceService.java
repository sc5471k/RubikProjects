package learn.field_agent.domain;

import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.SecurityClearance;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class SecurityClearanceService {
    private final SecurityClearanceRepository repository;

    public SecurityClearanceService(SecurityClearanceRepository repository) {
        this.repository = repository;
    }

    public List<SecurityClearance> findAll() {
        return repository.findAll();
    }

    public SecurityClearance findById(int securityID) {
        return repository.findById(securityID);
    }

    public Result<SecurityClearance> add(SecurityClearance securityClearance) {
        Result<SecurityClearance> result = validate(securityClearance);
        if (!result.isSuccess()) {
            return result;
        }

        if (securityClearance.getSecurityClearanceId() != 0) {
            result.addMessage("securityID cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        securityClearance = repository.add(securityClearance);
        result.setPayload(securityClearance);
        return result;
    }

    public Result<SecurityClearance> update(SecurityClearance securityClearance) {
        Result<SecurityClearance> result = validate(securityClearance);
        if (!result.isSuccess()) {
            return result;
        }

        if (securityClearance.getSecurityClearanceId() != 0) {
            result.addMessage("securityID cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        if (!repository.update(securityClearance)) {
            String msg = String.format("securityID: %s, not found", securityClearance.getSecurityClearanceId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }
        return result;
    }

    public boolean deleteById(int securityID) throws SQLException {
        return repository.deleteById(securityID);
    }

    private Result<SecurityClearance> validate(SecurityClearance securityClearance) {
        Result<SecurityClearance> result = new Result<>();
        if (securityClearance == null) {
            result.addMessage("security clearance cannot be null", ResultType.INVALID);
            return result;
        }

        // Security clearance name is required.
        if(Validations.isNullOrBlank(securityClearance.getName())) {
            result.addMessage("name is required", ResultType.INVALID);
        }

        // Name cannot be duplicated.
        List<SecurityClearance> securityClearanceList = findAll();
        for(SecurityClearance s : securityClearanceList) {
            if(s.getName().equals(securityClearance.getName())) {
                result.addMessage("name cannot be duplicated", ResultType.INVALID);
            }
        }
        return result;
    }
}
