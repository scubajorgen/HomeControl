/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado;

import java.util.List;

import net.studioblueplanet.homecontrol.tado.entities.TadoDevice;
import net.studioblueplanet.homecontrol.tado.entities.TadoEmail;
import net.studioblueplanet.homecontrol.tado.entities.TadoHome;
import net.studioblueplanet.homecontrol.tado.entities.TadoLanguage;
import net.studioblueplanet.homecontrol.tado.entities.TadoMe;
import net.studioblueplanet.homecontrol.tado.entities.TadoName;
import net.studioblueplanet.homecontrol.tado.entities.TadoOverlay;
import net.studioblueplanet.homecontrol.tado.entities.TadoPassword;
import net.studioblueplanet.homecontrol.tado.entities.TadoPresence.TadoHomePresence;
import net.studioblueplanet.homecontrol.tado.entities.TadoScheduleBlock;
import net.studioblueplanet.homecontrol.tado.entities.TadoState;
import net.studioblueplanet.homecontrol.tado.entities.TadoTimeTable;
import net.studioblueplanet.homecontrol.tado.entities.TadoToken;
import net.studioblueplanet.homecontrol.tado.entities.TadoZone;
import net.studioblueplanet.homecontrol.tado.entities.TadoZoneState;

/**
 *
 * @author jorgen.van.der.velde
 */
public interface TadoInterface
{
    /**
     * This method authenticates the user based on his/her credentials.
     * An initial access token is acquired. It is periodically refreshed in 
     * the background. Hence, subsequent calls to the Tado API will be provided
     * by a valid token.
     * @param username User name
     * @param password Password
     * @return The access token or null if authentication failed
     */
    public TadoToken        authenticate(String username, String password);
    
    /**
     * Signout logged in user. Remove logged in account.
     */
    public void             signOut();
    
    /**
     * Remove all traced accounts.
     */
    public void             reset();
    
    /**
     * Requests information on the account
     * @return Account information including a list of Home IDs
     */
    public TadoMe           tadoMe();
    
    /**
     * Requests information about the indicated home
     * @param homeId ID of the home
     * @return Information on the homne
     */
    public TadoHome         tadoHome(int homeId);
    
    /**
     * Requests a list of zones including the devices in them
     * @param homeId Id of the home to request the zones for
     * @return Information on the zones and devices
     */
    public List<TadoZone>   tadoZones(int homeId);

    /**
     * Requests the state of the home, whether the owner is at home or not
     * @param homeId Id of the home
     * @return The tado state
     */
    public TadoState        tadoPresence(int homeId);
    
    /**
     * Request the information of a zone
     * @param homeId Home ID
     * @param zoneId Zone ID
     * @return The zone state
     */
    public TadoZoneState    tadoZoneState(int homeId, int zoneId);

    /**
     * Sets the presence to HOME or AWAY
     * @param presence The presence
     */
    public void             setTadoPresence(int homeId, TadoHomePresence presence);
    
    /**
     * Return the current overlay of the zone
     * @param homeId Home ID
     * @param zoneId Zone ID
     * @return The overlay if present or 404
     */
    public TadoOverlay      tadoOverlay(int homeId, int zoneId);
    
    /**
     * Set an overlay to the zone, defining parameters overruling the schedule
     * @param homeId Home ID
     * @param zoneId Zone ID
     * @param overlay The overlay defining the overruling of the schedule
     * @return The overlay as obtained as response from Tado
     */
    public TadoOverlay      setTadoOverlay(int homeId, int zoneId, TadoOverlay overlay);

    /**
     * Overrules the automatic settings of a zone by setting an overlay
     * @param homeId Home ID
     * @param zoneId Zone ID
     * @param type HEATING for heating zones, HOT_WATER for hot water zone
     * @param power ON or OFF to switch the zone on or off
     * @param temperature Temperature in Celsius
     * @param termination Termination: MANUAL, TIMER, NEXT_TIME_BLOCK
     * @param timerSeconds In case of TIMER: the timeout seconds
     * @return The overlay as obtained as response from Tado
     */
     public TadoOverlay setTadoOverlay(int homeId, int zoneId, String type, String power, double temperature, String termination, int timerSeconds);

    
    /**
     * Delete the overlay, resume schedule
     * @param homeId Home ID
     * @param zoneId Zone ID
     */
    public void             deleteTadoOverlay(int homeId, int zoneId);
    
    /**
     * List all Tado devices registered to the home. Uses V1 of Tado API
     * @param homeId Home ID
     * @return List of devices
     */
    public List<TadoDevice> tadoDevices(int homeId);
    
    /**
     * Change the name of the user registered with the account
     * @param name The name of the user
     * @return TadoMe instance
     */
    public TadoMe           setName(TadoName name);
    
    /**
     * Set the language for the user
     * @param language The language
     */
    public void             setLanguage(TadoLanguage language);
    
    /**
     * Sets the email of the user. The email is also used as the 
     * username, e.g. for logging in 
     * @param email Email
     * @return TadoMe instance containing the changed email
     */
    public TadoMe           setEmail(TadoEmail email);

    /**
     * Sets the password
     * @param password The password
     */
    public void             setPassword(TadoPassword password);
    
    /**
     * Get the available time table types for this zone. Usually:
     * 0 - ONE_DAY, 1 - THREE_DAY or 2 - SEVEN_DAY
     * @param homeId Home ID
     * @param zoneId Zone ID
     * @return List of available time tables type
     */
    public List<TadoTimeTable>      tadoTimeTables(int homeId, int zoneId);
    
    /**
     * Get the chosen time table type for this zone
     * @param homeId Home ID
     * @param zoneId Zone ID
     * @return The chosen time table type
     */
    public TadoTimeTable            tadoActiveTimeTable(int homeId, int zoneId);
    
    /**
     * Request the list of blocks making up the current schedule
     * @param homeId Home ID
     * @param zoneId Zone ID
     * @return List of blocks
     */
    public List<TadoScheduleBlock>  tadoScheduleBlocks(int homeId, int zoneId, int timeTableId);  
}
