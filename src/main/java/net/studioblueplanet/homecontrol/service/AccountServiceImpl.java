/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.service;

import javax.annotation.PostConstruct;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MapperFacade;
import net.studioblueplanet.homecontrol.service.entities.Account;
import net.studioblueplanet.homecontrol.service.entities.HomeId;
import net.studioblueplanet.homecontrol.tado.TadoInterface;
import net.studioblueplanet.homecontrol.tado.entities.TadoMe;
import net.studioblueplanet.homecontrol.tado.entities.TadoHome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author jorgen
 */
@Component
public class AccountServiceImpl implements AccountService
{
    @Autowired
    private TadoInterface tado;
    
    @Autowired
    private MapperFactory mapperFactory;
    
    private Account account;
    private boolean mappingRegistered=false;
    
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
        mapperFactory.classMap(TadoMe.class, Account.class)
        .byDefault()
        .register();

        mapperFactory.classMap(TadoHome.class, HomeId.class)
                .byDefault()
                .register();

        mappingRegistered=true;
    }
    
    /**
     * Retrieve the account information from Tado and store in the 
     * account instance.
     */
    private void retrieveAccountFromTado()
    {
        TadoMe me;
/*
        if (!mappingRegistered)
        {
            registerMappings();
        }
*/        
        MapperFacade mapper = mapperFactory.getMapperFacade();
        
        me=tado.tadoMe();
        
        account=mapper.map(me, Account.class);
    }
    
    @Override
    public Account getAccount()
    {
        if (account==null)
        {
            retrieveAccountFromTado();
        }
        return account;
    }
}
