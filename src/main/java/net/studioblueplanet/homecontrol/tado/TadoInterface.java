/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado;

import java.util.List;

import net.studioblueplanet.homecontrol.tado.entities.TadoHome;
import net.studioblueplanet.homecontrol.tado.entities.TadoMe;
import net.studioblueplanet.homecontrol.tado.entities.TadoState;
import net.studioblueplanet.homecontrol.tado.entities.TadoToken;
import net.studioblueplanet.homecontrol.tado.entities.TadoZone;
import org.springframework.web.client.RestTemplate;

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
     * Destroys the token and stops the refresh process
     */
    public void             signOut();
    
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
    public TadoState  tadoState(int homeId);

}
