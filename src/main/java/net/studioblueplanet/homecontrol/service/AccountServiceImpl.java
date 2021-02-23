/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MapperFacade;
import net.studioblueplanet.homecontrol.service.entities.Account;
import net.studioblueplanet.homecontrol.service.entities.FriendAccount;
import net.studioblueplanet.homecontrol.service.entities.HomeId;
import net.studioblueplanet.homecontrol.tado.TadoInterface;
import net.studioblueplanet.homecontrol.tado.entities.TadoAccount;
import net.studioblueplanet.homecontrol.tado.entities.TadoMe;
import net.studioblueplanet.homecontrol.tado.entities.TadoHome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author jorgen
 */
@Component
public class AccountServiceImpl implements AccountService
{
    private static final Logger     LOG = LoggerFactory.getLogger(AccountServiceImpl.class); 

    @Autowired
    private TadoInterface   tado;
    
    @Autowired
    private MapperFactory   mapperFactory;
    
    List<FriendAccount>     friendAccounts;
    
    /**
     * Constructor
     */
    public AccountServiceImpl()
    {
        reset();
    }
    
    /**
     * Post constructor, after beans have been created.
     */
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
     * Retrieve the account information from Tado.
     * @return The Tado account
     */
    private Account retrieveAccountFromTado()
    {
        TadoMe  me;
        Account account;

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
        return account;
    }

    @Override
    public void reset()
    {
        friendAccounts=new ArrayList<>();
    }
    
    @Override
    public Account getAccount()
    {
        Account account=retrieveAccountFromTado();
        return account;
    }
    
    @Override
    public void addToFriendAccount(String friendAccountUsername)
    {
        Account account=retrieveAccountFromTado();
        FriendAccount friend=new FriendAccount();
        friend.setAccount(friendAccountUsername);
        friend.setFriendAccount(account.getUsername());
        friendAccounts.add(friend);
    }
    
    @Override
    public List<String> getFriendAccountUsernames()
    {
        Account account=retrieveAccountFromTado();
        
        return friendAccounts.stream()
                        .filter(s -> s.getAccount().equals(account.getUsername()))
                        .map(i -> i.getFriendAccount())
                        .sorted()
                        .collect(Collectors.toList());
    }    
    
    @Override
    public List<HomeId> getAvailableHomes()
    {
        List<HomeId>                homeList;
        Iterator<FriendAccount>     it;
        FriendAccount               friend;
        TadoAccount                 friendAccount;
        List<TadoHome>              friendHomes;
        
        Account account=retrieveAccountFromTado();
        
        homeList=account.getHomes().stream().collect(Collectors.toList());
        
        it=friendAccounts.iterator();
        while (it.hasNext())
        {
            friend=it.next();
            if (friend.getAccount().equals(account.getUsername()))
            {
                friendAccount=tado.findAccount(friend.getFriendAccount());
                friendHomes=friendAccount.getTadoMe().getHomes();
                MapperFacade mapper = mapperFactory.getMapperFacade();
                List<HomeId> friendHomeList=mapper.mapAsList(friendHomes, HomeId.class);
                homeList.addAll(friendHomeList);
            }
        }

        return homeList;
    }
}
