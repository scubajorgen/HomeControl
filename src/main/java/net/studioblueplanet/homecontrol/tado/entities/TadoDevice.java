/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

import java.util.List;

/**
 *
 * @author jorgen
 */
public class TadoDevice
{
    /**  */
    private String              deviceType;
    /**  */
    private String              serialNo;
    /**  */
    private String              shortSerialNo;
    /**  */
    private String              currentFwVersion;
    /**  */
    private TadoConnectionState connectionState;
    /**  */
    private TadoCharacteristics characteristics;
    /**  */
    private TadoMountingState   mountingState;
    /**  */
    private String              batteryState;
    /**  */
    private List<String>        duties;

    public String getDeviceType()
    {
        return deviceType;
    }

    public void setDeviceType(String deviceType)
    {
        this.deviceType = deviceType;
    }

    public String getSerialNo()
    {
        return serialNo;
    }

    public void setSerialNo(String serialNo)
    {
        this.serialNo = serialNo;
    }

    public String getShortSerialNo()
    {
        return shortSerialNo;
    }

    public void setShortSerialNo(String shortSerialNo)
    {
        this.shortSerialNo = shortSerialNo;
    }

    public String getCurrentFwVersion()
    {
        return currentFwVersion;
    }

    public void setCurrentFwVersion(String currentFwVersion)
    {
        this.currentFwVersion = currentFwVersion;
    }

    public TadoConnectionState getConnectionState()
    {
        return connectionState;
    }

    public void setConnectionState(TadoConnectionState connectionState)
    {
        this.connectionState = connectionState;
    }

    public TadoCharacteristics getCharacteristics()
    {
        return characteristics;
    }

    public void setCharacteristics(TadoCharacteristics characteristics)
    {
        this.characteristics = characteristics;
    }

    public TadoMountingState getMountingState()
    {
        return mountingState;
    }

    public void setMountingState(TadoMountingState mountingState)
    {
        this.mountingState = mountingState;
    }

    public String getBatteryState()
    {
        return batteryState;
    }

    public void setBatteryState(String batteryState)
    {
        this.batteryState = batteryState;
    }

    public List<String> getDuties()
    {
        return duties;
    }

    public void setDuties(List<String> duties)
    {
        this.duties = duties;
    }

    
}
