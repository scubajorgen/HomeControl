/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

import net.studioblueplanet.homecontrol.tado.entities.TadoBearingFromHome;

/**
 *
 * @author jorgen.van.der.velde
 */
public class TadoLocation
{
    /** Indicates the data is valid (false) or stale (true)*/
    private boolean stale;
    
    /** Indicates whether  (my device) is at home */
    private boolean atHome;
    
    /** Bearing from home */
    private TadoBearingFromHome bearingFromHome;

    /** Distance from home */
    private double relativeDistanceFromHomeFence; 

    public boolean isStale()
    {
        return stale;
    }

    public TadoBearingFromHome getBearingFromHome()
    {
        return bearingFromHome;
    }

    public void setBearingFromHome(TadoBearingFromHome bearingFromHome)
    {
        this.bearingFromHome = bearingFromHome;
    }
    
    public void setStale(boolean stale)
    {
        this.stale = stale;
    }

    public boolean isAtHome()
    {
        return atHome;
    }

    public void setAtHome(boolean atHome)
    {
        this.atHome = atHome;
    }

    public TadoBearingFromHome getBearing()
    {
        return bearingFromHome;
    }

    public void setBearing(TadoBearingFromHome bearing)
    {
        this.bearingFromHome = bearing;
    }

    public double getRelativeDistanceFromHomeFence()
    {
        return relativeDistanceFromHomeFence;
    }

    public void setRelativeDistanceFromHomeFence(double relativeDistanceFromHomeFence)
    {
        this.relativeDistanceFromHomeFence = relativeDistanceFromHomeFence;
    }
}
