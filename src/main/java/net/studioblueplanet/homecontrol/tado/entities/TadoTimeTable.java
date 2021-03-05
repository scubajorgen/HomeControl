/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

/**
 * This class represents a time table type. Apparently following types are
 * available 0 - ONE_DAY (every weekday the same), 
 * 1 - THREE_DAY (saterday, sunday monday-friday), 2 - SEVEN_DAY (each day)
 * @author jorgen
 */
public class TadoTimeTable
{
    /** ID of the time table type */
    private int id;
    /** Human readable description */
    private String type;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
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
