/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.service;

import net.studioblueplanet.homecontrol.service.entities.Account;
import net.studioblueplanet.homecontrol.service.entities.Home;
import net.studioblueplanet.homecontrol.tado.TadoInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 *
 * @author jorgen
 */
@Component
public class HomeServiceImpl implements HomeService
{
    @Autowired
    private TadoInterface   tado;
    
    private Account         account;
    
    @Override
    public Home getHome()
    {
        return null;
    }
    
}
