/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import net.studioblueplanet.homecontrol.tado.entities.TadoAccount;
import net.studioblueplanet.homecontrol.tado.entities.TadoDevice;
import net.studioblueplanet.homecontrol.tado.entities.TadoEmail;
import net.studioblueplanet.homecontrol.tado.entities.TadoHome;
import net.studioblueplanet.homecontrol.tado.entities.TadoLanguage;
import net.studioblueplanet.homecontrol.tado.entities.TadoMe;
import net.studioblueplanet.homecontrol.tado.entities.TadoName;
import net.studioblueplanet.homecontrol.tado.entities.TadoOverlay;
import net.studioblueplanet.homecontrol.tado.entities.TadoPassword;
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
                catch (TadoException e)
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
        TadoToken   token;
        TadoAccount account;
        
        LOG.info("Authenticating user {}", username);
        
        token=null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", "tado-web-app");
        requestBody.add("scope", "home.user");
        requestBody.add("grant_type", "password");
        requestBody.add("username", username);
        requestBody.add("password", password);
        requestBody.add("client_secret", "wZaRN7rpjn3FoNyF5IFuxg9uMzYJcvOoQ8QWiIqS3hfk6gLhVlG57j5YNoZL2Rtc");

        HttpEntity formEntity = new HttpEntity<>(requestBody, headers);
        try
        {
            token = template.postForObject("https://auth.tado.com/oauth/token", formEntity, TadoToken.class);
            if (token!=null)
            {
                LOG.info("Token acquired for {}: expires in {} seconds", username, token.getExpires_in());
                synchronized(this)
                {
                    account=accountManager.findAccount(username);
                    if (account!=null)
                    {
                        LOG.info("Account found for {}: acquired {}, expires {} (in {} seconds)", username, account.getLastRefresh(), account.expiryDate(), account.expiresIn());
                        account.setToken(token);
                    }
                    else
                    {
                        LOG.info("Account not found for {}, creating new account", username);
                        account=new TadoAccount(username, password, token);
                        accountManager.addAccount(account);
                    }
                }
            }
        }
        catch (TadoException e)
        {
            if (e.getType()==TadoException.TadoExceptionType.CLIENT_ERROR)
            {
                if (e.getStatusCode()==400)
                {
                    LOG.error("Invalid credentials for user {}: {}", username, e.getMessage());
                }
                else if (e.getStatusCode()==401)
                {
                    LOG.error("Error requesting authentication token for user {}: {}", username, e.getMessage());
                }
                else
                {
                    LOG.error("Unexpected Error requesting authentication token for user {}: {}", username, e.getMessage());
                }
            }
        }
        return token;
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
        TadoMe      me              =loggedInAccount.getTadoMe();
        
        if (me==null)
        {
            LOG.info("Requesting Tado ME for user {}", loggedInAccount.getUsername());
            HttpHeaders headers             = new HttpHeaders();
            headers.setBearerAuth(loggedInAccount.getToken().getAccess_token());
            HttpEntity entity               = new HttpEntity(headers);
            ResponseEntity<TadoMe> response = template.exchange("https://my.tado.com/api/v2/me/", HttpMethod.GET, entity, TadoMe.class);        
            me=response.getBody();
            loggedInAccount.setTadoMe(me); // cache account information
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
            LOG.error("No account found for home {} requested by user {}", homeId, accountManager.loggedInUsername());
            throw new TadoException(TadoException.TadoExceptionType.APPLICATION_ERROR, "No account found for home "+homeId+". Unauthorized access.", 0);
        }
        return home;        
    }

    @Override
    public List<TadoZone> tadoZones(int homeId)
    {
        TadoAccount account=accountManager.findAccount(homeId);
        List<TadoZone> zones=null;
        if (account!=null)
        {
            LOG.info("Requesting Tado ZONES for user {}, home {}", account.getUsername(), homeId);
            HttpHeaders headers             = new HttpHeaders();
            headers.setBearerAuth(account.getToken().getAccess_token());
            HttpEntity entity               = new HttpEntity(headers);
            ResponseEntity<TadoZone[]> response = template.exchange("https://my.tado.com/api/v2/homes/"+homeId+"/zones", HttpMethod.GET, entity, TadoZone[].class);        
            zones=Arrays.stream(response.getBody()).collect(Collectors.toList());
        }
        else
        {
            LOG.error("No account found for home {} requested by user {}", homeId, accountManager.loggedInUsername());
            throw new TadoException(TadoException.TadoExceptionType.APPLICATION_ERROR, "No account found for home "+homeId+". Unauthorized access.", 0);            
        }
        return zones;             
    }
    
    @Override
    public TadoState tadoPresence(int homeId)
    {
        TadoAccount account=accountManager.findAccount(homeId);
        TadoState state=null;   
        
        if (account!=null)
        {
        LOG.info("Requesting Tado PRESENCE for user {}, home {}", account.getUsername(), homeId);
            HttpHeaders headers             = new HttpHeaders();
            headers.setBearerAuth(account.getToken().getAccess_token());
            HttpEntity entity               = new HttpEntity(headers);
            ResponseEntity<TadoState> response = template.exchange("https://my.tado.com/api/v2/homes/"+homeId+"/state", 
                                                                   HttpMethod.GET, entity, TadoState.class);        
            state=response.getBody();
        }
        else
        {
            LOG.error("No account found for home {} requested by user {}", homeId, accountManager.loggedInUsername());
            throw new TadoException(TadoException.TadoExceptionType.APPLICATION_ERROR, "No account found for home "+homeId+". Unauthorized access.", 0);            
        }        
        return state;         
    }
    
    @Override
    public TadoZoneState tadoZoneState(int homeId, int zoneId)
    {
        TadoAccount     account =accountManager.findAccount(homeId);
        TadoZoneState   state   =null;

        if (account!=null)
        {
            LOG.info("Requesting Tado ZONES for user {}, home {}, zone {}", account.getUsername(), homeId, zoneId);
            HttpHeaders headers             = new HttpHeaders();
            headers.setBearerAuth(account.getToken().getAccess_token());
            HttpEntity entity               = new HttpEntity(headers);
            ResponseEntity<TadoZoneState> response = template.exchange("https://my.tado.com/api/v2/homes/"+homeId+"/zones/"+zoneId+"/state", 
                                                                       HttpMethod.GET, entity, TadoZoneState.class);        
            state=response.getBody();
        }
        else
        {
            LOG.error("No account found for home {} requested by user {}", homeId, accountManager.loggedInUsername());
            throw new TadoException(TadoException.TadoExceptionType.APPLICATION_ERROR, "No account found for home "+homeId+". Unauthorized access.", 0);            
        } 
        return state;            
    }
    
    @Override
    public void setTadoPresence(int homeId, TadoPresence.TadoHomePresence presence)
    {
        TadoAccount account=accountManager.findAccount(homeId);


        if (account!=null)
        {
            LOG.info("Set Tado PRESENCE for user {}, home {}: {}", account.getUsername(), homeId, presence.toString());
            TadoPresence thePresence;

            HttpHeaders headers             = new HttpHeaders();
            thePresence=new TadoPresence();
            thePresence.setHomePresence(presence);
            headers.setBearerAuth(account.getToken().getAccess_token());
            HttpEntity<TadoPresence> entity        = new HttpEntity<>(thePresence, headers);
            ResponseEntity<String> response = template.exchange("https://my.tado.com/api/v2/homes/"+homeId+"/presenceLock", 
                                                                HttpMethod.PUT, entity, String.class);
        }
        else
        {
            LOG.error("No account found for home {} requested by user {}", homeId, accountManager.loggedInUsername());
            throw new TadoException(TadoException.TadoExceptionType.APPLICATION_ERROR, "No account found for home "+homeId+". Unauthorized access.", 0);            
        } 
    }
    
    @Override
    public TadoOverlay tadoOverlay(int homeId, int zoneId)
    {
        TadoAccount account=accountManager.findAccount(homeId);
        TadoOverlay overlay=null;

        if (account!=null)
        {
            LOG.info("Request Tado OVERLAY for user {}, home {}, zone {}", account.getUsername(), homeId, zoneId);
            HttpHeaders headers             = new HttpHeaders();
            headers.setBearerAuth(account.getToken().getAccess_token());
            HttpEntity entity               = new HttpEntity(headers);
            ResponseEntity<TadoOverlay> response = template.exchange("https://my.tado.com/api/v2/homes/"+homeId+"/zones/"+zoneId+"/overlay", 
                                                                       HttpMethod.GET, entity, TadoOverlay.class);        
            overlay=response.getBody();
        }
        else
        {
            LOG.error("No account found for home {} requested by user {}", homeId, accountManager.loggedInUsername());
            throw new TadoException(TadoException.TadoExceptionType.APPLICATION_ERROR, "No account found for home "+homeId+". Unauthorized access.", 0);            
        } 
        return overlay;            
    }
    
    @Override
    public TadoOverlay setTadoOverlay(int homeId, int zoneId, TadoOverlay overlay)
    {
        TadoAccount  account        =accountManager.findAccount(homeId);
        TadoOverlay  responseOverlay=null;
        if (account!=null)
        {
            LOG.info("Set Tado OVERLAY for user {}, home {}, zone {}", account.getUsername(), homeId, zoneId);
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(account.getToken().getAccess_token());
            HttpEntity<TadoOverlay> entity = new HttpEntity<>(overlay, headers);
            ResponseEntity<TadoOverlay> response = template.exchange("https://my.tado.com/api/v2/homes/"+homeId+"/zones/"+zoneId+"/overlay", 
                                                   HttpMethod.PUT, entity, TadoOverlay.class); 
            responseOverlay=response.getBody();
        }
        else
        {
            LOG.error("No account found for home {} requested by user {}", homeId, accountManager.loggedInUsername());
            throw new TadoException(TadoException.TadoExceptionType.APPLICATION_ERROR, "No account found for home "+homeId+". Unauthorized access.", 0);            
        }        
        return responseOverlay;
    }
    
    @Override
    public TadoOverlay setTadoOverlay(int homeId, int zoneId, String type, String power, double temperature, String termination, int timerSeconds)
    {
        TadoTemperature temp=new TadoTemperature();
        temp.setCelsius(temperature);
        temp.setFahrenheit((temperature * 9.0/5.0) + 32.0);
        TadoSetting setting=new TadoSetting();

        setting.setPower(power);
        if (power.equals("ON"))
        {
           setting.setTemperature(temp);            
        }
        else if (power.equals("OFF"))
        {
            
        }
        else
        {
            LOG.error("Error invalid value for overlay setting for power: {}", power);
        }

        setting.setType(type);
        if (type.equals("HEATING"))
        {
            
        }
        else if (type.equals("HOT_WATER"))
        {
            
        }
        else
        {
            LOG.error("Error invalid value for overlay setting for type: {}", type);
        }
        
        TadoTermination term=new TadoTermination();
        TadoOverlay overlay=new TadoOverlay();
        term.setTypeSkillBasedApp(termination);

        if (termination.equals("TIMER"))
        {
            term.setDurationInSeconds(timerSeconds);
        }
        else if (termination.equals("NEXT_TIME_BLOCK"))
        {
            
        }
        else if (termination.equals("MANUAL"))
        {
            overlay.setType("MANUAL");
        }
        else
        {
            LOG.error("Error invalid value for overlay setting for termination: {}", termination);
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

        if (account!=null)
        {
            LOG.info("Delete Tado OVERLAY for user {}, home {}, zone {}", account.getUsername(), homeId, zoneId);
            HttpHeaders headers             = new HttpHeaders();
            headers.setBearerAuth(account.getToken().getAccess_token());
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity<String> response = template.exchange("https://my.tado.com/api/v2/homes/"+homeId+"/zones/"+zoneId+"/overlay", 
                                                                HttpMethod.DELETE, entity, String.class);  
        }
        else
        {
            LOG.error("No account found for home {} requested by user {}", homeId, accountManager.loggedInUsername());
            throw new TadoException(TadoException.TadoExceptionType.APPLICATION_ERROR, "No account found for home "+homeId+". Unauthorized access.", 0);            
        }         
    }
    
    
    @Override
    public List<TadoDevice> tadoDevices(int homeId)
    {
        TadoAccount account=accountManager.findAccount(homeId);

        if (account!=null)
        {
            LOG.info("Requesting Tado ZONES for user {}, home {}", account.getUsername(), homeId);
            HttpHeaders headers             = new HttpHeaders();
            headers.setBearerAuth(account.getToken().getAccess_token());
            HttpEntity entity               = new HttpEntity(headers);
            ResponseEntity<TadoDevice[]> response = template.exchange("https://my.tado.com/api/v1/home/"+homeId+"/devices", 
                                                                       HttpMethod.GET, entity, TadoDevice[].class);        
            TadoDevice[] devices=response.getBody();
            return Arrays.stream(devices).collect(Collectors.toList());
        }
        else
        {
            LOG.error("No account found for home {} requested by user {}", homeId, accountManager.loggedInUsername());
            throw new TadoException(TadoException.TadoExceptionType.APPLICATION_ERROR, "No account found for home "+homeId+". Unauthorized access.", 0);            
        }         
    }

    @Override
    public TadoMe setName(TadoName name)
    {
        TadoAccount account=accountManager.loggedInAccount();
        
        LOG.info("Set name for user {} to {}", account.getUsername(), name.getName());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(account.getToken().getAccess_token());
        HttpEntity<TadoName> entity = new HttpEntity<>(name, headers);
        ResponseEntity<TadoMe> response = template.exchange("https://my.tado.com/api/v2/users/"+account.getUsername()+"/name", 
                                               HttpMethod.PUT, entity, TadoMe.class); 
        // Validate the name change
        TadoMe me=response.getBody();   
        if (!name.getName().equals(me.getName()))
        {
            throw new TadoException(TadoException.TadoExceptionType.APPLICATION_ERROR, "Setting name failed", 0);
        }
        account.setTadoMe(me);
        return me;        
    }
    
    @Override
    public void setLanguage(TadoLanguage language)
    {
        TadoAccount account=accountManager.loggedInAccount();
        
        LOG.info("Set languave for user {} to {}", account.getUsername(), language.getLanguage());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(account.getToken().getAccess_token());
        HttpEntity<TadoLanguage> entity = new HttpEntity<>(language, headers);
        ResponseEntity response = template.exchange("https://my.tado.com/api/v2/users/"+account.getUsername()+"/language", 
                                               HttpMethod.PUT, entity, String.class); 
    }

    public TadoMe setEmail(TadoEmail email)
    {
        TadoAccount account=accountManager.loggedInAccount();
        
        LOG.info("Set email for user {} to {}", account.getUsername(), email.getEmail());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(account.getToken().getAccess_token());
        HttpEntity<TadoEmail> entity = new HttpEntity<>(email, headers);
        ResponseEntity<TadoMe> response = template.exchange("https://my.tado.com/api/v2/users/"+account.getUsername()+"/email", 
                                               HttpMethod.PUT, entity, TadoMe.class); 
        // Validate the name change
        TadoMe me=response.getBody();        
        if (!email.getEmail().equals(me.getEmail()) || !email.getEmail().equals(me.getEmail()))
        {
            throw new TadoException(TadoException.TadoExceptionType.APPLICATION_ERROR, "Setting name failed", 0);
        }
        account.setTadoMe(me);
        return me;          
    }
    
    @Override
    public void setPassword(TadoPassword password)
    {
        TadoAccount account=accountManager.loggedInAccount();
        
        LOG.info("Set password for user {} to ...", account.getUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(account.getToken().getAccess_token());
        HttpEntity<TadoPassword> entity = new HttpEntity<>(password, headers);
        ResponseEntity response = template.exchange("https://my.tado.com/api/v2/users/"+account.getUsername()+"/password", 
                                               HttpMethod.PUT, entity, String.class);         
    }
}
