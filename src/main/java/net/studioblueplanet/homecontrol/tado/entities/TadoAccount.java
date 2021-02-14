/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author jorgen
 */
public class TadoAccount
{
    private static final long   MILLISECONDSPERSECOND=1000;
    private static final int    REFRESHBEFORESECONDS =60;
    
    private String              username;
    private String              password;
    private TadoToken           token;
    private Date                lastRefresh;

    public TadoAccount(String username, String password, TadoToken token)
    {
        this.username   =username;
        this.password   =password;
        this.token      =token;
        this.lastRefresh=new Date();
    }
    
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public TadoToken getToken()
    {
        return token;
    }

    public void setToken(TadoToken token)
    {
        lastRefresh=new Date();
        this.token = token;
    }
    
    public Date getLastRefresh()
    {
        return lastRefresh;
    }
    
    public String getLastRefreshFormatted()
    {
        String formattedDate;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        formattedDate=formatter.format(lastRefresh);
        return formattedDate;
    }
    
    public Date expiryDate()
    {
        return new Date(lastRefresh.getTime()+token.getExpires_in()*MILLISECONDSPERSECOND);
    }

    public int expiresIn()
    {
        return token.getExpires_in()-(int)((System.currentTimeMillis()-lastRefresh.getTime())/MILLISECONDSPERSECOND);
    }
    
    public boolean needsRefresh()
    {
        return (expiresIn()<REFRESHBEFORESECONDS);
    }
}
