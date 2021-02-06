/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import net.studioblueplanet.homecontrol.tado.entities.TadoMe;
import net.studioblueplanet.homecontrol.tado.entities.TadoState;
import net.studioblueplanet.homecontrol.tado.TadoInterface;

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
}