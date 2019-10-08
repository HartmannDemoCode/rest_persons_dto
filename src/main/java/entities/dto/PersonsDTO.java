/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.dto;

import entities.Person;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tha
 */
public class PersonsDTO {
    @Schema(required = true,example = "[{\n" +
            "            \"id\": 1,\n" +
            "            \"fName\": \"Henrik\",\n" +
            "            \"lName\": \"Hamsun\",\n" +
            "            \"phone\": \"40404050\",\n" +
            "            \"street\": \"Rolighedsvej 4\",\n" +
            "            \"zip\": \"2100\",\n" +
            "            \"city\": \"Copenhagen East\",\n" +
            "            \"clubs\": []\n" +
            "        },...],")
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
