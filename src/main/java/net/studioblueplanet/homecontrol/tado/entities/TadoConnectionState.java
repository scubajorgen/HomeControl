/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

import java.util.Date;

/**
 *
 * @author jorgen
 */
public class TadoConnectionState
{
    /** Connection state */
    private boolean value;
    /** Datetime of last connection in ISO format e.g. 2021-02-04T07:17:19.671Z */
    private Date  timestamp;

    public boolean isValue()
    {
        return value;
    }

    public void setValue(boolean value)
    {
        this.value = value;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Date timeStamp)
    {
        this.timestamp = timeStamp;
    }
}
