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
public class TadoZoneState
{
    /** State: HOME or AWAY */
    private String                  tadoMode;
    private boolean                 geolocationOverride;
    private Object                  geolocationOverrideDisableTime;
    private Object                  preparation;
    private TadoZoneSetting         setting;
    private String                  overlayType;
    private Object                  overlay;
    private Object                  openWindow;
    private TadoScheduleChange      nextScheduleChange;
    private TadoTimeBlock           nextTimeBlock;
    private TadoLink                link;
    private TadoDataPointsActivity  activityDataPoints;
    private TadoDataPointsSensor    sensorDataPoints;

    public String getTadoMode()
    {
        return tadoMode;
    }

    public void setTadoMode(String tadoMode)
    {
        this.tadoMode = tadoMode;
    }

    public boolean isGeolocationOverride()
    {
        return geolocationOverride;
    }

    public void setGeolocationOverride(boolean geolocationOverride)
    {
        this.geolocationOverride = geolocationOverride;
    }

    public Object getGeolocationOverrideDisableTime()
    {
        return geolocationOverrideDisableTime;
    }

    public void setGeolocationOverrideDisableTime(Object geolocationOverrideDisableTime)
    {
        this.geolocationOverrideDisableTime = geolocationOverrideDisableTime;
    }

    public Object getPreparation()
    {
        return preparation;
    }

    public void setPreparation(Object preparation)
    {
        this.preparation = preparation;
    }

    public TadoZoneSetting getSetting()
    {
        return setting;
    }

    public void setSetting(TadoZoneSetting setting)
    {
        this.setting = setting;
    }

    public String getOverlayType()
    {
        return overlayType;
    }

    public void setOverlayType(String overlayType)
    {
        this.overlayType = overlayType;
    }

    public Object getOverlay()
    {
        return overlay;
    }

    public void setOverlay(Object overlay)
    {
        this.overlay = overlay;
    }

    public Object getOpenWindow()
    {
        return openWindow;
    }

    public void setOpenWindow(Object openWindow)
    {
        this.openWindow = openWindow;
    }

    public TadoScheduleChange getNextScheduleChange()
    {
        return nextScheduleChange;
    }

    public void setNextScheduleChange(TadoScheduleChange nextScheduleChange)
    {
        this.nextScheduleChange = nextScheduleChange;
    }

    public TadoTimeBlock getNextTimeBlock()
    {
        return nextTimeBlock;
    }

    public void setNextTimeBlock(TadoTimeBlock nextTimeBlock)
    {
        this.nextTimeBlock = nextTimeBlock;
    }

    public TadoLink getLink()
    {
        return link;
    }

    public void setLink(TadoLink link)
    {
        this.link = link;
    }

    public TadoDataPointsActivity getActivityDataPoints()
    {
        return activityDataPoints;
    }

    public void setActivityDataPoints(TadoDataPointsActivity activityDataPoints)
    {
        this.activityDataPoints = activityDataPoints;
    }

    public TadoDataPointsSensor getSensorDataPoints()
    {
        return sensorDataPoints;
    }

    public void setSensorDataPoints(TadoDataPointsSensor sensorDataPoints)
    {
        this.sensorDataPoints = sensorDataPoints;
    }

}
