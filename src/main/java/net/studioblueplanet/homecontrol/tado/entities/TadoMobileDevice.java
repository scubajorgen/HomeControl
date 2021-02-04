/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

import net.studioblueplanet.homecontrol.tado.entities.TadoDeviceSettings;
import net.studioblueplanet.homecontrol.tado.entities.TadoDeviceMetadata;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author jorgen.van.der.velde
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TadoMobileDevice
{
    /** Name of the device */
    private String                      name;
    /** ID of the device */
    private int                         id;
    
    /** Settings of the device */
    private TadoDeviceSettings          settings;
    /** Location of the device (and owner implicitly) */
    private TadoLocation                location;
    /** Device metadata */
    private TadoDeviceMetadata          deviceMetadata;

    public TadoLocation getLocation()
    {
        return location;
    }

    public void setLocation(TadoLocation location)
    {
        this.location = location;
    }

    public TadoDeviceMetadata getDeviceMetadata()
    {
        return deviceMetadata;
    }

    public void setDeviceMetadata(TadoDeviceMetadata deviceMetadata)
    {
        this.deviceMetadata = deviceMetadata;
    }

    public TadoDeviceSettings getSettings()
    {
        return settings;
    }

    public void setSettings(TadoDeviceSettings settings)
    {
        this.settings = settings;
    }    

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
}
