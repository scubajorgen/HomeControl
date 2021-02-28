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
public class Overlay
{
    private String                  type;
    private String                  power;
    private Double                  temperatureSetpoint;    
    private String                  termination;
    private int                     timer;

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

    public String getTermination()
    {
        return termination;
    }

    public void setTermination(String termination)
    {
        this.termination = termination;
    }

    public int getTimer()
    {
        return timer;
    }

    public void setTimer(int timer)
    {
        this.timer = timer;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}
