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
    private String                  id;
    private String                  name;
    private String                  email;
    private String                  username;
    private List<HomeId>            homes;
    private List<String>            friendAccountUsernames;
    
    public Account()
    {
        this.friendAccountUsernames=new ArrayList<>();
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

    public List<HomeId> getHomes()
    {
        return homes;
    }

    public void setHomes(List<HomeId> homes)
    {
        this.homes = homes;
    }

    public List<String> getFriendAccountUsernames()
    {
        return friendAccountUsernames;
    }

    public void setFriendAccountUsernames(List<String> friendAccountUsernames)
    {
        this.friendAccountUsernames.clear();
        this.friendAccountUsernames.addAll(friendAccountUsernames);
    }
    
    public void addFriendAccountUsername(String username)
    {
        this.friendAccountUsernames.add(username);
    }
}
