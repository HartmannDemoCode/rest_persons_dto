package facades;

import entities.Person;
import exceptions.PersonNotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import utils.EMF_Creator;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class PersonFacade implements IPersonFacade {

    private static IPersonFacade instance;
    private static EntityManagerFactory emf;
    
    //Private Constructor to ensure Singleton
    private PersonFacade() {}

    public static IPersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    

    @Override
    public Person addPerson(String fName, String lName, String phone) {
        Person p = new Person(fName, lName, phone);
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
        return p;
    }

    @Override
    public Person deletePerson(int id) throws PersonNotFoundException {
       EntityManager em = getEntityManager();
       Person p = em.find(Person.class, id);
       if(p == null)
           throw new PersonNotFoundException("Could not delete, provided id does not exist");
       em.getTransaction().begin();
       em.remove(p);
       em.getTransaction().commit();
       em.close();
       return p;
    }

    @Override
    public Person getPerson(int id) throws PersonNotFoundException {
       EntityManager em = getEntityManager();
       Person p = em.find(Person.class, id);
       if(p == null)
           throw new PersonNotFoundException("No person with provided id found");
       em.close();
       return p;
    }

    @Override
    public List<Person> getAllPersons() {
       EntityManager em = getEntityManager();
       TypedQuery<Person> tq = em.createNamedQuery("Person.getAll", Person.class);
       List<Person> persons = tq.getResultList();
       em.close();
       return persons;
    }

    @Override
    public Person editPerson(Person p) throws PersonNotFoundException {
       EntityManager em = getEntityManager();
       em.getTransaction().begin();
        try {
            em.merge(p);
        } catch (IllegalArgumentException e) {
            throw new PersonNotFoundException("Could not edit person. No person found with id "+p.getId());
        }
       em.getTransaction().commit();
       em.close();
       return p;
    }

    @Override
    public long count() {
       EntityManager em = getEntityManager();
       TypedQuery<Long> tq = em.createQuery("SELECT count(p) FROM Person p", Long.class);
       long count = tq.getSingleResult();
       em.close();
       return count;
    }

    public static void main(String[] args) {
        IPersonFacade pf = getPersonFacade(EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/startcode",
                "dev",
                "ax2",
                EMF_Creator.Strategy.CREATE));
        Person p = pf.addPerson("Henrik", "Hamsun", "40404050");
//        pf.getAllPersons().forEach(System.out::println);
//        p.setFirstName("Hansine");
//        pf.editPerson(p);
//        pf.getAllPersons().forEach(System.out::println);
        System.out.println("count: "+pf.count());
    }

}
