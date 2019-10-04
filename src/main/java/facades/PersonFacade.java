package facades;

import entities.Address;
import entities.Person;
import entities.dto.PersonDTO;
import entities.dto.PersonsDTO;
import exceptions.AddressNotFoundException;
import exceptions.PersonNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
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
    private PersonFacade() {
    }

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
    public PersonDTO addPerson(String fName, String lName, String phone, String street, String zip, String city) {
        EntityManager em = getEntityManager();
        Person p = new Person(fName, lName, phone);
        Address a = new Address(street, zip, city);
        em.getTransaction().begin();
        Address mergedAddress = em.merge(a);
        p.setAddress(mergedAddress);
        em.persist(p);
        em.getTransaction().commit();
        return new PersonDTO(p);
    }
    
    @Override
    public PersonDTO addPerson(PersonDTO pdto) {
        EntityManager em = getEntityManager();
        Person p = new Person(pdto.getfName(),pdto.getlName(),pdto.getPhone());
        Address a = new Address(pdto.getStreet(),pdto.getZip(),pdto.getCity());
        em.getTransaction().begin();
        Address mergedAddress = em.merge(a);
        p.setAddress(mergedAddress);
        em.persist(p);
        em.getTransaction().commit();
        return new PersonDTO(p);
    }

    @Override
    public PersonDTO deletePerson(Long id) throws PersonNotFoundException {
        EntityManager em = getEntityManager();
        Person p = em.find(Person.class, id);
        if (p == null) {
            throw new PersonNotFoundException("Could not delete, provided id does not exist");
        }
        Address a = p.getAddress();
        em.getTransaction().begin();
        if(livesAloneAtAddress(a)){
            em.remove(a);
        }
        em.remove(p);
        em.getTransaction().commit();
        em.close();
        return new PersonDTO(p);
    }

    @Override
    public PersonDTO getPerson(Long id) throws PersonNotFoundException {
        EntityManager em = getEntityManager();
        Person p = em.find(Person.class, id);
        if (p == null) {
            throw new PersonNotFoundException("No person with provided id found");
        }
        em.close();
        return new PersonDTO(p);
    }

    @Override
    public PersonsDTO getAllPersons() {
        EntityManager em = getEntityManager();
        TypedQuery<Person> tq = em.createNamedQuery("Person.getAll", Person.class);
        List<Person> persons = tq.getResultList();
        em.close();
        return new PersonsDTO(persons);
    }

    @Override
    public PersonDTO editPerson(PersonDTO pdto) throws PersonNotFoundException {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Person p = new Person(pdto.getfName(), pdto.getlName(),pdto.getPhone());
        p.setId(pdto.getId());
        Address address = null;
        try {
            address = findAddress(pdto.getStreet(), pdto.getZip(), pdto.getCity());
            address = em.merge(address);
            p.setAddress(address);
        } catch (AddressNotFoundException ex) {
            address = new Address(pdto.getStreet(), pdto.getZip(), pdto.getCity());
            em.persist(address);
            p.setAddress(address);
        }
        try {
            p = em.merge(p);
        } catch (IllegalArgumentException e) {
            throw new PersonNotFoundException("Could not edit person. No person found with id " + p.getId());
        }
        em.getTransaction().commit();
        em.close();
        return new PersonDTO(p);
    }

    @Override
    public long count() {
        EntityManager em = getEntityManager();
        TypedQuery<Long> tq = em.createQuery("SELECT count(p) FROM Person p", Long.class);
        long count = tq.getSingleResult();
        em.close();
        return count;
    }

    public Address findAddress(String street, String zip, String city) throws AddressNotFoundException {
        EntityManager em = getEntityManager();
        Address address = null;
            TypedQuery<Address> tq = em.createQuery("SELECT a FROM Address a WHERE a.city = :city AND a.zip = :zip AND a.street = :street", Address.class);
            tq.setParameter("city", city);
            tq.setParameter("zip", zip);
            tq.setParameter("street", street);
        try {
            address = tq.getSingleResult();
        } catch (NonUniqueResultException e){
            return tq.getResultList().get(1);
        } catch (NoResultException e) {
            throw new AddressNotFoundException("No address found");
        }
        return address;
    }

    public static void main(String[] args) {
        IPersonFacade pf = getPersonFacade(EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/startcode",
                "dev",
                "ax2",
                EMF_Creator.Strategy.CREATE));
//        Person p = pf.addPerson("Henrik", "Hamsun", "40404050", new Address("Rolighedsvej 4", "2100", "Copenhagen East"));
//        pf.getAllPersons().forEach(System.out::println);
//        p.setFirstName("Hansine");
//        pf.editPerson(p);
//        pf.getAllPersons().forEach(System.out::println);
           
    }


    private boolean livesAloneAtAddress(Address a){
        return a != null && a.getPersons().size() == 1;
    }
}
