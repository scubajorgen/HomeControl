/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a Tado Home
 * @author jorgen.van.der.velde
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TadoHome
{
    /** Home ID. Apparently a numbering of homes 0- */
    private int                     id;
    /** Home name */
    private String                  name;
    /** Home date time zone, e.g. Europe/Amsterdam */
    private String                  dateTimeZone;
    /** Datetime of creation in ISO format */
    private String                  dateCreated;
    /** Unit of temperature, e.g. CELCIUS */
    private String                  temperatureUnit;
    /** Unclear */
    private String                  partner;
    /** Unclear */
    private boolean                 simpleSmartScheduleEnabled;
    /** The distance from home beyond which the user is regarded as away */
    private double                  awayRadiusInMeters;
    /** Indicates the home is properly installed */
    private boolean                 installationCompleted;
    /** Settings for incident detection */
    private TadoIncidentDetection   incidentDetection;
    /** Free trial of auto-assist enabled */
    private boolean                 autoAssistFreeTrialEnabled;
    /** Unclear */
    private List<TadoSkill>         skills;
    /** Unclear */
    private boolean                 christmasModeEnabled;
    /** Indicates whether auto-assist can send reminders */
    private boolean                 showAutoAssistReminders;
    /** Home owner contact details */
    private TadoContactDetails      contactDetails;
    /** Home address */
    private TadoAddress             address;
    /** Home geo coordinate (lat/lon) */
    private TadoGeolocation         geolocation;
    /** Unclear */
    private boolean                 consentGrantSkippable;

    public String getDateTimeZone()
    {
        return dateTimeZone;
    }

    public void setDateTimeZone(String dateTimeZone)
    {
        this.dateTimeZone = dateTimeZone;
    }

    public String getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated)
    {
        this.dateCreated = dateCreated;
    }

    public String getTemperatureUnit()
    {
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnit)
    {
        this.temperatureUnit = temperatureUnit;
    }

    public String getPartner()
    {
        return partner;
    }

    public void setPartner(String partner)
    {
        this.partner = partner;
    }

    public boolean isSimpleSmartScheduleEnabled()
    {
        return simpleSmartScheduleEnabled;
    }

    public void setSimpleSmartScheduleEnabled(boolean simpleSmartScheduleEnabled)
    {
        this.simpleSmartScheduleEnabled = simpleSmartScheduleEnabled;
    }

    public double getAwayRadiusInMeters()
    {
        return awayRadiusInMeters;
    }

    public void setAwayRadiusInMeters(double awayRadiusInMeters)
    {
        this.awayRadiusInMeters = awayRadiusInMeters;
    }

    public boolean isInstallationCompleted()
    {
        return installationCompleted;
    }

    public void setInstallationCompleted(boolean installationCompleted)
    {
        this.installationCompleted = installationCompleted;
    }

    public TadoIncidentDetection getIncidentDetection()
    {
        return incidentDetection;
    }

    public void setIncidentDetection(TadoIncidentDetection incidentDetection)
    {
        this.incidentDetection = incidentDetection;
    }

    public boolean isAutoAssistFreeTrialEnabled()
    {
        return autoAssistFreeTrialEnabled;
    }

    public void setAutoAssistFreeTrialEnabled(boolean autoAssistFreeTrialEnabled)
    {
        this.autoAssistFreeTrialEnabled = autoAssistFreeTrialEnabled;
    }

    public List<TadoSkill> getSkills()
    {
        return skills;
    }

    public void setSkills(List<TadoSkill> skills)
    {
        this.skills = skills;
    }

    public boolean isChristmasModeEnabled()
    {
        return christmasModeEnabled;
    }

    public void setChristmasModeEnabled(boolean christmasModeEnabled)
    {
        this.christmasModeEnabled = christmasModeEnabled;
    }

    public boolean isShowAutoAssistReminders()
    {
        return showAutoAssistReminders;
    }

    public void setShowAutoAssistReminders(boolean showAutoAssistReminders)
    {
        this.showAutoAssistReminders = showAutoAssistReminders;
    }

    public TadoContactDetails getContactDetails()
    {
        return contactDetails;
    }

    public void setContactDetails(TadoContactDetails contactDetails)
    {
        this.contactDetails = contactDetails;
    }

    public TadoAddress getAddress()
    {
        return address;
    }

    public void setAddress(TadoAddress address)
    {
        this.address = address;
    }

    public TadoGeolocation getGeolocation()
    {
        return geolocation;
    }

    public void setGeolocation(TadoGeolocation geolocation)
    {
        this.geolocation = geolocation;
    }

    public boolean isConsentGrantSkippable()
    {
        return consentGrantSkippable;
    }

    public void setConsentGrantSkippable(boolean consentGrantSkippable)
    {
        this.consentGrantSkippable = consentGrantSkippable;
    }
    
    
    
    
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
    
    
    
    @Override
    public String toString()
    {
        String string;
        string ="ID         : "+id+"\n";
        string+="Name       : "+name+"\n";
        return string;
    }
}
