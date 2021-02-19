/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author jorgen
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TadoSetting
{
    /** Type: HEATING */
    private String type;
    /** Power ON or OFF */
    private String power;
    /** Temperature setting */
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
