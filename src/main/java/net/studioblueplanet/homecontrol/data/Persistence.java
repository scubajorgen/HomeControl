/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.data;

import java.util.List;
import net.studioblueplanet.homecontrol.service.entities.FriendAccount;
import net.studioblueplanet.homecontrol.tado.entities.TadoAccount;

/**
 *
 * @author jorgen
 */
public interface Persistence
{
    /** 
     * Store the friend account relations to file
     * @param friendAccounts Relations to store
     */
    public void storeFriends(List<FriendAccount> friendAccounts);
    
    /**
     * Read friend account relations from file
     * @return A list of relations
     */
    public List<FriendAccount> restoreFriends();

    /**
     * Store the tado accounts to json file
     * @param accounts The accounts to store
     */
    public void storeAccounts(List<TadoAccount> accounts);

    /**
     * Read the tado accounts from json file
     * @return The list of accounts read or null if no accounts available
     */
    public List<TadoAccount> restoreAccounts();    
}
