/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.service;

import javax.annotation.PostConstruct;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import net.studioblueplanet.homecontrol.service.entities.Account;
import net.studioblueplanet.homecontrol.service.entities.Home;
import net.studioblueplanet.homecontrol.service.entities.HomeState;
import net.studioblueplanet.homecontrol.tado.entities.TadoHome;
import net.studioblueplanet.homecontrol.tado.entities.TadoPresence.TadoHomePresence;
import net.studioblueplanet.homecontrol.tado.entities.TadoState;
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
    
    private boolean         mappingRegistered=false;
    
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

        mappingRegistered=true;
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
}