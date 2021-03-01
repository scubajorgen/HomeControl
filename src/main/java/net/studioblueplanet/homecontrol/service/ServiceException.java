/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.service;

/**
 *
 * @author jorgen
 */
public class ServiceException extends RuntimeException
{
    private String userAccount="xx";
    
    public ServiceException(String message)
    {
        super(message);
    }

    public String getUserAccount()
    {
        return userAccount;
    }
}
