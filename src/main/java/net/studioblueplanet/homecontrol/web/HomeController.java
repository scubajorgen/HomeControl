/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import net.studioblueplanet.homecontrol.service.AccountService;
import net.studioblueplanet.homecontrol.service.entities.Account;

import net.studioblueplanet.homecontrol.tado.entities.TadoMe;
import net.studioblueplanet.homecontrol.tado.entities.TadoState;
import net.studioblueplanet.homecontrol.tado.TadoInterface;
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
    TadoInterface tado;
    
    @Autowired
    AccountService accountService;

    
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
    @RequestMapping("/me")
    public ResponseEntity<TadoMe> me() 
    {
        ResponseEntity<TadoMe> meResponse;

        TadoMe me=tado.tadoMe();
        if (me!=null)
        {
            meResponse=ResponseEntity.ok(me);
        }
        else
        {
            meResponse=ResponseEntity.notFound().build();
        }
        return meResponse;
    }

    /**
     * Example method. Simply echos the account information
     * @return Information about my account
     */
    @RequestMapping("/state")
    public ResponseEntity<TadoState> state() 
    {
        ResponseEntity<TadoState> stateResponse;

        TadoMe me=tado.tadoMe();
        if (me!=null)
        {
            TadoState state=tado.tadoState(me.getHomes().get(0).getId());
            stateResponse=ResponseEntity.ok(state);
        }
        else
        {
            stateResponse=ResponseEntity.notFound().build();
        }
        return stateResponse;
    }
    
    @PutMapping("/presence")
    public ResponseEntity<String> presence(@RequestBody String presence) 
    {
        String response;
        
        response="";
        return new ResponseEntity(HttpStatus.OK);
    }
}