/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.service.entities;

/**
 *
 * @author jorgen
 */
public class Zone
{
    /** Zone ID, number counting from 0*/
    private int                     id;
    /** Zone name */
    private String                  name;
    /** Zone type, like HEATING*/
    private String                  type;
    private String                  tadoMode;
    private String                  power;
    private Double                  temperatureSetpoint;
    private Double                  temperature;
    private Double                  humidity;
    
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

    public String getTadoMode()
    {
        return tadoMode;
    }

    public void setTadoMode(String tadoMode)
    {
        this.tadoMode = tadoMode;
    }

    public String getPower()
    {
        return power;
    }

    public void setPower(String power)
    {
        this.power = power;
    }

    public Double getTemperatureSetpoint()
    {
        return temperatureSetpoint;
    }

    public void setTemperatureSetpoint(Double temperatureSetpoint)
    {
        this.temperatureSetpoint = temperatureSetpoint;
    }

    public Double getTemperature()
    {
        return temperature;
    }

    public void setTemperature(Double temperature)
    {
        this.temperature = temperature;
    }

    public Double getHumidity()
    {
        return humidity;
    }

    public void setHumidity(Double humidity)
    {
        this.humidity = humidity;
    }
}
