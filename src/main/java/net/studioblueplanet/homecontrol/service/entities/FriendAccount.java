/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.service.entities;

import java.util.Date;

/**
 *
 * @author jorgen
 */
public class FriendAccount
{
    private String account;
    private String friendAccount;
    private Date   creationDate;

    public FriendAccount()
    {
        creationDate=new Date();
    }
    
    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getFriendAccount()
    {
        return friendAccount;
    }

    public void setFriendAccount(String friendAccount)
    {
        this.friendAccount = friendAccount;
    }

    public Date getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }
}
