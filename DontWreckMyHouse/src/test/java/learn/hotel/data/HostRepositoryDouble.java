package learn.hotel.data;

import learn.hotel.models.Host;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostRepositoryDouble implements HostRepository {

    public final static Host host = new Host("1", "Chong", "sammi@hotmail.com", "0918233",
            "test", "London", "OH", "SE8", BigDecimal.valueOf(10), BigDecimal.valueOf(15));
    private final ArrayList<Host> hosts = new ArrayList<>();

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
