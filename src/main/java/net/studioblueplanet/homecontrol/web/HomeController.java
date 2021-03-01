/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.web;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.server.ResponseStatusException;

import net.studioblueplanet.homecontrol.service.AccountService;
import net.studioblueplanet.homecontrol.service.HomeService;
import net.studioblueplanet.homecontrol.service.ServiceException;
import net.studioblueplanet.homecontrol.service.entities.Account;
import net.studioblueplanet.homecontrol.service.entities.Device;
import net.studioblueplanet.homecontrol.service.entities.Home;
import net.studioblueplanet.homecontrol.service.entities.Overlay;
import net.studioblueplanet.homecontrol.service.entities.Presence;
import net.studioblueplanet.homecontrol.service.entities.Zone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.studioblueplanet.homecontrol.tado.TadoException;
/**
 *
 * @author jorgen.van.der.velde
 */
@RestController
@RequestMapping("/api")
public class HomeController 
{
    private static final Logger     LOG = LoggerFactory.getLogger(HomeController.class);
    
    @Autowired
    AccountService accountService;

    @Autowired
    HomeService homeService;
    
    /**
     * Example method. Simply echos the account information
     * @return Information about my account
     */
    @RequestMapping("/account")
    public ResponseEntity<Account> account() 
    {
        LOG.info("API: account info requested");
        ResponseEntity<Account> accountResponse=null;
        
        try
        {
            Account account=accountService.getAccount();
            if (account!=null)
            {
                accountResponse=ResponseEntity.ok(account);
            }
            else
            {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No account found");
            }
        }
        catch (ServiceException e)
        {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, e.getMessage());
        }
        return accountResponse;
    }

    /**
     * Example method. Simply echos the account information
     * @return Information about my account
     */
    @RequestMapping("/home/{homeId}/presence")
    public ResponseEntity<Presence> state(@PathVariable int homeId) 
    {
        LOG.info("API: presence info requested");
        ResponseEntity<Presence> stateResponse=null;
        Presence                 state;

        try
        {
            Account account=accountService.getAccount();
            if (account!=null)
            {
                state=homeService.getHomeState(account.getOwnHomes().get(0).getId());
                stateResponse=ResponseEntity.ok(state);
            }
            else
            {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No account found");
            }    
        }
        catch (ServiceException e)
        {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, e.getMessage());
        }
        return stateResponse;
    }
    
    @PutMapping("/home/{homeId}/presence")
    public ResponseEntity<String> presence(@PathVariable int homeId, @RequestBody Presence presence) 
    {
        LOG.info("API: presence info set");
        ResponseEntity response=null;
        try
        {
            Account account=accountService.getAccount();
            if (account!=null)

            {
                homeService.setPresence(homeId, presence);
                response=new ResponseEntity(HttpStatus.OK);
            }
            else
            {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No account found");
            }
        }
        catch (ServiceException e)
        {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, e.getMessage());
        }
        return response;
    }

    @RequestMapping("/home/{homeId}")
    public ResponseEntity<Home> home(@PathVariable int homeId) 
    {
        LOG.info("API: home info requested");
        ResponseEntity<Home>    homeResponse=null;
        Home                    home;

        try
        {
            Account account=accountService.getAccount();
            if (account!=null)
            {
                home=homeService.getHome(homeId);
                homeResponse=ResponseEntity.ok(home);
            }
            else
            {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No account found");
            }    
        }
        catch (ServiceException e)
        {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, e.getMessage());
        }
        return homeResponse;
    }
    
    @RequestMapping("/home/{homeId}/zones")
    public ResponseEntity<List<Zone>> zones(@PathVariable int homeId) 
    {
        LOG.info("API: zone info requested");
        ResponseEntity<List<Zone>>  zonesResponse=null;
        List<Zone>                  zones;

        try
        {
            Account account=accountService.getAccount();
            if (account!=null)
            {
                zones=homeService.getZones(homeId);
                zonesResponse=ResponseEntity.ok(zones);
            }
            else
            {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No account found");
            }    
        }
        catch (ServiceException e)
        {
           throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, e.getMessage());            
        }
        return zonesResponse;
    }
    
    @PutMapping("home/{homeId}/zone/{zoneId}/overlay")
    public ResponseEntity<String> overlay(@PathVariable int homeId, @PathVariable int zoneId, @RequestBody Overlay overlay) 
    {
        LOG.info("API: overlay set for home {}, zone {}", homeId, zoneId);
        ResponseEntity response=null;
        try
        {
            Account account=accountService.getAccount();
            if (account!=null)
            {
                homeService.setOverlay(homeId, zoneId, overlay);
                response=new ResponseEntity(HttpStatus.OK);
            }
            else
            {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No account found");
            }
        }
        catch (ServiceException e)
        {
           throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, e.getMessage());            
        }        
        return response;
    }    
    
    @DeleteMapping("home/{homeId}/zone/{zoneId}/overlay")
    public ResponseEntity<String> deleteOverlay(@PathVariable int homeId, @PathVariable int zoneId) 
    {
        LOG.info("API: overlay deleted for home {}, zone {}", homeId, zoneId);
        ResponseEntity response=null;
        try
        {
            Account account=accountService.getAccount();
            if (account!=null)
            {
                homeService.deleteOverlay(homeId, zoneId);
                response=new ResponseEntity(HttpStatus.OK);
            }
            else
            {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No account found");
            }
        }
        catch (ServiceException e)
        {
           throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, e.getMessage());            
        } 
        return response;
    }    

    @RequestMapping("/home/{homeId}/devices")
    public ResponseEntity<List<Device>> devices(@PathVariable int homeId) 
    {
        LOG.info("API: device info requested");
        ResponseEntity<List<Device>>    deviceResponse=null;
        List<Device>                    devices;

        try
        {
            Account account=accountService.getAccount();
            if (account!=null)
            {
                devices=homeService.getDevices(homeId);
                deviceResponse=ResponseEntity.ok(devices);
            }
            else
            {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No account found");
            }    
        }
        catch (ServiceException e)
        {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, e.getMessage());
        }
        return deviceResponse;
    }    
}