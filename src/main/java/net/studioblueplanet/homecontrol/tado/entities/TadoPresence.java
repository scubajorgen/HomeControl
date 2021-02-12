/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

/**
 *
 * @author jorgen
 */
public class TadoPresence
{
    public enum TadoHomePresence
    {
        HOME("HOME"),
        AWAY("AWAY");
        
        private final String description;
        
        TadoHomePresence(String description)
        {
            this.description=description;
        }
        
        @Override
        public String toString()
        {
            return description;
        }
    }
    
    private String homePresence;

    public String getHomePresence()
    {
        return homePresence;
    }

    public void setHomePresence(TadoHomePresence homePresence)
    {
        this.homePresence = homePresence.toString();
    }

}
