# Swagger og Rest API

1. Clone start code from [here](git clone https://github.com/dat3startcode/rest-jpa-devops-startcode.git -b openapi demoOAS)
2. Download SwaggerUI from [https://swagger.io/tools/swagger-ui/](https://github.com/swagger-api/swagger-ui) (kun hent dist folderen. Det er buildet som er det eneste der er brug for)
  - Gem denne folder inde i maven projectets webapp folder og omdøb den til 'openapi'
3. [Indsæt 2 nye dependencies i POM.xml](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Getting-started):

```xml
 <dependency>
    <groupId>io.swagger.core.v3</groupId>
    <artifactId>swagger-jaxrs2</artifactId>
    <version>2.0.9</version>
</dependency>

<dependency>
    <groupId>io.swagger.core.v3</groupId>
    <artifactId>swagger-jaxrs2-servlet-initializer</artifactId>
    <version>2.0.9</version>
</dependency>
```
4. i ApplicationConfig.java indsætters følgende i metode: getClasses():
```java
//These two Resource Classes are not auto discovered so we add them manually above the return statement
        resources.add(OpenApiResource.class);
        resources.add(AcceptHeaderOpenApiResource.class);
```
5. Inside the openapi folder in the index.html change `SwaggerUIBundle({url: "../api/openapi.json",` like so.
6. In each rest ressource java file add to the top as [here](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Annotations#openapi-annotations):
```java
@OpenAPIDefinition(
        info = @Info(
                title = "Demo on using JPA, Rest and OpenApi",
                version = "0.1",
                description = "This is the main ressource Person that has relationships to Clup and Address",
                contact = @Contact(name = "......", email = "...........")
        ),
        tags = {
            @Tag(name = "person", description = "API related to person")
        },
        servers = {
            @Server(
                    description = "For Local host testing",
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
```
as seen above the @OpenAPIDefinition annotationen sits right on top of the class keyword in the java file and descripes the general stuff about this ressource.  
7. Create a new DTO class and annotate each field (except the id field) with an annotation like:  
`@Schema(required = true,example = "Forrest Gump")` or  
`@Schema(required = true,example="Robert Zemeckis")` or  
`@Schema(required = true,example = "1994")` or  
`@Schema(example="[\"Tom Hanks\",\"Robin Wright\"]")`  
8. In POST and PUT method annotate for the request body like:
```java
public Response createPerson(
            @RequestBody(description = "PersonDTO object that needs to be added to the store",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PersonDTO.class))) PersonDTO content) {
        PersonDTO p = FACADE.addPerson(content);
        return Response.ok(GSON.toJson(p)).build();
    }
```
