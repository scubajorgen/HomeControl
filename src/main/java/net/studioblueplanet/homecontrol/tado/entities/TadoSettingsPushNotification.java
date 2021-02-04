/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

/**
 *
 * @author jorgen.van.der.velde
 */
public class TadoSettingsPushNotification
{
    private boolean lowBatteryReminder;
    private boolean awayModeReminder;
    private boolean homeModeReminder;
    private boolean openWindowReminder;
    private boolean energySavingsReportReminder;
    private boolean incidentDetection;   

    public boolean isLowBatteryReminder()
    {
        return lowBatteryReminder;
    }

    public void setLowBatteryReminder(boolean lowBatteryReminder)
    {
        this.lowBatteryReminder = lowBatteryReminder;
    }

    public boolean isAwayModeReminder()
    {
        return awayModeReminder;
    }

    public void setAwayModeReminder(boolean awayModeReminder)
    {
        this.awayModeReminder = awayModeReminder;
    }

    public boolean isHomeModeReminder()
    {
        return homeModeReminder;
    }

    public void setHomeModeReminder(boolean homeModeReminder)
    {
        this.homeModeReminder = homeModeReminder;
    }

    public boolean isOpenWindowReminder()
    {
        return openWindowReminder;
    }

    public void setOpenWindowReminder(boolean openWindowReminder)
    {
        this.openWindowReminder = openWindowReminder;
    }

    public boolean isEnergySavingsReportReminder()
    {
        return energySavingsReportReminder;
    }

    public void setEnergySavingsReportReminder(boolean energySavingsReportReminder)
    {
        this.energySavingsReportReminder = energySavingsReportReminder;
    }

    public boolean isIncidentDetection()
    {
        return incidentDetection;
    }

    public void setIncidentDetection(boolean incidentDetection)
    {
        this.incidentDetection = incidentDetection;
    }
}
