/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import net.studioblueplanet.homecontrol.tado.entities.TadoHome;
import net.studioblueplanet.homecontrol.tado.entities.TadoMe;
import net.studioblueplanet.homecontrol.tado.entities.TadoPresence;
import net.studioblueplanet.homecontrol.tado.entities.TadoState;
import net.studioblueplanet.homecontrol.tado.entities.TadoToken;
import net.studioblueplanet.homecontrol.tado.entities.TadoZone;
import net.studioblueplanet.homecontrol.tado.entities.TadoZoneState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jorgen.van.der.velde
 */
public class TadoInterfaceImpl extends TimerTask implements TadoInterface
{
    private static final long   MILLISECONDSPERSECOND         =1000L;
    private static final int    REFRESHSECONDSBEFOREEXPIRATION=20;
    private static final int    MINSECONDSBEFOREREFRESH       =2;
    private static final int    MAXSECONDSBEFOREREFRESH       =120;
    private static final Logger LOG = LoggerFactory.getLogger(TadoInterfaceImpl.class);    

    // Guarded data
    private TadoToken           token;
    private Timer               timer;
    
    @Autowired
    private RestTemplate  template;
    
    
    public TadoInterfaceImpl()
    {
        synchronized (this)
        {
            token=null;
            timer=null;
        }
    }
    
    /**
     * The timed process. Executes a token refresh
     */
    @Override
    public void run()
    {
        refreshToken();
    }
    
    /**
     * Refresh the authentication token
     */
    private void refreshToken()
    {
        TadoToken localToken;
     
        synchronized(this)
        {
            localToken=token;
        }     
        if (localToken!=null)
        {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
            requestBody.add("client_id", "tado-web-app");
            requestBody.add("scope", "home.user");
            requestBody.add("grant_type", "refresh_token");
            requestBody.add("refresh_token", localToken.getRefresh_token());
            requestBody.add("client_secret", "wZaRN7rpjn3FoNyF5IFuxg9uMzYJcvOoQ8QWiIqS3hfk6gLhVlG57j5YNoZL2Rtc");

            HttpEntity formEntity = new HttpEntity<MultiValueMap<String, String>>(requestBody, headers);
            try
            {
                localToken = template.postForObject("https://auth.tado.com/oauth/token", formEntity, TadoToken.class);        
                LOG.info("Token refreshed: expires in {} seconds", localToken.getExpires_in());

                synchronized(this)
                {
                    token=localToken;
                }
            }
            catch (HttpClientErrorException e)
            {
                LOG.error("Error requesting authentication token: {}", e.getMessage());
            }        
        }
        else
        {
            LOG.info("Token refresh: no token, no need to refresh");
        }
    }
    
    /**
     * Authenticates by requesting the access token. 
     * @param username Username
     * @param password Password
     * @return Token acquired or null if authentication failed
     */
    @Override
    public TadoToken authenticate(String username, String password)
    {
        int         delaySeconds;
        long        delayMilliseconds;
        TadoToken   localToken;

        signOut();
        localToken=null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
        requestBody.add("client_id", "tado-web-app");
        requestBody.add("scope", "home.user");
        requestBody.add("grant_type", "password");
        requestBody.add("username", username);
        requestBody.add("password", password);
        requestBody.add("client_secret", "wZaRN7rpjn3FoNyF5IFuxg9uMzYJcvOoQ8QWiIqS3hfk6gLhVlG57j5YNoZL2Rtc");

        HttpEntity formEntity = new HttpEntity<MultiValueMap<String, String>>(requestBody, headers);
        try
        {
            localToken = template.postForObject("https://auth.tado.com/oauth/token", formEntity, TadoToken.class);
            LOG.info("Token acquired: expires in {} seconds", localToken.getExpires_in());

            synchronized(this)
            {
                token=localToken;
                // Only the 1st authentication: schedule a timer
                if (timer==null)
                {
                    delaySeconds        =Math.max(localToken.getExpires_in()-REFRESHSECONDSBEFOREEXPIRATION, MINSECONDSBEFOREREFRESH);
                    delaySeconds        =Math.min(delaySeconds, MAXSECONDSBEFOREREFRESH);
                    delayMilliseconds   =MILLISECONDSPERSECOND*delaySeconds;
                    LOG.info("Scheduling token refresh every {} seconds", delayMilliseconds/MILLISECONDSPERSECOND);
                    timer=new Timer();
                    timer.scheduleAtFixedRate(this, delayMilliseconds, delayMilliseconds);
                }
            }
        }
        catch (HttpClientErrorException e)
        {
            LOG.error("Error requesting authentication token for user {}: {}", username, e.getMessage());
        }
        
        return localToken;
    }
    
