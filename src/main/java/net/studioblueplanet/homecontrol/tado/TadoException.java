/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado;

/**
 *
 * @author jorgen
 */
public class TadoException extends RuntimeException
{
    public enum TadoExceptionType
    {
        SERVER_ERROR,
        CLIENT_ERROR
    }
    private final TadoExceptionType     type;
    private final int                   statusCode;
    public TadoException(TadoExceptionType type, String message, int statusCode)
    {
        super(message);
        this.type=type;
        this.statusCode=statusCode;
    }
    
    @Override
    public String getMessage()
    {
        String message;
        
        switch (type)
        {
            case SERVER_ERROR:
                message="Server error: "+super.getMessage();
                break;
            case CLIENT_ERROR:
                message="Client error: "+super.getMessage()+". Status code: "+statusCode;
                break;
            default:
                message="Unknown error";
                break;
        }
        return message;
    }
    
    public TadoExceptionType getType()
    {
        return type;
    }
   
    public int getStatusCode()
    {
        return statusCode;
    }
}
