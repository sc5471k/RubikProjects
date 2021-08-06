package learn.hotel.data;

import learn.hotel.models.Host;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HostFileRepository implements HostRepository {
    private final String directory;

    public HostFileRepository(@Value("${HostFilePath}") String directory) {
        this.directory = directory;
    }

    @Override
    public List<Host> findAll() {
        ArrayList<Host> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(directory))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 10) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }

    @Override
    public Host findByID(String id) {
        return findAll().stream()
                .filter(i -> i.getHostID().equals(id))
                .findFirst()
                .orElse(null);
    }

    private Host deserialize(String[] fields) {
        Host result = new Host();
        result.setHostID((fields[0]));
        result.setLastName(fields[1]);
        result.setEmail(fields[2]);
        result.setPhone(fields[3]);
        result.setAddress(fields[4]);
        result.setCity(fields[5]);
        result.setState(fields[6]);
        result.setPostalCode(fields[7]);
        result.setStandardRate(new BigDecimal(fields[8]));
        result.setWeekendRate(new BigDecimal(fields[9]));
        return result;
    }
}
