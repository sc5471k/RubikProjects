package learn.hotel.domain;

import learn.hotel.data.HostRepository;
import learn.hotel.models.Host;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HostService {

    private final HostRepository repo;

    public HostService(HostRepository repo) {
        this.repo = repo;
    }

    public List<Host> findAll() {
        List<Host> hosts = repo.findAll();
        return hosts;
    }

    public Host findByID(String id) {
        Host host = repo.findByID(id);
        return host;
    }
}
