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
public class TadoDazzleMode
{
    /** Dazzle mode supported */
    private boolean supported;
    /** Dazzle mode enabled */
    private boolean enabled;

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public boolean isSupported()
    {
        return supported;
    }

    public void setSupported(boolean supported)
    {
        this.supported = supported;
    }
}
