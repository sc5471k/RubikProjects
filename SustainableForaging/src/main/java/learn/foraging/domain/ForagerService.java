package learn.foraging.domain;

import learn.foraging.data.DataException;
import learn.foraging.data.ForagerRepository;
import learn.foraging.models.Forage;
import learn.foraging.models.Forager;
import learn.foraging.models.Item;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ForagerService {

    private final ForagerRepository repository;

    public ForagerService(ForagerRepository repository) {
        this.repository = repository;
    }

    public List<Forager> findByState(String stateAbbr) {
        return repository.findByState(stateAbbr);
    }

    public List<Forager> findAll() {
        List<Forager> result = repository.findAll();
        return result;
    }
    public List<Forager> findByLastName(String prefix) {
        return repository.findAll().stream()
                .filter(i -> i.getLastName().startsWith(prefix))
                .collect(Collectors.toList());
    }

    public Result<Forager> add(Forager forager) throws DataException {
        Result<Forager> result = validate(forager);
        if (!result.isSuccess()) {
            return result;
        }

        result.setPayload(repository.add(forager));

        return result;
    }

    private Result<Forager> validate(Forager forager) {
        //The combination of first name, last name, and state cannot be duplicated.
        Result<Forager> result = validateNulls(forager);
        if (!result.isSuccess()) {
            return result;
        }
        return result;
    }

    private Result<Forager> validateNulls(Forager forager) {
        Result<Forager> result = new Result<>();

        if (forager == null) {
            result.addErrorMessage("Forager must not be null.");
            return result;
        }

        if (forager.getFirstName() == null || forager.getFirstName().isBlank()) {
            result.addErrorMessage("Forager first name is required.");
        }

        if (forager.getLastName() == null || forager.getLastName().isBlank()) {
            result.addErrorMessage("Forager last name is required.");
        }

        if (forager.getState() == null || forager.getState().isBlank()) {
            result.addErrorMessage("State is required.");
        }
        return result;
        //else if (repository.findAll().stream()
//                .anyMatch(i -> i.getState().equalsIgnoreCase(forager.getState()))) {
//            result.addErrorMessage(String.format("Forager state '%s' is a duplicate.", forager.getState()));
//        }
    }
}
