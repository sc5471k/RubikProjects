package learn.hotel.domain;

import learn.hotel.data.HostRepository;
import learn.hotel.models.Host;
import learn.hotel.models.Reservation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class HostService {

    private final HostRepository repo;

    public HostService(HostRepository repo) {
        this.repo = repo;
    }

    public List<Host> findAll() {
        return repo.findAll();
    }

    public Host findByID(String id) {
        return repo.findByID(id);
    }

    public BigDecimal calculateTotal(Host host, LocalDate startDate, LocalDate endDate) {
        BigDecimal standardRate = host.getStandardRate();
        BigDecimal weekendRate = host.getWeekendRate();

        int weekdayCount = 0;
        int weekendCount = 0;

        for (LocalDate current = startDate; current.compareTo(endDate) <= 0; current = current.plusDays(1)) {
            if (current.getDayOfWeek() == DayOfWeek.SATURDAY || current.getDayOfWeek() == DayOfWeek.SUNDAY) {
                weekendCount++;
            } else {
                weekdayCount++;
            }
        }

        return new BigDecimal(weekendCount).multiply(weekendRate).add(new BigDecimal(weekdayCount).multiply(standardRate));
    }

    public Result<Reservation> validateHostExistence(Reservation reservation, Result<Reservation> result) {
        //The host must already exist in the "database". Cannot be created.
        Host host = repo.findByID(reservation.getHostID());

        if (!host.getHostID().equals(reservation.getHostID())) {
            result.addErrorMessage("Host must already exist");
        }
        return result;
    }
}
