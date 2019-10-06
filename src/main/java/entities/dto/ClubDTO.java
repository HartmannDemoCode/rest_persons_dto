/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.dto;

import entities.Club;
import java.util.List;

/**
 *
 * @author tha
 */
public class ClubDTO {
    private long id;
    private String name;
    private List<PersonDTO> persons;

    public ClubDTO(Club club) {
        this.id = club.getId();
        this.name = club.getName();
        club.getPersons().forEach(p->persons.add(new PersonDTO(p)));
    }

    public ClubDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PersonDTO> getPersons() {
        return persons;
    }

    public void addPerson(PersonDTO person) {
        this.persons.add(person);
    }
    
}
