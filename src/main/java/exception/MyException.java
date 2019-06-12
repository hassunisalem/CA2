/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

import com.google.gson.Gson;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author claudia
 */
public class MyException extends WebApplicationException
{

    private int errorCode;
    private Gson gson = new Gson();

    public MyException(String message, int errorCode)
    {
        super(message);
        this.errorCode = errorCode;
    }

    public MyException()
    {
        super();
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public Response toResponse(MyException exception)
    {
        return Response.status(404).entity(gson.toJson(exception)).type(MediaType.APPLICATION_JSON).build();
    }
}
