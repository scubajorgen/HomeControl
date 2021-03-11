/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

/**
 *
 * @author jorgen
 */
public class TadoPolarCoordinate
{
    /** Radius between 0 and 1 */
    private double radial;
    /** Angle in degrees */
    private int angular;

    public double getRadial()
    {
        return radial;
    }

    public void setRadial(double radial)
    {
        this.radial = radial;
    }

    public int getAngular()
    {
        return angular;
    }

    public void setAngular(int angular)
    {
        this.angular = angular;
    }
}
