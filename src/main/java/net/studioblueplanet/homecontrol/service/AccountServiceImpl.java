/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.service;

import java.util.List;
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
        
        mapperFactory.classMap(TadoMe.class, Account.class)
        .byDefault()
        .register();

        mapperFactory.classMap(TadoHome.class, HomeId.class)
                .byDefault()
                .register();
    }
    
    /**
     * Retrieve the account information from Tado and store in the 
     * account instance.
     */
    private void retrieveAccountFromTado()
    {
        TadoMe me;

        MapperFacade mapper = mapperFactory.getMapperFacade();
        
        me=tado.tadoMe();
        
        if (me!=null)
        {
            account=mapper.map(me, Account.class);
        }
        else
        {
            account=null;
        }
        // TODO: add friend account usernames from persistency
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
    
    @Override
    public void setFriendAccount(String friendAccountUsername)
    {
        if (account==null)
        {
            retrieveAccountFromTado();
        }
        account.addFriendAccountUsername(friendAccountUsername);
        // TODO: persist friend account usernames
    }
    
    @Override
    public List<String> getFriendAccountUsernames()
    {
        if (account==null)
        {
            retrieveAccountFromTado();
        }
        return account.getFriendAccountUsernames();
    }    
}
