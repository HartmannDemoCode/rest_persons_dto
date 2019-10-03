package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;


@Entity
@NamedQueries({
@NamedQuery(name = "Person.deleteAllRows", query = "DELETE from Person"),
@NamedQuery(name = "Person.getAll", query = "SELECT p FROM Person p")
})
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fName, lName, phone;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date created;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date lastEdited;

    public Person(String firstName, String lastName, String phone) {
        this.fName = firstName;
        this.lName = lastName;
        this.phone = phone;
        this.created = new Date();
        this.lastEdited = new Date();
    }

    public Person() {
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return fName;
    }

    public void setFirstName(String firstName) {
        this.fName = firstName;
        this.lastEdited = new Date();

    }

    public String getLastName() {
        return lName;
    }

    public void setLastName(String lastName) {
        this.lName = lastName;
        this.lastEdited = new Date();

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        this.lastEdited = new Date();
    }

    public Date getCreated() {
        return created;
    }


    public Date getLastEdited() {
        return lastEdited;
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", firstName=" + fName + ", lastName=" + lName + ", phone=" + phone + ", created=" + created + ", lastEdited=" + lastEdited + '}';
    }

    
        
    
}
