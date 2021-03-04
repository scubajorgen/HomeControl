/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

import java.util.List;
import java.util.Iterator;

/**
 *
 * @author jorgen
 */
public class TadoErrorMessage
{
    /** Array of errors */
    List<TadoError> errors;

    public List<TadoError> getErrors()
    {
        return errors;
    }

    public void setErrors(List<TadoError> errors)
    {
        this.errors = errors;
    }
    
    public String getMessages()
    {
        String messages=null;
        
        if (errors!=null)
        {
            TadoError error;
            Iterator<TadoError> it;
            it=errors.iterator();
            messages="";
            while (it.hasNext())
            {
                error=it.next();
                messages+=error.getTitle();
                if (it.hasNext())
                {
                    messages+=", ";
                }
            }
        }
        return messages;
    }
}
