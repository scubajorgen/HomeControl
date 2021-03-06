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
public class TadoOverlay
{
    /** Type of the overlay */
    private String              type;
    /** Setting to overrule */
    private TadoSetting         setting;
    /** Termination of the overlay */
    private TadoTermination     termination;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public TadoSetting getSetting()
    {
        return setting;
    }

    public void setSetting(TadoSetting setting)
    {
        this.setting = setting;
    }

    public TadoTermination getTermination()
    {
        return termination;
    }

    public void setTermination(TadoTermination termination)
    {
        this.termination = termination;
    }
}
