/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;
import entities.Address;
import entities.Person;
import entities.dto.PersonDTO;
import entities.dto.PersonsDTO;
import exceptions.AddressNotFoundException;
import exceptions.PersonNotFoundException;

/**
 *
 * @author tha
 */


    public interface IPersonFacade {
        
        PersonDTO addPerson(String fName, String lName, String phone, String street, String zip, String city);

        PersonDTO addPerson(PersonDTO pdto);

        PersonDTO deletePerson(int id) throws PersonNotFoundException;

        PersonDTO getPerson(int id) throws PersonNotFoundException;

        PersonsDTO getAllPersons();

        PersonDTO editPerson(PersonDTO p) throws PersonNotFoundException;
                
        long count();
    }
