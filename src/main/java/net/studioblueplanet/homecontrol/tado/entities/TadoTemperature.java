/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

import net.studioblueplanet.homecontrol.tado.DoubleSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 *
 * @author jorgen
 */
public class TadoTemperature
{
    /** Temperature in degree Celcius */
    @JsonSerialize(using = DoubleSerializer.class)
    private double celsius;
    /** Temperature in degree Fahrenheit */
    @JsonSerialize(using = DoubleSerializer.class)
    private double fahrenheit;

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
}
