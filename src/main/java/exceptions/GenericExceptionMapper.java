/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exceptions.dto.ExceptionDTO;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author tha
 */
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    @Override
    public Response toResponse(Throwable ex) {
        Response.StatusType type = getStatusType(ex);
        Logger.getLogger(GenericExceptionMapper.class.getName()).log(Level.SEVERE, null, ex);
        ExceptionDTO err = new ExceptionDTO(type.getStatusCode(),type.getReasonPhrase());
        return Response.status(type.getStatusCode())
                .entity(gson.toJson(err))
                .type(MediaType.APPLICATION_JSON).
                build();
    }
     private Response.StatusType getStatusType(Throwable ex) {
        if (ex instanceof WebApplicationException) {
            return((WebApplicationException)ex).getResponse().getStatusInfo();
        } 
        return Response.Status.INTERNAL_SERVER_ERROR;
        
    }
}

