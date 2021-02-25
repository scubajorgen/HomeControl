/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.service.entities;

/**
 *
 * @author jorgen
 */
public class HomeId
{
    private int     id;
    private String  name;
    private String  accountUserName;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
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

    public String getAccountUserName()
    {
        return accountUserName;
    }

    public void setAccountUserName(String accountUserName)
    {
        this.accountUserName = accountUserName;
    }    
}
