/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.service;

import net.studioblueplanet.homecontrol.service.entities.Account;

/**
 *
 * @author jorgen
 */
public interface AccountService
{
    /**
     * Get the account information.
     * @return Account information
     */
    public Account getAccount();
}
