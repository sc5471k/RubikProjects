package learn.hotel.data;

import learn.hotel.models.Host;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostRepositoryDouble implements HostRepository {

    private final ArrayList<Host> hosts = new ArrayList<>();

    public HostRepositoryDouble() {
        hosts.add(new Host("1", "Chong", "sammi@hotmail.com", "0918233",
                "test", "London", "OH", "SE8", BigDecimal.valueOf(10), BigDecimal.valueOf(15)));
        hosts.add(new Host("2", "Second", "second@hotmail.com", "0914394",
                "secondTest", "London", "CA", "SE10", BigDecimal.valueOf(10), BigDecimal.valueOf(15)));
    }

    @Override
    public List<Host> findAll() {
        return new ArrayList<>(hosts);
    }

    @Override
    public Host findByID(String id) {
        return findAll().stream()
                .filter(i -> i.getHostID().equals(id))
                .findFirst()
                .orElse(null);
    }
}
