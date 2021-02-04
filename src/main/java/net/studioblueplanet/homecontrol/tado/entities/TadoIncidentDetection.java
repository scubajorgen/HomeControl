/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

/**
 * Represents a home incident detection settings
 * @author jorgen
 */
public class TadoIncidentDetection
{
    private boolean supported;
    private boolean enabled;

    public boolean isSupported()
    {
        return supported;
    }

    public void setSupported(boolean supported)
    {
        this.supported = supported;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }
}
