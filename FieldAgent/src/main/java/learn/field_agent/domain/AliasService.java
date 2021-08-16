package learn.field_agent.domain;

import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AliasService {

    private final AliasRepository repository;

    public AliasService(AliasRepository repository) {
        this.repository = repository;
    }

    public List<Alias> findAll() {
        return repository.findAll();
    }

    public AliasAgent findAgentByAlias(int agentId) {
        return repository.findAgentByAlias(agentId);
    }

    public Result<Alias> add(Alias alias) {
        Result<Alias> result = validate(alias);
        if (!result.isSuccess()) {
            return result;
        }

        if (alias.getAliasID() != 0) {
            result.addMessage("aliasId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        alias = repository.add(alias);
        result.setPayload(alias);
        return result;
    }

    public Result<Alias> update(Alias alias) {
        Result<Alias> result = validate(alias);
        if (!result.isSuccess()) {
            return result;
        }

        if (alias.getAliasID() <= 0) {
            result.addMessage("aliasId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!repository.update(alias)) {
            String msg = String.format("aliasId: %s, not found", alias.getAliasID());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int aliasId) {
        return repository.deleteById(aliasId);
    }

    private Result<Alias> validate(Alias alias) {
        Result<Alias> result = new Result<>();
        if (alias == null) {
            result.addMessage("alias cannot be null", ResultType.INVALID);
            return result;
        }

        //Name is required.
        if(Validations.isNullOrBlank(alias.getName())) {
            result.addMessage("Name is required", ResultType.INVALID);
        }

        //Persona is not required unless a name is duplicated. The persona differentiates between duplicate names.
        List<Alias> aliasList = findAll();
        for(Alias a : aliasList) {
            if(a.getName().equals(alias.getName())) {
                if(Validations.isNullOrBlank(alias.getPersona()))
                result.addMessage("Name is duplicated, persona is required.", ResultType.INVALID);
            }
        }
        return result;
    }
}
