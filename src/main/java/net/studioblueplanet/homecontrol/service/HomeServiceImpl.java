/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.service;

import java.util.List;
import java.util.Iterator;
import javax.annotation.PostConstruct;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import net.studioblueplanet.homecontrol.service.entities.Account;
import net.studioblueplanet.homecontrol.service.entities.Home;
import net.studioblueplanet.homecontrol.service.entities.HomeState;
import net.studioblueplanet.homecontrol.service.entities.Overlay;
import net.studioblueplanet.homecontrol.service.entities.Zone;
import net.studioblueplanet.homecontrol.tado.entities.TadoHome;
import net.studioblueplanet.homecontrol.tado.entities.TadoPresence.TadoHomePresence;
import net.studioblueplanet.homecontrol.tado.entities.TadoState;
import net.studioblueplanet.homecontrol.tado.entities.TadoZone;
import net.studioblueplanet.homecontrol.tado.entities.TadoZoneState;
import net.studioblueplanet.homecontrol.tado.TadoInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 *
 * @author jorgen
 */
@Component
public class HomeServiceImpl implements HomeService
{
    @Autowired
    private TadoInterface   tado;

    @Autowired
    private MapperFactory   mapperFactory;
    
    private Account         account;
    
    @PostConstruct
    public void init()
    {
        registerMappings();
    }
    
    /**
     * Register the mappings used in this class.
     */
    private void registerMappings()
    {
        mapperFactory.classMap(TadoState.class, HomeState.class)
                .byDefault()
                .register();

        mapperFactory.classMap(TadoHome.class, Home.class)
                .byDefault()
                .field("geolocation.longitude", "longitude")
                .field("geolocation.latitude", "latitude")
                .field("address.addressLine1", "addressLine1")
                .field("address.addressLine2", "addressLine2")
                .field("address.zipCode", "zipCode")
                .field("address.city", "city")
                .field("address.country", "country")
                .field("address.city", "city")
                .field("contactDetails.name", "contact")
                .register();

        mapperFactory.classMap(TadoState.class, Home.class)
                .byDefault()
                .register();

        mapperFactory.classMap(TadoZone.class, Zone.class)
                .byDefault()
                .register();

        mapperFactory.classMap(TadoZoneState.class, Zone.class)
                .byDefault()
                .field("setting.temperature.celsius", "temperatureSetpoint")
                .field("setting.power", "power")
                .field("sensorDataPoints.insideTemperature.celsius", "temperature")
                .field("sensorDataPoints.insideTemperature.precision.celsius", "temperaturePrecision")
                .field("sensorDataPoints.humidity.percentage", "humidity")
                .field("activityDataPoints.heatingPower.percentage", "heatingPower")
                .register();
    }
    
    @Override
    public Home getHome(int homeId)
    {
        TadoHome  home;
        TadoState state;
        Home      theHome;
        
        state=tado.tadoState(homeId);    
        home=tado.tadoHome(homeId);
        
        MapperFacade mapper = mapperFactory.getMapperFacade();
        theHome=mapper.map(home, Home.class);
        mapper.map(state, theHome);
        return theHome;
    }
    
    @Override
    public HomeState getHomeState(int homeId)
    {
        TadoState state;
        
        state=tado.tadoState(homeId);
        
        MapperFacade mapper = mapperFactory.getMapperFacade();
        return mapper.map(state, HomeState.class);
    }

    @Override
    public void setPresence(int homeId, String presence)
    {
        if (presence.equals("AWAY"))
        {
            tado.setTadoPresence(homeId, TadoHomePresence.AWAY);
        }
        else
        {
            tado.setTadoPresence(homeId, TadoHomePresence.HOME);
        }
    }
    
    @Override
    public List<Zone>   getZones(int homeId)
    {
        List<TadoZone>  tadoZones;
        List<Zone>      zones;
        Iterator<Zone>  it;
        Zone            zone;
        TadoZoneState   state;        
        
        tadoZones=tado.tadoZones(homeId);
        
        
        MapperFacade mapper = mapperFactory.getMapperFacade();
        zones=mapper.mapAsList(tadoZones, Zone.class);
        
        it=zones.iterator();
        while (it.hasNext())
        {
            zone    =it.next();
            state   =tado.tadoZoneState(homeId, zone.getId());
            mapper.map(state, zone);
        }
        
        return zones;
    }
    
    @Override
    public void setOverlay(int homeId, int zoneId, Overlay overlay)
    {
        tado.setTadoOverlay(homeId, zoneId, overlay.getType(), overlay.getPower(), overlay.getTemperatureSetpoint(), overlay.getTermination(), overlay.getTimer());
    }
    
    @Override
    public void deleteOverlay(int homeId, int zoneId)
    {
        tado.deleteTadoOverlay(homeId, zoneId);
    }
}
