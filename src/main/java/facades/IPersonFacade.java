/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;
import exceptions.ClubNotFoundException;
import entities.Address;
import entities.dto.ClubDTO;
import entities.dto.PersonDTO;
import entities.dto.PersonsDTO;
import exceptions.AddressNotFoundException;
import exceptions.PersonNotFoundException;
import java.util.List;

/**
 *
 * @author tha
 */


    public interface IPersonFacade {
        
        PersonDTO addPerson(String fName, String lName, String phone, String street, String zip, String city);

        PersonDTO addPerson(PersonDTO pdto);

        PersonDTO deletePerson(Long id) throws PersonNotFoundException;

        PersonDTO getPerson(Long id) throws PersonNotFoundException;

        PersonsDTO getAllPersons();

        PersonDTO editPerson(PersonDTO p) throws PersonNotFoundException;
        
        Address findAddress(String street, String zip, String city) throws AddressNotFoundException;
        
        ClubDTO getClub(Long id) throws ClubNotFoundException;
                
        List<ClubDTO> getAllClubs();
        
        long count();
    }
