/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

/**
 *
 * @author jorgen.van.der.velde
 */
public class TadoBearingFromHome
{
    /** Bearing in degrees (0-360) */
    private double degrees;
    /** Bearing in radians 0-2pi*/
    private double radians;

    public double getDegrees()
    {
        return degrees;
    }

    public void setDegrees(double degrees)
    {
        this.degrees = degrees;
    }

    public double getRadians()
    {
        return radians;
    }

    public void setRadians(double radians)
    {
        this.radians = radians;
    }
    
}
