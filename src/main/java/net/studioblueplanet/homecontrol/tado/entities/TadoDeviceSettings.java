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
public class TadoDeviceSettings
{
    private boolean                         geoTrackingEnabled;
    private boolean                         onDemandLogRetrievalEnabled;
    private TadoSettingsPushNotification    pushNotifications;

    public boolean isGeoTrackingEnabled()
    {
        return geoTrackingEnabled;
    }

    public void setGeoTrackingEnabled(boolean geoTrackingEnabled)
    {
        this.geoTrackingEnabled = geoTrackingEnabled;
    }

    public boolean isOnDemandLogRetrievalEnabled()
    {
        return onDemandLogRetrievalEnabled;
    }

    public void setOnDemandLogRetrievalEnabled(boolean onDemandLogRetrievalEnabled)
    {
        this.onDemandLogRetrievalEnabled = onDemandLogRetrievalEnabled;
    }

    public TadoSettingsPushNotification getPushNotifications()
    {
        return pushNotifications;
    }

    public void setPushNotifications(TadoSettingsPushNotification pushNotifications)
    {
        this.pushNotifications = pushNotifications;
    }
    
}
