/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;

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
    private String              encryptedPassword;
    /** Tokens as retrieved from OAuth server from Tado */
    private TadoToken           token;
    /** Datetime of last token refresh */
    private Date                lastRefresh;
    /** Account information from Tado */
    private TadoMe              tadoMe;
    
    private final String        pwd         ="34be657de602799cf9dbcd2756e07f4c";
    private final String        salt        ="e149b3adbffc265ad92cc78afde4f432";
    private final TextEncryptor encryptor   = Encryptors.text(pwd, salt);

    public TadoAccount(String username, String password, TadoToken token)
    {
        this.username           =username;
        this.encryptedPassword  =encryptor.encrypt(password);
        this.token              =token;
        this.lastRefresh        =new Date();
    }
    
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getEncryptedPassword()
    {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword)
    {
        this.encryptedPassword = encryptedPassword;
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
    
    public String getPassword()
    {
        return encryptor.decrypt(encryptedPassword);
    }
}
