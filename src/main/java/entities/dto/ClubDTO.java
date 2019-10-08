/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.dto;

import entities.Club;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 *
 * @author tha
 */
public class ClubDTO {
    private long id;
    @Schema(required = true,example = "East Sea sailing")
    private String name;
    @Schema(example="{\n" +
            "      \"id\": 3,\n" +
            "      \"fName\": \"Hansine\",\n" +
            "      \"lName\": \"Hamsun\",\n" +
            "      \"phone\": \"40404050\",\n" +
            "      \"street\": \"Rolighedsvej 4\",\n" +
            "      \"zip\": \"2100\",\n" +
            "      \"city\": \"Copenhagen East\",\n" +
            "      \"clubs\": [\n" +
            "        \"sailing\"\n" +
            "      ]\n" +
            "    },")
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
