/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

import java.util.Date;

/**
 * Indication for the freshness of the home
 * @author jorgen
 */
public class TadoFreshness
{
    /** Indication for the freshness */
    private String  value;
    /** Date time of last open window detection, ISO format */
    private Date    lastOpenWindow;

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public Date getLastOpenWindow()
    {
        return lastOpenWindow;
    }

    public void setLastOpenWindow(Date lastOpenWindow)
    {
        this.lastOpenWindow = lastOpenWindow;
    }
}
