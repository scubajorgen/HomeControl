/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

import java.util.Date;
import java.time.LocalTime;

/**
 *
 * @author jorgen
 */
public class TadoScheduleBlock
{
    /** Day indication, like MONDAY_TO_SUNDAY/MONDAY_TO_FRIDAY, SATURDAY, SUNDAY/MONDAY, TUESDAY, WEDNESSDAY, ... */
    private String      dayType;
    /** Start time of block, like "00:00", "08:30" or "23:00" */
    private LocalTime   start;
    private LocalTime   end;
    private boolean     geolocationOverride;
    private TadoSetting setting;

    public String getDayType()
    {
        return dayType;
    }

    public void setDayType(String dayType)
    {
        this.dayType = dayType;
    }

    public LocalTime getStart()
    {
        return start;
    }

    public void setStart(LocalTime start)
    {
        this.start = start;
    }

    public LocalTime getEnd()
    {
        return end;
    }

    public void setEnd(LocalTime end)
    {
        this.end = end;
    }

    public boolean isGeolocationOverride()
    {
        return geolocationOverride;
    }

    public void setGeolocationOverride(boolean geolocationOverride)
    {
        this.geolocationOverride = geolocationOverride;
    }

    public TadoSetting getSetting()
    {
        return setting;
    }

    public void setSetting(TadoSetting setting)
    {
        this.setting = setting;
    }
}
