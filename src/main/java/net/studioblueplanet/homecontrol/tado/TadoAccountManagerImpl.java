/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado;
import java.util.ArrayList;
import java.util.List;
import net.studioblueplanet.homecontrol.tado.entities.TadoAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author jorgen
 */
@Component
public class TadoAccountManagerImpl implements TadoAccountManager
{
    private final List<TadoAccount> accounts;
    private static final Logger     LOG = LoggerFactory.getLogger(TadoAccountManagerImpl.class); 
    
    @Autowired
    private RestTemplate            template;    
    
    public TadoAccountManagerImpl()
    {
        accounts=new ArrayList<>();
    }

    @Override
    public boolean isAuthorized()
    {
        return (loggedInAccount()!=null);
    }
    
    @Override
    public TadoAccount findAccount(String username)
    {
        TadoAccount account;
        account = accounts.stream()
                    .filter(ac -> username.equals(ac.getUsername()))
                    .findAny()
                    .orElse(null);    
        return account;
    }
    
    @Override
    public TadoAccount findAccount(int homeId)
    {
        TadoAccount             account;
        account = accounts.stream()
                .filter(ac -> ac.getTadoMe().getHomes().stream().allMatch(h -> h.getId()==homeId))
                .findAny()
                .orElse(null); 
        return account;
    }
    
    /**
     * Returns the logged in account
     * @return The account
     */
    @Override
    public TadoAccount loggedInAccount()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();  
        return findAccount(currentPrincipalName);
    }

    @Override
    public String loggedInUsername()
    {
        TadoAccount account=loggedInAccount();
        String      username;
        if (account!=null)
        {
            username=account.getUsername();
        }
        else
        {
            username=null;
        }
        return username;
    }
    
    @Override
    public List<TadoAccount> getAccounts()
    {
        return accounts;
    }
    
    @Override
    public void addAccount(TadoAccount account)
    {
        accounts.add(account);
    }
    

    @Override
    public void deleteAccount(String username)
    {
        accounts.removeIf(ac -> ac.getUsername().equals(username));
    }
    
    @Override
    public void deleteAllAccounts()
    {
        accounts.clear();
    }
    
}
