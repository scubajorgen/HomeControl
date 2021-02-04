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
public class TadoOpenWindowDetection
{
    /** Feature supported */
    private boolean     supported;
    /** Feature enabled */
    private boolean     enabled;
    /** ?? */
    private int         timeoutInSeconds;

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

    public int getTimeoutInSeconds()
    {
        return timeoutInSeconds;
    }

    public void setTimeoutInSeconds(int timeoutInSeconds)
    {
        this.timeoutInSeconds = timeoutInSeconds;
    }
    
}
