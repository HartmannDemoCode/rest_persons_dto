package facades;

import entities.Person;
import facades.IPersonFacade;
import utils.EMF_Creator;
import exceptions.PersonNotFoundException;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private Person p1;
    private Person p2;

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
       emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST,Strategy.DROP_AND_CREATE);
       facade = PersonFacade.getPersonFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the script below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            p1 = new Person("Henning", "Bonnetsen","536436");
            p2 = new Person("Helle", "Harsk","213243");
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
    public void addPersonTest(){
        Person p = facade.addPerson("Janni",  "Spice",  "123456");
        assertTrue(p.getId() != null);
    }
        
        @Test
        public void deletePersonTest(){
        try {
            Person p = facade.deletePerson(p1.getId());
            assertThat(p, Matchers.hasProperty("id"));
        } catch (PersonNotFoundException ex) {
            ex.printStackTrace();
        }

        }

        @Test
        public void getPersonTest(){
        try {
            Person p = facade.getPerson(p1.getId());
            assertTrue(p.getFirstName().equals("Henning"));
        } catch (PersonNotFoundException ex) {
            ex.printStackTrace();
        }
        }
//        
//
        @Test
        public void getAllPersonsTest(){
            List<Person> persons = facade.getAllPersons();
            assertTrue(persons.size() == 2);
        }
//        
//
        @Test
        public void editPersonTest(){
        try {
            p1.setFirstName("Henrietta");
            Person changed = facade.editPerson(p1);
            System.out.println("Firstname"+changed.getFirstName());
            assertTrue(Objects.equals(changed.getId(), p1.getId()) && changed.getFirstName().equals("Henrietta"));
        } catch (PersonNotFoundException ex) {
            ex.printStackTrace();
        }
        }
        
    
}
