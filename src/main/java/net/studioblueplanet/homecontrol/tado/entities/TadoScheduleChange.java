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
public class TadoScheduleChange
{
    private Date            start;
    private TadoZoneSetting setting;

    public Date getStart()
    {
        return start;
    }

    public void setStart(Date start)
    {
        this.start = start;
    }

    public TadoZoneSetting getSetting()
    {
        return setting;
    }

    public void setSetting(TadoZoneSetting setting)
    {
        this.setting = setting;
    }
    
}
