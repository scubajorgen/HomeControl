/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.web;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import net.studioblueplanet.homecontrol.service.AccountService;
import net.studioblueplanet.homecontrol.service.HomeService;
import net.studioblueplanet.homecontrol.service.entities.Account;
import net.studioblueplanet.homecontrol.service.entities.Home;
import net.studioblueplanet.homecontrol.service.entities.HomeState;
import net.studioblueplanet.homecontrol.service.entities.Overlay;
import net.studioblueplanet.homecontrol.service.entities.Zone;

import org.springframework.http.HttpStatus;

/**
 *
 * @author jorgen.van.der.velde
 */
@RestController
@RequestMapping("/api")
public class HomeController 
{
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
        ResponseEntity<Account> accountResponse;

        Account account=accountService.getAccount();
        if (account!=null)
        {
            accountResponse=ResponseEntity.ok(account);
        }
        else
        {
            accountResponse=ResponseEntity.notFound().build();
        }
        return accountResponse;
    }

    /**
     * Example method. Simply echos the account information
     * @return Information about my account
     */
    @RequestMapping("/state/{homeId}")
    public ResponseEntity<HomeState> state(@PathVariable int homeId) 
    {
        ResponseEntity<HomeState> stateResponse;
        HomeState                 state;

        Account account=accountService.getAccount();
        if (account!=null)
        {
            state=homeService.getHomeState(account.getOwnHomes().get(0).getId());
            stateResponse=ResponseEntity.ok(state);
        }
        else
        {
            stateResponse=ResponseEntity.notFound().build();
        }    
        return stateResponse;
    }
    
    @PutMapping("/home/{homeId}/presence")
    public ResponseEntity<String> presence(@PathVariable int homeId, @RequestBody String presence) 
    {
        ResponseEntity response;
        Account account=accountService.getAccount();
        if (account!=null)

        {
            homeService.setPresence(homeId, presence);
            response=new ResponseEntity(HttpStatus.OK);
        }
        else
        {
            response=new ResponseEntity(HttpStatus.PRECONDITION_FAILED);
        }
        return response;
    }

    @RequestMapping("/home/{homeId}")
    public ResponseEntity<Home> home(@PathVariable int homeId) 
    {
        ResponseEntity<Home>    homeResponse;
        Home                    home;

        Account account=accountService.getAccount();
        if (account!=null)
        {
            // TODO: check validity of homeId
            home=homeService.getHome(homeId);
            homeResponse=ResponseEntity.ok(home);
        }
        else
        {
            homeResponse=ResponseEntity.notFound().build();
        }    
        return homeResponse;
    }
    
    @RequestMapping("/home/{homeId}/zones")
    public ResponseEntity<List<Zone>> zones(@PathVariable int homeId) 
    {
        ResponseEntity<List<Zone>>  zonesResponse;
        List<Zone>                  zones;

        Account account=accountService.getAccount();
        if (account!=null)
        {
            zones=homeService.getZones(homeId);
            zonesResponse=ResponseEntity.ok(zones);
        }
        else
        {
            zonesResponse=ResponseEntity.notFound().build();
        }    
        return zonesResponse;
    }
    
    @PutMapping("/overlay")
    public ResponseEntity<String> overlay(@RequestBody Overlay overlay) 
    {
        ResponseEntity response;
        Account account=accountService.getAccount();
        if (account!=null)

        {
            
            response=new ResponseEntity(HttpStatus.OK);
        }
        else
        {
            response=new ResponseEntity(HttpStatus.PRECONDITION_FAILED);
        }
        return response;
    }    
    
}