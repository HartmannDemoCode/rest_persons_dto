/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.dto;

import entities.Address;
import entities.Club;
import entities.Person;
import java.util.ArrayList;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.HashSet;
import java.util.Set;


/**
 *
 * @author tha
 */
@Schema(name = "Person")
public class PersonDTO {

    private long id;
    @Schema(required = true, example = "Henning")
    private String fName;
    @Schema(required = true,example = "Jensen")
    private String lName;
    @Schema(required = true,example = "+4540980480")
    private String phone;
    @Schema(required = true,example = "Rolighedsvej 3")
    private String street;
    @Schema(required = true,example = "2100")
    private String zip;
    @Schema(required = true,example = "Kbh Ø")
    private String city;
    @Schema(required = true,example = "\"clubs\":[\"sailing\"]")
    private List<String> clubs = new ArrayList();

    public PersonDTO(Person p) {
        this.fName = p.getFirstName();
        this.lName = p.getLastName();
        this.phone = p.getPhone();

        this.id = p.getId();
        Address a = p.getAddress();
        if (a != null) {
            this.street = a.getStreet();
            this.zip = a.getZip();
            this.city = a.getCity();
        }
//       p.getClubs().forEach(club->clubs.add(club.getName()));
//        List<Club> clubObjects = p.getClubs();
        for (Club clubObject : p.getClubs()) {
            clubs.add(clubObject.getName());
        }
    }

    public PersonDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getClubs() {
        return clubs;
    }

    public void addClub(String club) {
        this.clubs.add(club);
    }

    @Override
    public String toString() {
        String clubsString = clubs.stream().reduce("", (partialString, element) -> partialString + element);
        return "PersonDTO{" + "id=" + id + ", fName=" + fName + ", lName=" + lName + ", phone=" + phone + ", clubs: "+clubsString+'}';
    }

}
