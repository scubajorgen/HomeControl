/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.service;

import net.studioblueplanet.homecontrol.service.entities.Account;
import net.studioblueplanet.homecontrol.service.entities.Home;
import net.studioblueplanet.homecontrol.service.entities.HomeState;

/**
 *
 * @author jorgen
 */
public interface HomeService
{
    public Home getHome(int homeId);
    
    public HomeState getHomeState(int homeId);
    
    public void setPresence(int homeId, String presence);
}
