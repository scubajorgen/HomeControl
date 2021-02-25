/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado;

import java.util.List;
import net.studioblueplanet.homecontrol.tado.entities.TadoAccount;

/**
 *
 * @author jorgen
 */
public interface TadoAccountManager
{
    
    /**
     * Indicates if current logged in user has an account
     * @return True if authorized
     */
    public boolean isAuthorized();
    
    /**
     * Find the account if it already exists.
     * @param username Username to find
     * @return The account or null if it not exists
     */
    public TadoAccount      findAccount(String username);
    
    /**
     * Find the account that is being used for access to indicated home
     * @param homeId ID Of the home
     * @return The account to use or null if no account available
     */
    public TadoAccount findAccount(int homeId);

    /**
     * Retrieves the TadoAccount of currently logged in user
     * @return The account
     */
    public TadoAccount      loggedInAccount();
    
    /**
     * Returns the username of the logged in user
     * @return The username or null if account not foud
     */
    public String           loggedInUsername();

    /**
     * Get the list of accounts
     * @return List of accounts
     */
    public List<TadoAccount> getAccounts();    
    
    /**
     * Add new account
     * @param account Account to add
     */
    public void addAccount(TadoAccount account);
    
    /**
     * Remove account based on account username
     * @param username Username
     */
    public void deleteAccount(String username);
    
    /**
     * Remove all existing accounts
     */
    public void deleteAllAccounts();
            
}
