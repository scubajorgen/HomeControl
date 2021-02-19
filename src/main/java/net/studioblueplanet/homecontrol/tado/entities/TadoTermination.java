/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
/**
 *
 * @author jorgen
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TadoTermination
{
    /** Type of termination: TIMER */
    private String      type;
    /** TIMER, NEXT_TIME_BLOCK or MANUAL for infinite */
    private String      typeSkillBasedApp;
    private Integer     durationInSeconds;
    private Date        expiry;
    private Integer     remainingTimeInSeconds;
    private Date        projectedExpiry;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getTypeSkillBasedApp()
    {
        return typeSkillBasedApp;
    }

    public void setTypeSkillBasedApp(String typeSkillBasedApp)
    {
        this.typeSkillBasedApp = typeSkillBasedApp;
    }

    public Integer getDurationInSeconds()
    {
        return durationInSeconds;
    }

    public void setDurationInSeconds(Integer durationInSeconds)
    {
        this.durationInSeconds = durationInSeconds;
    }

    public Date getExpiry()
    {
        return expiry;
    }

    public void setExpiry(Date expiry)
    {
        this.expiry = expiry;
    }

    public Integer getRemainingTimeInSeconds()
    {
        return remainingTimeInSeconds;
    }

    public void setRemainingTimeInSeconds(Integer remainingTimeInSeconds)
    {
        this.remainingTimeInSeconds = remainingTimeInSeconds;
    }

    public Date getProjectedExpiry()
    {
        return projectedExpiry;
    }

    public void setProjectedExpiry(Date projectedExpiry)
    {
        this.projectedExpiry = projectedExpiry;
    }
    
}
