/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

/**
 * Represents the weather surrounding the home
 * @author jorgen
 */
public class TadoWeather
{
    /** The solar intensity as percentage */
    private TadoDataPoint   solarIntensity;
    /** The outside temperature */
    private TadoDataPoint   outsideTemperature;
    /** The state of the weather, type=WEATHER_STATE */
    private TadoDataPoint   weatherState;

    public TadoDataPoint getSolarIntensity()
    {
        return solarIntensity;
    }

    public void setSolarIntensity(TadoDataPoint solarIntensity)
    {
        this.solarIntensity = solarIntensity;
    }

    public TadoDataPoint getOutsideTemperature()
    {
        return outsideTemperature;
    }

    public void setOutsideTemperature(TadoDataPoint outsideTemperature)
    {
        this.outsideTemperature = outsideTemperature;
    }

    public TadoDataPoint getWeatherState()
    {
        return weatherState;
    }

    public void setWeatherState(TadoDataPoint weatherState)
    {
        this.weatherState = weatherState;
    }
}
