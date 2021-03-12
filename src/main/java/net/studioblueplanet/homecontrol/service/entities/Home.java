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
public class Home
{

    private int     id;
    private String  name;
    private String  dateTimeZone;
    private String  temperatureUnit;
    private int     awayRadiusInMeters;
    private double  latitude;
    private double  longitude;
    private String  presence;
    private String  contact;
    private String  addressLine1;
    private String  addressLine2;
    private String  zipCode;
    private String  city;
    private String  country;
    private double  solarIntensity;
    private double  outsideTemperature;
    private String  weatherState;

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

    public String getDateTimeZone()
    {
        return dateTimeZone;
    }

    public void setDateTimeZone(String dateTimeZone)
    {
        this.dateTimeZone = dateTimeZone;
    }

    public String getTemperatureUnit()
    {
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnit)
    {
        this.temperatureUnit = temperatureUnit;
    }

    public int getAwayRadiusInMeters()
    {
        return awayRadiusInMeters;
    }

    public void setAwayRadiusInMeters(int awayRadiusInMeters)
    {
        this.awayRadiusInMeters = awayRadiusInMeters;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public String getPresence()
    {
        return presence;
    }

    public void setPresence(String presence)
    {
        this.presence = presence;
    }

    public String getContact()
    {
        return contact;
    }

    public void setContact(String contact)
    {
        this.contact = contact;
    }

    public String getAddressLine1()
    {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1)
    {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2()
    {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2)
    {
        this.addressLine2 = addressLine2;
    }

    public String getZipCode()
    {
        return zipCode;
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public double getSolarIntensity()
    {
        return solarIntensity;
    }

    public void setSolarIntensity(double solarIntensity)
    {
        this.solarIntensity = solarIntensity;
    }

    public double getOutsideTemperature()
    {
        return outsideTemperature;
    }

    public void setOutsideTemperature(double outsideTemperature)
    {
        this.outsideTemperature = outsideTemperature;
    }

    public String getWeatherState()
    {
        return weatherState;
    }

    public void setWeatherState(String weatherState)
    {
        this.weatherState = weatherState;
    }
}
