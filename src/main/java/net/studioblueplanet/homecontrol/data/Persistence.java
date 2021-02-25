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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import net.studioblueplanet.homecontrol.service.entities.FriendAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author jorgen
 */
@Component
public class Persistence
{
    private static final Logger     LOG = LoggerFactory.getLogger(AccountServiceImpl.class);
    @Autowired
    private File                    friendsJsonFile;    
    
    public Persistence()
    {
        
    }

    public void storeFriends(List<FriendAccount> friendAccounts)
    {
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
    
    public List<FriendAccount> restoreFriends()
    {
        List<FriendAccount> friendAccounts;
        Gson gson = new Gson();
        friendAccounts=new ArrayList<>();
        try
        {
            if (friendsJsonFile.exists())
            {
                Type listType = new TypeToken<ArrayList<FriendAccount>>(){}.getType();
                friendAccounts = new Gson().fromJson(new FileReader(friendsJsonFile), listType);
                LOG.info("Friend accounts read from file. Size {}", friendAccounts.size());
            }
        }
        catch (IOException e)
        {
            LOG.error("Error reading file {}: {}", friendsJsonFile.getAbsolutePath(), e.getMessage());
        } 
        return friendAccounts;
    }
}
