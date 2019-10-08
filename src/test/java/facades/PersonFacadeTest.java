package facades;

import entities.Address;
import entities.Person;
import entities.dto.PersonDTO;
import entities.dto.PersonsDTO;
import exceptions.AddressNotFoundException;
import utils.EMF_Creator;
import exceptions.PersonNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static IPersonFacade facade;
    private Person p1, p2;
    private Address a1, a2;

    public PersonFacadeTest() {
    }

    //@BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/startcode_test",
                "dev",
                "ax2",
                EMF_Creator.Strategy.CREATE);
        facade = PersonFacade.getPersonFacade(emf);
    }

    /*   **** HINT **** 
        A better way to handle configuration values, compared to the UNUSED example above, is to store those values
        ONE COMMON place accessible from anywhere.
        The file config.properties and the corresponding helper class utils.Settings is added just to do that. 
        See below for how to use these files. This is our RECOMENDED strategy
     */
    @BeforeAll
    public static void setUpClassV2() {
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.DROP_AND_CREATE);
        facade = PersonFacade.getPersonFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            a1 = new Address("Rolighedsvej 5", "2100", "Cph E");
            a2 = new Address("Rolighedsvej 6", "2100", "Cph E");
            p1 = new Person("Henning", "Bonnetsen", "536436", a1);
            p2 = new Person("Helle", "Harsk", "213243", a2);
            em.persist(p1);
            em.persist(p2);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    // TODO: Delete or change this method 
    @Test
    public void testCount() {
        assertEquals(2, facade.count(), "Expects two rows in the database");
    }

    @Test
    public void addPersonTest() {
        Address address = new Address("Rolighedsvej 3", "2100", "Copenhagen East");
        PersonDTO p = facade.addPerson("Janni", "Spice", "123456", "Rolighedsvej 3", "2100", "Copenhagen East");
        assertTrue(p.getId() != 0);
    }

    @Test
    public void deletePersonTest() {
        try {
            PersonDTO p = facade.deletePerson(p1.getId());
            assertThat(p, Matchers.hasProperty("id"));
            assertThrows(AddressNotFoundException.class,()->{facade.findAddress("Rolighedsvej 5", "2100", "Cph E");});
        } catch (PersonNotFoundException ex) {
            ex.printStackTrace();
        }
        
    }

    @Test
    public void getPersonTest() {
        try {
            PersonDTO p = facade.getPerson(p1.getId());
            assertTrue(p.getfName().equals("Henning"));
        } catch (PersonNotFoundException ex) {
            ex.printStackTrace();
        }
    }
//        
//

    @Test
    public void getAllPersonsTest() {
        PersonsDTO persons = facade.getAllPersons();
        assertTrue(persons.getAll().size() == 2);
    }
//        
//

    @Test
    public void editPersonTest() {
        try {
            PersonDTO pdto1 = new PersonDTO(p1);
            pdto1.setfName("Henrietta");
            pdto1.addClub("sailing");
            System.out.println("NOT YET CHANGED:"+pdto1);
            PersonDTO changed = facade.editPerson(pdto1);
            
            System.out.println("CHANGED" + changed);
            assertTrue(changed.getfName().equals("Henrietta"));
            assertTrue(changed.getClubs().contains("sailing"));
        } catch (PersonNotFoundException ex) {
            ex.printStackTrace();
        }
    }

}
