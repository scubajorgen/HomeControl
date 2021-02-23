/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.service;

import java.util.List;
import net.studioblueplanet.homecontrol.service.entities.Account;
import net.studioblueplanet.homecontrol.service.entities.FriendAccount;
import net.studioblueplanet.homecontrol.service.entities.HomeId;

/**
 *
 * @author jorgen
 */
public interface AccountService
{
    /** 
     * Clear the relations between accounts.
     */
    public void             reset();
    
    /**
     * Get the account information.
     * @return Account information
     */
    public Account          getAccount();
    
    /**
     * Add yourself to a friend account. You give access to your home.
     * @param friendAccountUsername Friend user name
     */
    public void             addToFriendAccount(String friendAccountUsername);
    
    /**
     * Get a list of friend accounts that have given access to their home.
     * @return List of user names
     */
    public List<String>     getFriendAccountUsernames();
    
    /**
     * Retrieve a list of available home IDs for this account. The list consists
     * of own homes and friend homes (only if the friend account has been logged in)
     * @return List of home IDs
     */
    public List<HomeId>     getAvailableHomes();
}
