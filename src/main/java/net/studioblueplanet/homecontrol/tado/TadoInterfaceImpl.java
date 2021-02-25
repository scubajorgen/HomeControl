/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import net.studioblueplanet.homecontrol.tado.entities.TadoAccount;
import net.studioblueplanet.homecontrol.tado.entities.TadoDevice;
import net.studioblueplanet.homecontrol.tado.entities.TadoHome;
import net.studioblueplanet.homecontrol.tado.entities.TadoMe;
import net.studioblueplanet.homecontrol.tado.entities.TadoOverlay;
import net.studioblueplanet.homecontrol.tado.entities.TadoPresence;
import net.studioblueplanet.homecontrol.tado.entities.TadoSetting;
import net.studioblueplanet.homecontrol.tado.entities.TadoState;
import net.studioblueplanet.homecontrol.tado.entities.TadoTemperature;
import net.studioblueplanet.homecontrol.tado.entities.TadoTermination;
import net.studioblueplanet.homecontrol.tado.entities.TadoToken;
import net.studioblueplanet.homecontrol.tado.entities.TadoZone;
import net.studioblueplanet.homecontrol.tado.entities.TadoZoneState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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




/**
 *
 * @author jorgen.van.der.velde
 */
public class TadoInterfaceImpl extends TimerTask implements TadoInterface
{
    private static final long       TIMERMILLISECONDS             =10000L;
    private static final long       MILLISECONDSPERSECOND         =1000L;
    private static final Logger     LOG = LoggerFactory.getLogger(TadoInterfaceImpl.class);    

    @Autowired
    private RestTemplate            template;


    // Guarded data
    private final Timer             timer;
    
    @Autowired
    private TadoAccountManager      accountManager;
    
    public TadoInterfaceImpl()
    {
        synchronized (this)
        {
            timer=new Timer();
            timer.scheduleAtFixedRate(this, TIMERMILLISECONDS, TIMERMILLISECONDS);
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
        TadoToken               localToken;
        TadoAccount             account;
        Iterator<TadoAccount>   it;
     
        it=accountManager.getAccounts().iterator();
        while (it.hasNext())
        {
            synchronized(this)
            {
                account=it.next();
                localToken=account.getToken();
            }     

            if ((localToken!=null) && account.needsRefresh())
            {
                LOG.info("Token for {} expires in {} seconds, refresh needed", account.getUsername(), account.expiresIn());
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
                    LOG.info("Token for {} refreshed: expires in {} seconds", account.getUsername(), localToken.getExpires_in());

                    synchronized(this)
                    {
                        account.setToken(localToken);
                    }
                }
                catch (HttpClientErrorException e)
                {
                    LOG.error("Error requesting authentication token: {}", e.getMessage());
                }        
            }
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
        boolean     accountExists;
        TadoToken   localToken;
        TadoAccount account;
        
        LOG.info("Authenticating user {}", username);
        synchronized (this)
        {
            localToken=null;
            account=accountManager.findAccount(username);
            if (account!=null)
            {
                localToken=account.getToken();
                LOG.info("Account found for {}: acquired {}, expires {} (in {} seconds)", username, account.getLastRefresh(), account.expiryDate(), account.expiresIn());
            }
        }
        if (localToken==null)
        {
            signOut();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
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
                LOG.info("Token acquired for {}: expires in {} seconds", username, localToken.getExpires_in());

                synchronized(this)
                {
                    if (account!=null)
                    {
                        account.setToken(localToken);
                    }
                    else
                    {
                        account=new TadoAccount(username, password, localToken);
                        accountManager.addAccount(account);
                    }
                }
            }
            catch (TadoBadRequestException e)
            {
                LOG.error("Invalid credentials for user {}: {}", username, e.getMessage());
            }
            catch (TadoUnauthorizedException e)
            {
                LOG.error("Error requesting authentication token for user {}: {}", username, e.getMessage());
            }
        }
        
        return localToken;
    }
    
    @Override
    public void reset()
    {
        synchronized(this)
        {
            accountManager.deleteAllAccounts();
        }         
    }

    @Override
    public void signOut()
    {
        synchronized(this)
        {
        }         
    }
     
