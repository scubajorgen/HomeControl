/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

/**
 * Indication for the comfort in the zone.
 * @author jorgen
 */
public class TadoRoomComfort
{
    /** Zone ID */
    private int                 roomId;
    /** Indication about temperature, like COMFY, COLD */
    private String              temperatureLevel;
    /** Indication about humidity, like COMFY, HUMID */
    private String              humidityLevel;
    /** Indication about where in the comfort circle the zone is located */
    private TadoPolarCoordinate coordinate;

    public int getRoomId()
    {
        return roomId;
    }

    public void setRoomId(int roomId)
    {
        this.roomId = roomId;
    }

    public String getTemperatureLevel()
    {
        return temperatureLevel;
    }

    public void setTemperatureLevel(String temperatureLevel)
    {
        this.temperatureLevel = temperatureLevel;
    }

    public String getHumidityLevel()
    {
        return humidityLevel;
    }

    public void setHumidityLevel(String humidityLevel)
    {
        this.humidityLevel = humidityLevel;
    }

    public TadoPolarCoordinate getCoordinate()
    {
        return coordinate;
    }

    public void setCoordinate(TadoPolarCoordinate coordinate)
    {
        this.coordinate = coordinate;
    }
}
