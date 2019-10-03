/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.dto;

import entities.Person;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tha
 */
public class PersonsDTO {
    
    List<PersonDTO> all = new ArrayList();

    public PersonsDTO(List<Person> personEntities) {
//        personEntities.forEach((p) -> {
//            all.add(new PersonDTO(p));
//        });
        for (Person person : personEntities) {
            all.add(new PersonDTO(person));
        }
    }

    public List<PersonDTO> getAll() {
        return all;
    }

    public void setAll(List<PersonDTO> all) {
        this.all = all;
    }
    
}
