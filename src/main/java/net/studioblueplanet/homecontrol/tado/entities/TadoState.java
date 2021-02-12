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
public class TadoState
{
    /** Presence of the owner: AWAY or HOME */
    private String presence;
    /** ? */
    private boolean presenceLocked;
    /** Show the presence switch button - optional */
    private boolean showHomePresenceSwitchButton;

    public boolean isPresenceLocked()
    {
        return presenceLocked;
    }

    public void setPresenceLocked(boolean presenceLocked)
    {
        this.presenceLocked = presenceLocked;
    }

    public String getPresence()
    {
        return presence;
    }

    public void setPresence(String presence)
    {
        this.presence = presence;
    }

    public boolean isShowHomePresenceSwitchButton()
    {
        return showHomePresenceSwitchButton;
    }

    public void setShowHomePresenceSwitchButton(boolean showHomePresenceSwitchButton)
    {
        this.showHomePresenceSwitchButton = showHomePresenceSwitchButton;
    }
}
