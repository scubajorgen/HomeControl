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
public class TadoZoneSetting
{
    /** Zone type HEATING or HOT_WATER */
    private String type;
    /** Power on/off state: ON or OFF */
    private String power;
    /** Temperature */
    private TadoTemperature temperature;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getPower()
    {
        return power;
    }

    public void setPower(String power)
    {
        this.power = power;
    }

    public TadoTemperature getTemperature()
    {
        return temperature;
    }

    public void setTemperature(TadoTemperature temperature)
    {
        this.temperature = temperature;
    }
    
    
}
