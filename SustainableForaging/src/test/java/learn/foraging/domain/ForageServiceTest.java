package learn.foraging.domain;

import learn.foraging.data.DataException;
import learn.foraging.data.ForageRepositoryDouble;
import learn.foraging.data.ForagerRepositoryDouble;
import learn.foraging.data.ItemRepositoryDouble;
import learn.foraging.models.Category;
import learn.foraging.models.Forage;
import learn.foraging.models.Forager;
import learn.foraging.models.Item;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ForageServiceTest {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    ForageService service = new ForageService(
            new ForageRepositoryDouble(),
            new ForagerRepositoryDouble(),
            new ItemRepositoryDouble());

    @Test
    void shouldAdd() throws DataException {
        Forage forage = new Forage();
        forage.setDate(LocalDate.now());
        forage.setForager(ForagerRepositoryDouble.FORAGER);
        forage.setItem(ItemRepositoryDouble.ITEM);
        forage.setKilograms(0.5);

        Result<Forage> result = service.add(forage);
        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        assertEquals(36, result.getPayload().getId().length());
    }

    @Test
    void noDuplication() throws DataException {
        Forage forage = new Forage();
        forage.setDate(LocalDate.now());
        forage.setForager(ForagerRepositoryDouble.FORAGER);
        forage.setItem(ItemRepositoryDouble.ITEM);
        forage.setKilograms(0.5);
        Result<Forage> result = service.add(forage);

        Forage forage2 = new Forage();
        forage2.setDate(LocalDate.now());
        forage2.setForager(ForagerRepositoryDouble.FORAGER);
        forage2.setItem(ItemRepositoryDouble.ITEM);
        forage2.setKilograms(0.5);
        result = service.add(forage2);

        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddWhenForagerNotFound() throws DataException {

        Forager forager = new Forager();
        forager.setId("30816379-188d-4552-913f-9a48405e8c08");
        forager.setFirstName("Ermengarde");
        forager.setLastName("Sansom");
        forager.setState("NM");

        Forage forage = new Forage();
        forage.setDate(LocalDate.now());
        forage.setForager(forager);
        forage.setItem(ItemRepositoryDouble.ITEM);
        forage.setKilograms(0.5);

        Result<Forage> result = service.add(forage);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddWhenItemNotFound() throws DataException {

        Item item = new Item(11, "Dandelion", Category.EDIBLE, new BigDecimal("0.05"));

        Forage forage = new Forage();
        forage.setDate(LocalDate.now());
        forage.setForager(ForagerRepositoryDouble.FORAGER);
        forage.setItem(item);
        forage.setKilograms(0.5);

        Result<Forage> result = service.add(forage);
        assertFalse(result.isSuccess());
    }


    @Test
    void countForageItem() {
        LocalDate date = LocalDate.parse("01/01/2020", formatter);
        Map<Item, Long> itemCount = service.countForageItem(date);

        assertNotNull(itemCount);
    }

    @Test
    void valCategoriesPerDay() {
        LocalDate date = LocalDate.parse("01/01/2020", formatter);

        List<Forage> forages = service.findByDate(date);
        List<BigDecimal> total = service.valCategoriesPerDay(forages);

        assertNotNull(total);
    }
}