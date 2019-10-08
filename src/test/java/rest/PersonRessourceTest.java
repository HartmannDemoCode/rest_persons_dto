package rest;

import entities.Person;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonRessourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Person p1, p2;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        // Testing our Jax RS service by pointing to ApplicationConfig()
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.CREATE);

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        p1 = new Person("Henning", "Bonnetsen", "536436");
        p2 = new Person("Helle", "Harsk", "213243");
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.persist(p1);
            em.getTransaction().commit();
            
            em.getTransaction().begin();
            em.persist(p2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
//    @Test
//    public void testStart(){
//        WebTarget people = ClientBuilder.newClient().target("http://localhost:7777/api/person/all");
//        System.out.println(people.path("1").request().get());
//    }
//
    
    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when()
            .get("/person").then().statusCode(200);
    }

    @Test
    public void testDummyMsg() throws Exception {
        given().contentType("application/json")
            .get("/person/")
                 .then().statusCode(200)
                .body("msg", equalTo("Hello World"));
    }
    
//This test assumes the database contains two rows
    @Test
    public void testCount() throws Exception {
        given()
                .contentType("application/json")
                .get("/person/count").then()
                .assertThat()
                 .statusCode(200)
                .body("count", equalTo(2));
    }

    @Test
    public void testGetPersonById() throws Exception {
        int id = p1.getId();
        given()
                .contentType("application/json")
                .get("/person/"+id).then()
                .assertThat()
                .statusCode(200)
                .body("fName", is("Henning"));
    }
    @Test
    public void testGetPersonAll() throws Exception {
        Response response = given()
                .when().get("/person/all")
                .then()
                .contentType("application/json")
                .body("all.lName", hasItems("Bonnetsen","Harsk") )
                .extract().response();
        System.out.println(response.asString());
    }
    
    @Test
    public void testCreatePerson() throws Exception {

        given()
            .contentType(ContentType.JSON)
            .request().body("{\"fName\":\"Krister\",\"lName\": \"Havsup\",\"phone\": \"40404054\"}")
            .post("/person")
            .then().statusCode(200)
            .body("fName", is("Krister"));
    }
    @Test
    public void testEditPerson() throws Exception {

        given()
            .contentType(ContentType.JSON)
            .request().body("{\"fName\":\"Kalle\",\"lName\": \"Kistrup\",\"phone\": \"40404054\"}")
            .put("/person/"+p1.getId())
            .then().statusCode(200)
            .body("fName", is("Kalle"));
    }
    @Test
    public void testDeletePerson() throws Exception {

        given()
            .contentType(ContentType.JSON)
            .delete("/person/"+p1.getId())
            .then().statusCode(200)
            .body("fName", is("Henning"));
    }
    @Test
    public void testDeletePersonError() throws Exception {
        given()
            .contentType(ContentType.JSON)
            .delete("/person/999999")
            .then().statusCode(404)
            .body("message", is("Could not delete, provided id does not exist"));
    }
}
