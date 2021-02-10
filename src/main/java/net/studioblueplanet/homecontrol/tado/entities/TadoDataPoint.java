/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

import java.util.Date;

/**
 * Generic datapoint. Based on the type the temperature of percentage is 
 * available
 * @author jorgen
 */
public class TadoDataPoint
{
    /** Type of datapoint: TEMPERATURE or PERCENTAGE */
    private String                      type;
    private Date                        timestamp;
    private double                      percentage;
    private double                      celsius;
    private double                      fahrenheit;
    private TadoTemperaturePrecision    precision;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    public double getPercentage()
    {
        return percentage;
    }

    public void setPercentage(double percentage)
    {
        this.percentage = percentage;
    }

    public double getCelsius()
    {
        return celsius;
    }

    public void setCelsius(double celsius)
    {
        this.celsius = celsius;
    }

    public double getFahrenheit()
    {
        return fahrenheit;
    }

    public void setFahrenheit(double fahrenheit)
    {
        this.fahrenheit = fahrenheit;
    }

    public TadoTemperaturePrecision getPrecision()
    {
        return precision;
    }

    public void setPrecision(TadoTemperaturePrecision precision)
    {
        this.precision = precision;
    }
}
