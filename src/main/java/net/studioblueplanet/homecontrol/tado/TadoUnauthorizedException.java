/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado;


/**
 *
 * @author jorgen
 */
public class TadoUnauthorizedException extends RuntimeException
{
    public TadoUnauthorizedException()
    {
        super("Tado does not authorize the request");
    }    
}
