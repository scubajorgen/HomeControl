/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.service;

import java.util.List;

import net.studioblueplanet.homecontrol.service.entities.Home;
import net.studioblueplanet.homecontrol.service.entities.Presence;
import net.studioblueplanet.homecontrol.service.entities.Overlay;
import net.studioblueplanet.homecontrol.service.entities.Zone;

/**
 *
 * @author jorgen
 */
public interface HomeService
{
    /**
     * Return information about the home
     * @param homeId ID of the home
     * @return Home info
     */
    public Home         getHome(int homeId);
    
    /**
     * Returns whether the owner is home or away
     * @param homeId ID of the home
     * @return The state of the home
     */
    public Presence    getHomeState(int homeId);
    
    /**
     * Sets the presence of the home owner
     * @param homeId ID of the home
     * @param presence Presence: "HOME" or "AWAY"
     */
    public void         setPresence(int homeId, Presence presence);
    
    /**
     * Get info about all zones
     * @param homeId ID of the home
     * @return Info about the zones
     */
    public List<Zone>   getZones(int homeId);
    
    /**
     * Sets manual control
     * @param homeId ID Of the home
     * @param zoneId ID of the zone
     * @param overlay Information about the manual control
     */
    public void         setOverlay(int homeId, int zoneId, Overlay overlay);
    
    /**
     * Delete manual control and switch back to automatic
     * @param homeId ID of the home
     * @param zoneId ID of the zone
     */
    public void         deleteOverlay(int homeId, int zoneId);
}
