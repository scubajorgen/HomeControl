/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.data;

import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.studioblueplanet.homecontrol.service.AccountServiceImpl;
import org.springframework.stereotype.Component;
import net.studioblueplanet.homecontrol.service.entities.FriendAccount;
import net.studioblueplanet.homecontrol.tado.entities.TadoAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author jorgen
 */
@Component
public class PersistenceImpl implements Persistence
{
    private static final String     ACCOUNTSJSONFILE        ="accounts.json";
    private static final String     FRIENDACCOUNTSJSONFILE  ="friendAccounts.json";
    private static final Logger     LOG                     = LoggerFactory.getLogger(AccountServiceImpl.class);
    
    
    public PersistenceImpl()
    {
        
    }

    @Override
    public void storeFriends(List<FriendAccount> friendAccounts)
    {
        File friendsJsonFile=new File(FRIENDACCOUNTSJSONFILE);
        Gson gson = new Gson();

        try
        {
            FileWriter fileWriter=new FileWriter(friendsJsonFile);
            gson.toJson(friendAccounts, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        }
        catch (IOException e)
        {
            LOG.error("Error writing file {}: {}", friendsJsonFile, e.getMessage());
        }

    }
    
    @Override
    public List<FriendAccount> restoreFriends()
    {
        FileReader fileReader;
        File friendsJsonFile=new File(FRIENDACCOUNTSJSONFILE);
        List<FriendAccount> friendAccounts;
        Gson gson = new Gson();
        friendAccounts=new ArrayList<>();
        try
        {
            if (friendsJsonFile.exists())
            {
                Type listType = new TypeToken<ArrayList<FriendAccount>>(){}.getType();
                fileReader=new FileReader(friendsJsonFile);
                friendAccounts = new Gson().fromJson(new FileReader(friendsJsonFile), listType);
                fileReader.close();
                LOG.info("Friend accounts read from file. Size {}", friendAccounts.size());
            }
        }
        catch (IOException e)
        {
            LOG.error("Error reading file {}: {}", friendsJsonFile.getAbsolutePath(), e.getMessage());
        } 
        return friendAccounts;
    }
    
    @Override
    public void storeAccounts(List<TadoAccount> accounts)
    {
        File accountJsonFile=new File(ACCOUNTSJSONFILE);
        Gson gson = new Gson();

        try
        {
            FileWriter fileWriter=new FileWriter(accountJsonFile);
            gson.toJson(accounts, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        }
        catch (IOException e)
        {
            LOG.error("Error writing accounts file {}: {}", accountJsonFile, e.getMessage());
        }

    }
    
    @Override
    public List<TadoAccount> restoreAccounts()
    {
        File accountJsonFile=new File(ACCOUNTSJSONFILE);
        FileReader fileReader;
        List<TadoAccount> accounts;
        Gson gson = new Gson();
        accounts=new ArrayList<>();
        try
        {
            if (accountJsonFile.exists())
            {
                Type listType = new TypeToken<ArrayList<TadoAccount>>(){}.getType();
                fileReader=new FileReader(accountJsonFile);
                accounts = new Gson().fromJson(fileReader, listType);
                fileReader.close();
                LOG.info("Accounts read from file. Size {}", accounts.size());
            }
        }
        catch (IOException e)
        {
            LOG.error("Error reading accounts file {}: {}", accountJsonFile.getAbsolutePath(), e.getMessage());
        } 
        return accounts;
    }
    
}