     public void signOut()
     {
        synchronized(this)
        {
            token=null;
        }         
     }
    
    /**
     * Request information about me
     * @return The information about me
     */
    @Override
    public TadoMe tadoMe()
    {
        HttpHeaders headers             = new HttpHeaders();
        headers.setBearerAuth(token.getAccess_token());
        HttpEntity entity               = new HttpEntity(headers);
        ResponseEntity<TadoMe> response = template.exchange("https://my.tado.com/api/v2/me/", HttpMethod.GET, entity, TadoMe.class);        
        TadoMe me=response.getBody();
        return me;
    }

    @Override
    public TadoHome tadoHome(int homeId)
    {
        HttpHeaders headers             = new HttpHeaders();
        headers.setBearerAuth(token.getAccess_token());
        HttpEntity entity               = new HttpEntity(headers);
        ResponseEntity<TadoHome> response = template.exchange("https://my.tado.com/api/v2/homes/"+homeId, HttpMethod.GET, entity, TadoHome.class);        
        TadoHome home=response.getBody();
        return home;        
    }

    @Override
    public List<TadoZone> tadoZones(int homeId)
    {
        HttpHeaders headers             = new HttpHeaders();
        headers.setBearerAuth(token.getAccess_token());
        HttpEntity entity               = new HttpEntity(headers);
        ResponseEntity<TadoZone[]> response = template.exchange("https://my.tado.com/api/v2/homes/"+homeId+"/zones", HttpMethod.GET, entity, TadoZone[].class);        
        TadoZone[] zones=response.getBody();
        return Arrays.stream(zones).collect(Collectors.toList());             
    }
    
    @Override
    public TadoState tadoState(int homeId)
    {
        HttpHeaders headers             = new HttpHeaders();
        headers.setBearerAuth(token.getAccess_token());
        HttpEntity entity               = new HttpEntity(headers);
        ResponseEntity<TadoState> response = template.exchange("https://my.tado.com/api/v2/homes/"+homeId+"/state", HttpMethod.GET, entity, TadoState.class);        
        TadoState state=response.getBody();
        return state;         
    }
    
    @Override
    public TadoZoneState tadoZoneState(int homeId, int zoneId)
    {
        HttpHeaders headers             = new HttpHeaders();
        headers.setBearerAuth(token.getAccess_token());
        HttpEntity entity               = new HttpEntity(headers);
        ResponseEntity<TadoZoneState> response = template.exchange("https://my.tado.com/api/v2/homes/"+homeId+"/zones/"+zoneId+"/state", 
                                                                   HttpMethod.GET, entity, TadoZoneState.class);        
        TadoZoneState state=response.getBody();
        return state;            
    }
    
    @Override
    public void setTadoPresence(int homeId, TadoPresence.TadoHomePresence presence)
    {
        TadoPresence thePresence;
        
        HttpHeaders headers             = new HttpHeaders();
        thePresence=new TadoPresence();
        thePresence.setHomePresence(presence);
        headers.setBearerAuth(token.getAccess_token());
        HttpEntity<TadoPresence> entity        = new HttpEntity<>(thePresence, headers);
        ResponseEntity<String> response = template.exchange("https://my.tado.com/api/v2/homes/"+homeId+"/presenceLock", 
                                                            HttpMethod.PUT, entity, String.class);        
    }
}
