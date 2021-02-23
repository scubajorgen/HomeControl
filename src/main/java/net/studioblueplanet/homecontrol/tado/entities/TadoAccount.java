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
    
    /** Username, usually the e-mail */
    private String              username;
    /** Password */
    private String              password;
    /** Tokens as retrieved from OAuth server from Tado */
    private TadoToken           token;
    /** Datetime of last token refresh */
    private Date                lastRefresh;
    /** Account information from Tado */
    private TadoMe              tadoMe;

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
        Date date;
        
        if (token!=null)
        {
            date=token.getAccessTokenExpires();          
        }
        else
        {
            date=null;
        }
        
        return date;
    }

    public int expiresIn()
    {
        int seconds;
        
        if (token!=null)
        {
            seconds=Math.max(token.getExpires_in()-(int)((System.currentTimeMillis()-lastRefresh.getTime())/MILLISECONDSPERSECOND),0);          
        }
        else
        {
            seconds=0;
        }
        
        return seconds;
    }
    
    public boolean needsRefresh()
    {
        return (expiresIn()<REFRESHBEFORESECONDS);
    }

    public TadoMe getTadoMe()
    {
        return tadoMe;
    }

    public void setTadoMe(TadoMe tadoMe)
    {
        this.tadoMe = tadoMe;
    }
}
