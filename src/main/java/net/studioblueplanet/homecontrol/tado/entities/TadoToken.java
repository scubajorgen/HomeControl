/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Date;
import org.json.simple.JSONObject;  
import org.json.simple.JSONValue;  
/**
 *
 * @author jorgen
 */
public class TadoToken
{
    /** The access token */
    private String access_token;
    /** Toke type: password */
    private String token_type;
    /** Token to be used for refreshing the access token */
    private String refresh_token;
    /** Expiration in seconds, usually 599 */
    private int    expires_in;
    
    /** Scope to which the token applies, e.g. home.user */
    private String scope;
    /** ? */
    private String jti;

    public String getAccess_token()
    {
        return access_token;
    }

    public void setAccess_token(String access_token)
    {
        this.access_token = access_token;
    }

    public String getToken_type()
    {
        return token_type;
    }

    public void setToken_type(String token_type)
    {
        this.token_type = token_type;
    }

    public String getRefresh_token()
    {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token)
    {
        this.refresh_token = refresh_token;
    }

    public int getExpires_in()
    {
        return expires_in;
    }

    public void setExpires_in(int expires_in)
    {
        this.expires_in = expires_in;
    }

    public String getScope()
    {
        return scope;
    }

    public void setScope(String scope)
    {
        this.scope = scope;
    }

    public String getJti()
    {
        return jti;
    }

    public void setJti(String jti)
    {
        this.jti = jti;
    }
    
    public Date getAccessTokenExpires()
    {
        Date date;
        String[] split_string   = access_token.split("\\.");
        Decoder decoder         = Base64.getUrlDecoder();
        String payload          = new String(decoder.decode(split_string[1]));
        Object object           =JSONValue.parse(payload);
        JSONObject jsonObject   =(JSONObject)object;
        date=new Date((long)(jsonObject.get("exp"))*1000);
        return date;
    }
    
}
