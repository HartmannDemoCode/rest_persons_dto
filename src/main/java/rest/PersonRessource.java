package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.Address;
import entities.Person;
import entities.dto.PersonDTO;
import entities.dto.PersonsDTO;
import exceptions.AddressNotFoundException;
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
    private static final IPersonFacade FACADE = PersonFacade.getPersonFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getPerson(@PathParam("id") Long id) throws PersonNotFoundException {
        System.out.println("ID:" + id);
        System.out.println(FACADE.getPerson(id));
        return Response.ok(GSON.toJson(FACADE.getPerson(id))).build();
    }

    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getPersons() {
        PersonsDTO persons = FACADE.getAllPersons();
        return Response.ok(GSON.toJson(persons)).build();
    }
//    

    @GET
    @Path("count")
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonCount() {
        long count = FACADE.count();
        //System.out.println("--------------->"+count);
        return "{\"count\":" + count + "}";  //Done manually so no need for a DTO
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createPerson(String content) {
        PersonDTO pd = GSON.fromJson(content, PersonDTO.class);
        PersonDTO p = FACADE.addPerson(pd);
        return Response.ok(GSON.toJson(p)).build();
    }

    @PUT
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response editPerson(@PathParam("id") int id, String content) throws PersonNotFoundException {
        PersonDTO pd = GSON.fromJson(content, PersonDTO.class);
        pd.setId(id);
        PersonDTO pOut = FACADE.editPerson(pd);
        System.out.println("PERSON................=>" + pOut);
        return Response.ok(GSON.toJson(pOut)).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response deletePerson(@PathParam("id") Long id) throws PersonNotFoundException {

        PersonDTO pdto = FACADE.deletePerson(id);
        return Response.ok(GSON.toJson(pdto)).build();
    }

}
