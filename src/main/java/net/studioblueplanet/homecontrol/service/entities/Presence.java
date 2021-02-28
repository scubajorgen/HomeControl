/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.service.entities;

import net.studioblueplanet.homecontrol.service.ServiceException;
/**
 *
 * @author jorgen
 */
public class Presence
{
    String presence;

    public String getPresence()
    {
        return presence;
    }

    public void setPresence(String presence)
    {
        if (!presence.equals("AWAY") && !presence.equals("HOME"))
        {
            throw new ServiceException("Illegal presence value "+presence+". Please use AWAY or HOME");
        }
        this.presence = presence;
    }
}
