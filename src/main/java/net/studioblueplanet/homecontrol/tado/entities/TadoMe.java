/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

import net.studioblueplanet.homecontrol.tado.entities.TadoHome;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author jorgen.van.der.velde
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TadoMe
{
    private String                  name;
    private String                  email;
    private String                  username;
    private String                  id;
    private List<TadoHome>          homes;
    private List<TadoMobileDevice>  mobileDevices;
    private String                  locale;

    public List<TadoMobileDevice> getMobileDevices()
    {
        return mobileDevices;
    }

    public void setMobileDevices(List<TadoMobileDevice> devices)
    {
        this.mobileDevices = devices;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public List<TadoHome> getHomes()
    {
        return homes;
    }

    public void setHomes(List<TadoHome> homes)
    {
        this.homes = homes;
    }

    public String getLocale()
    {
        return locale;
    }

    public void setLocale(String locale)
    {
        this.locale = locale;
    }
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    @Override
    public String toString()
    {
        String string;
        
        string="";
        string+="ID         : "+id;
        string+="Name       : "+name;
        string+="e-mail     : "+email;
        string+="Username   : "+username;
        string+="Locale     : "+locale;
        string+="HOMES";
        for (TadoHome home: homes)
        {
            string+=home.toString();
        }
        return string;
    }
}
