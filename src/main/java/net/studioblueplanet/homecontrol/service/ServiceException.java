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
    public ServiceException(String message)
    {
        super(message);
    }
}
