/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

import java.util.Date;
import java.util.List;

/**
 *
 * @author jorgen
 */
public class TadoZone
{
    /** Zone ID, number counting from 0*/
    private int                     id;
    /** Zone name */
    private String                  name;
    /** Zone type, like HEATING*/
    private String                  type;
    /** Date time of creation in ISO format*/
    private Date                    dateCreated;
    /** List of device types used in the zones: RU02, VA02*/
    private List<String>            deviceTypes;
    /** List of devices */
    private List<TadoDevice>        devices;
    /** ?? */
    private boolean                 reportAvailable;
    /** ? */
    private boolean                 supportsDazzle;
    /** ? */
    private boolean                 dazzleEnabled;
    /** ? */
    private TadoDazzleMode          dazzleMode;
    /** Open window detection */
    private TadoOpenWindowDetection openWindowDetection;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Date getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated)
    {
        this.dateCreated = dateCreated;
    }

    public List<String> getDeviceTypes()
    {
        return deviceTypes;
    }

    public void setDeviceTypes(List<String> deviceTypes)
    {
        this.deviceTypes = deviceTypes;
    }

    public List<TadoDevice> getDevices()
    {
        return devices;
    }

    public void setDevices(List<TadoDevice> devices)
    {
        this.devices = devices;
    }

    public boolean isReportAvailable()
    {
        return reportAvailable;
    }

    public void setReportAvailable(boolean reportAvailable)
    {
        this.reportAvailable = reportAvailable;
    }

    public boolean isSupportsDazzle()
    {
        return supportsDazzle;
    }

    public void setSupportsDazzle(boolean supportsDazzle)
    {
        this.supportsDazzle = supportsDazzle;
    }

    public boolean isDazzleEnabled()
    {
        return dazzleEnabled;
    }

    public void setDazzleEnabled(boolean dazzleEnabled)
    {
        this.dazzleEnabled = dazzleEnabled;
    }

    public TadoDazzleMode getDazzleMode()
    {
        return dazzleMode;
    }

    public void setDazzleMode(TadoDazzleMode dazzleMode)
    {
        this.dazzleMode = dazzleMode;
    }

    public TadoOpenWindowDetection getOpenWindowDetection()
    {
        return openWindowDetection;
    }

    public void setOpenWindowDetection(TadoOpenWindowDetection openWindowDetection)
    {
        this.openWindowDetection = openWindowDetection;
    }
}
