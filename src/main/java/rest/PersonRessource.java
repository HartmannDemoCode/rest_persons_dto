package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.Person;
import entities.dto.PersonDTO;
import entities.dto.PersonsDTO;
import exceptions.PersonNotFoundException;
import facades.IPersonFacade;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import utils.EMF_Creator;
import facades.PersonFacade;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@OpenAPIDefinition(
        info = @Info(
                title = "Demo on using JPA, Rest and OpenApi",
                version = "0.1",
                description = "This is the main ressource: Person that has relationships to Clup and Address objects",
                contact = @Contact(name = "Thomas Hartmann", email = "somemail@postit.org")
        ),
        tags = {
            @Tag(name = "person", description = "API related to person")
        },
        servers = {
            @Server(
                    description = "For Local testing",
                    url = "http://localhost:8080/rest_persons_dto"
            ),
            @Server(
                    description = "Server API",
                    url = "https://pro.bugelhartmann.dk/rest_persons_dto"
            )

        }
)
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
        return "{\"msg\":\"Hello \"}";
    }

    @GET
    @Path("error")
    @Produces({MediaType.APPLICATION_JSON})
    public String makeError() {
        throw new WebApplicationException("Some error message", 400);
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Get information about a person by id",
            tags = {"person"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Person.class))),
                @ApiResponse(responseCode = "200", description = "The Requested person"),
                @ApiResponse(responseCode = "400", description = "Person not found")})
    public Response getPerson(@PathParam("id") Long id) throws PersonNotFoundException {
        System.out.println("ID:" + id);
        System.out.println(FACADE.getPerson(id));
        return Response.ok(GSON.toJson(FACADE.getPerson(id))).build();
    }

    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Get all persons in the system",
            tags = {"person"},
            responses = {
                    @ApiResponse(
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Person.class))),
                    @ApiResponse(responseCode = "200", description = "Get all persons in system")})
    public Response getPersons() {
        PersonsDTO persons = FACADE.getAllPersons();
        return Response.ok(GSON.toJson(persons)).build();
    }
//    

    @GET
    @Path("count")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Get count on how many persons are in the system",
            tags = {"count"},
            responses = {
                    @ApiResponse(content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "200", description = "The requested count")})
    public String getPersonCount() {
        long count = FACADE.count();
        //System.out.println("--------------->"+count);
        return "{\"count\":" + count + "}";  //Done manually so no need for a DTO
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Operation(summary = "Post information to create a new person",
            tags = {"person"},
            responses = {
                    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
                    @ApiResponse(responseCode = "200", description = "The new person that was created")})
    public Response createPerson(
            @RequestBody(description = "PersonDTO object that needs to be added to the store",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PersonDTO.class))) PersonDTO content) {
//        PersonDTO pd = GSON.fromJson(content, PersonDTO.class);
        PersonDTO p = FACADE.addPerson(content);
        return Response.ok(GSON.toJson(p)).build();
    }

    @PUT
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Operation(summary = "PUT information to update a person",
            tags = {"person","update"},
            responses = {
                    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
                    @ApiResponse(responseCode = "200", description = "The Updated person")})
    public Response editPerson(@PathParam("id") int id,@RequestBody(description = "PersonDTO object that needs to be updated", required = true,
            content = @Content(schema = @Schema(implementation = PersonDTO.class))) PersonDTO content) throws PersonNotFoundException {
        content.setId(id);
        PersonDTO pOut = FACADE.editPerson(content);
        System.out.println("PERSON................=>" + pOut);
        return Response.ok(GSON.toJson(pOut)).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Delete a person",
            tags = {"person","delete"},
            responses = {
                    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
                    @ApiResponse(responseCode = "200", description = "The deleted person")})
    public Response deletePerson(@PathParam("id") Long id) throws PersonNotFoundException {
        PersonDTO pdto = FACADE.deletePerson(id);
        return Response.ok(GSON.toJson(pdto)).build();
    }
}
