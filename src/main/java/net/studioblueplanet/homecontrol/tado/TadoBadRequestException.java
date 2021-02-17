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
public class TadoBadRequestException extends RuntimeException
{
    public TadoBadRequestException()
    {
        super("Bad request or invalid credentials");
    }
}