    /**
     * Request information about me
     * @return The information about me
     */
    @Override
    public TadoMe tadoMe()
    {
        TadoAccount loggedInAccount =accountManager.loggedInAccount();
        TadoMe      me              =accountManager.loggedInAccount().getTadoMe();
        
        if (me==null)
        {
            LOG.info("Requesting Tado ME for user {}", loggedInAccount.getUsername());
            HttpHeaders headers             = new HttpHeaders();
            headers.setBearerAuth(loggedInAccount.getToken().getAccess_token());
            HttpEntity entity               = new HttpEntity(headers);

            me=null;
            try
            {
                ResponseEntity<TadoMe> response = template.exchange("https://my.tado.com/api/v2/me/", HttpMethod.GET, entity, TadoMe.class);        
                me=response.getBody();
                loggedInAccount.setTadoMe(me); // cache account information
            }
            catch (TadoUnauthorizedException e)
            {
                LOG.error("Error requesting Tado account info for user {}: {}", loggedInAccount.getUsername(), e.getMessage());
                // reauthenticate
                loggedInAccount.setToken(null);
                authenticate(loggedInAccount.getUsername(), loggedInAccount.getPassword());
            }
        }
        return me;
    }

    @Override
    public TadoHome tadoHome(int homeId)
    {
        TadoHome home=null;
        TadoAccount account=accountManager.findAccount(homeId);
        if (account!=null)
        {
            LOG.info("Requesting Tado HOME by user {} using account {}", accountManager.loggedInUsername(), account.getUsername());
            HttpHeaders headers             = new HttpHeaders();
            headers.setBearerAuth(account.getToken().getAccess_token());
            HttpEntity entity               = new HttpEntity(headers);
            ResponseEntity<TadoHome> response = template.exchange("https://my.tado.com/api/v2/homes/"+homeId, HttpMethod.GET, entity, TadoHome.class);        
            home=response.getBody();
        }
        else
        {
            LOG.error("No account found for home {} requested by user ", homeId, accountManager.loggedInUsername());
        }
        return home;        
    }

    @Override
    public List<TadoZone> tadoZones(int homeId)
    {
        TadoAccount account=accountManager.findAccount(homeId);

        LOG.info("Requesting Tado ZONES for user {}, home {}", account.getUsername(), homeId);
        HttpHeaders headers             = new HttpHeaders();
        headers.setBearerAuth(account.getToken().getAccess_token());
        HttpEntity entity               = new HttpEntity(headers);
        ResponseEntity<TadoZone[]> response = template.exchange("https://my.tado.com/api/v2/homes/"+homeId+"/zones", HttpMethod.GET, entity, TadoZone[].class);        
        TadoZone[] zones=response.getBody();
        return Arrays.stream(zones).collect(Collectors.toList());             
    }
    
    @Override
    public TadoState tadoState(int homeId)
    {
        TadoAccount account=accountManager.findAccount(homeId);

        LOG.info("Requesting Tado STATE for user {}, home {}", account.getUsername(), homeId);
        HttpHeaders headers             = new HttpHeaders();
        headers.setBearerAuth(account.getToken().getAccess_token());
        HttpEntity entity               = new HttpEntity(headers);
        ResponseEntity<TadoState> response = template.exchange("https://my.tado.com/api/v2/homes/"+homeId+"/state", HttpMethod.GET, entity, TadoState.class);        
        TadoState state=response.getBody();
        return state;         
    }
    
    @Override
    public TadoZoneState tadoZoneState(int homeId, int zoneId)
    {
        TadoAccount account=accountManager.findAccount(homeId);

        LOG.info("Requesting Tado ZONES for user {}, home {}, zone {}", account.getUsername(), homeId, zoneId);
        HttpHeaders headers             = new HttpHeaders();
        headers.setBearerAuth(account.getToken().getAccess_token());
        HttpEntity entity               = new HttpEntity(headers);
        ResponseEntity<TadoZoneState> response = template.exchange("https://my.tado.com/api/v2/homes/"+homeId+"/zones/"+zoneId+"/state", 
                                                                   HttpMethod.GET, entity, TadoZoneState.class);        
        TadoZoneState state=response.getBody();
        return state;            
    }
    
    @Override
    public void setTadoPresence(int homeId, TadoPresence.TadoHomePresence presence)
    {
        TadoAccount account=accountManager.findAccount(homeId);

        LOG.info("Set Tado PRESENCE for user {}, home {}", account.getUsername(), homeId);
        TadoPresence thePresence;
        
        HttpHeaders headers             = new HttpHeaders();
        thePresence=new TadoPresence();
        thePresence.setHomePresence(presence);
        headers.setBearerAuth(account.getToken().getAccess_token());
        HttpEntity<TadoPresence> entity        = new HttpEntity<>(thePresence, headers);
        ResponseEntity<String> response = template.exchange("https://my.tado.com/api/v2/homes/"+homeId+"/presenceLock", 
                                                            HttpMethod.PUT, entity, String.class);        
    }
    
