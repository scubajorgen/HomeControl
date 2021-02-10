/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

/**
 * Measurement datapoint/sensor values
 * @author jorgen
 */
public class TadoDataPointsSensor
{
    private TadoDataPoint insideTemperature;
    private TadoDataPoint humidity;

    public TadoDataPoint getInsideTemperature()
    {
        return insideTemperature;
    }

    public void setInsideTemperature(TadoDataPoint insideTemperature)
    {
        this.insideTemperature = insideTemperature;
    }

    public TadoDataPoint getHumidity()
    {
        return humidity;
    }

    public void setHumidity(TadoDataPoint humidity)
    {
        this.humidity = humidity;
    }
}
