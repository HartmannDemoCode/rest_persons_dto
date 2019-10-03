package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.Person;
import entities.dto.PersonDTO;
import entities.dto.PersonsDTO;
import exceptions.PersonNotFoundException;
import facades.IPersonFacade;
import utils.EMF_Creator;
import facades.PersonFacade;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//Todo Remove or change relevant parts before ACTUAL use
@Path("person")
public class PersonRessource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/startcode",
                "dev",
                "ax2",
                EMF_Creator.Strategy.CREATE);
    private static final IPersonFacade FACADE =  PersonFacade.getPersonFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
            
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }
    
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getPerson(@PathParam("id") int id) throws PersonNotFoundException {
        System.out.println("ID:"+id);
        System.out.println(FACADE.getPerson(id));
        return Response.ok(GSON.toJson(new PersonDTO(FACADE.getPerson(id)))).build();
    }
    
    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getPersons() {
        List<Person> persons = FACADE.getAllPersons();
        PersonsDTO pd = new PersonsDTO(persons);
        return Response.ok(GSON.toJson(pd)).build();
    }
//    
    @GET
    @Path("count")
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonCount() {
        long count = FACADE.count();
        //System.out.println("--------------->"+count);
        return "{\"count\":"+count+"}";  //Done manually so no need for a DTO
    }
    
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createPerson(String content){
        PersonDTO pd = GSON.fromJson(content, PersonDTO.class);
        Person p = FACADE.addPerson(pd.getfName(), pd.getlName(), pd.getPhone());
        return Response.ok(GSON.toJson(new PersonDTO(p))).build();
    }
    
    @PUT
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response editPerson(@PathParam("id") int id, String content) throws PersonNotFoundException{
        System.out.println("CONTENT: "+content);
        Person pd = GSON.fromJson(content, Person.class);
        System.out.println("PERSON:"+pd);
        pd.setId(id);
        Person p = FACADE.editPerson(pd);
        return Response.ok(GSON.toJson(new PersonDTO(p))).build();
    }
    
    @DELETE
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response deletePerson(@PathParam("id") int id) throws PersonNotFoundException{

        Person p = FACADE.deletePerson(id);
        return Response.ok(GSON.toJson(new PersonDTO(p))).build();
    }

 
}