    @Override
    public TadoOverlay tadoOverlay(int homeId, int zoneId)
    {
        TadoAccount account=accountManager.findAccount(homeId);

        LOG.info("Request Tado OVERLAY for user {}, home {}, zone {}", account.getUsername(), homeId, zoneId);
        return null;
    }
    
    @Override
    public TadoOverlay setTadoOverlay(int homeId, int zoneId, TadoOverlay overlay)
    {
        TadoAccount account=accountManager.findAccount(homeId);

        LOG.info("Set Tado OVERLAY for user {}, home {}, zone {}", account.getUsername(), homeId, zoneId);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(account.getToken().getAccess_token());
        HttpEntity<TadoOverlay> entity = new HttpEntity<>(overlay, headers);
        ResponseEntity<TadoOverlay> response = template.exchange("https://my.tado.com/api/v2/homes/"+homeId+"/zones/"+zoneId+"/overlay", 
                                                            HttpMethod.PUT, entity, TadoOverlay.class); 
        
        return response.getBody();
    }
    
    public TadoOverlay setTadoOverlay(int homeId, int zoneId, ZoneType type, State state, double temperature, Termination termination, int timerSeconds)
    {
        TadoAccount account=accountManager.findAccount(homeId);

        LOG.info("Set Tado OVERLAY for user {}, home {}, zone {}", account.getUsername(), homeId, zoneId);

        TadoTemperature temp=new TadoTemperature();
        temp.setCelsius(temperature);
        temp.setFahrenheit((temperature * 9.0/5.0) + 32.0);
        TadoSetting setting=new TadoSetting();
        switch (state)
        {
            case OFF:
                setting.setPower("OFF");
                break;
            case ON:
                setting.setPower("ON");
                setting.setTemperature(temp);
                break;
        }
        switch (type)
        {
            case HEATING:
                setting.setType("HEATING");
                break;
            case HOTWATER:
                setting.setType("HOT_WATER");
                break;
        }
        TadoTermination term=new TadoTermination();
        TadoOverlay overlay=new TadoOverlay();
        switch(termination)
        {
            case TIMER:
                term.setTypeSkillBasedApp("TIMER");
                term.setDurationInSeconds(timerSeconds);
                break;
            case NEXTTIMEBLOCK:
                term.setTypeSkillBasedApp("NEXT_TIME_BLOCK");
                break;
            case INFINITE:
                term.setTypeSkillBasedApp("MANUAL");
                overlay.setType("MANUAL");
                break;
        }
        overlay.setSetting(setting);
        overlay.setTermination(term);
        
        TadoOverlay response=setTadoOverlay(homeId, zoneId, overlay);
        return response;
    }


    @Override
    public void deleteTadoOverlay(int homeId, int zoneId)
    {
        TadoAccount account=accountManager.findAccount(homeId);

        LOG.info("Delete Tado OVERLAY for user {}, home {}, zone {}", account.getUsername(), homeId, zoneId);
        HttpHeaders headers             = new HttpHeaders();
        headers.setBearerAuth(account.getToken().getAccess_token());
        HttpEntity<TadoPresence> entity        = new HttpEntity<>(headers);
        ResponseEntity<String> response = template.exchange("https://my.tado.com/api/v2/homes/"+homeId+"/zone/"+zoneId, 
                                                            HttpMethod.DELETE, entity, String.class);          
    }
    
    
    @Override
    public List<TadoDevice> tadoDevices(int homeId)
    {
        TadoAccount account=accountManager.findAccount(homeId);

        LOG.info("Requesting Tado ZONES for user {}, home {}", account.getUsername(), homeId);
        HttpHeaders headers             = new HttpHeaders();
        headers.setBearerAuth(account.getToken().getAccess_token());
        HttpEntity entity               = new HttpEntity(headers);
        ResponseEntity<TadoDevice[]> response = template.exchange("https://my.tado.com/api/v1/home/"+homeId+"/devices", 
                                                                   HttpMethod.GET, entity, TadoDevice[].class);        
        TadoDevice[] devices=response.getBody();
        return Arrays.stream(devices).collect(Collectors.toList());            
    }
}
