/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.service.entities;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 *
 * @author jorgen
 */
public class Device
{
    /**  */
    private String              deviceType;
    /**  */
    private String              serialNo;

    private boolean             connected;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date                lastConnected;

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

    public boolean isConnected()
    {
        return connected;
    }

    public void setConnected(boolean connected)
    {
        this.connected = connected;
    }

    public Date getLastConnected()
    {
        return lastConnected;
    }

    public void setLastConnected(Date lastConnected)
    {
        this.lastConnected = lastConnected;
    }
}
