/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.service.entities;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jorgen
 */
public class Account
{
    /** Tado account ID */
    private String                  id;
    /** Name */
    private String                  name;
    /** E-Mail */
    private String                  email;
    /** Username - key */
    private String                  username;
    /** Home owned by the account */
    private List<HomeId>            ownHomes;
    /** Homes to which the account has access: own homes and friend homes */
    private List<HomeId>            accessibleHomes;
   
    public Account()
    {
    }
    
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
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

    public List<HomeId> getOwnHomes()
    {
        return ownHomes;
    }

    public void setOwnHomes(List<HomeId> homes)
    {
        this.ownHomes = homes;
    }

    public List<HomeId> getAccessibleHomes()
    {
        return accessibleHomes;
    }

    public void setAccessibleHomes(List<HomeId> accessibleHomes)
    {
        this.accessibleHomes = accessibleHomes;
    }
}
