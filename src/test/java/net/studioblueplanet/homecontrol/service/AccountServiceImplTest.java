/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.service;

import java.util.ArrayList;
import java.util.List;

import net.studioblueplanet.homecontrol.service.entities.Account;
import net.studioblueplanet.homecontrol.service.entities.FriendAccount;
import net.studioblueplanet.homecontrol.service.entities.HomeId;
import net.studioblueplanet.homecontrol.data.Persistence;
import net.studioblueplanet.homecontrol.tado.TadoAccountManager;
import net.studioblueplanet.homecontrol.tado.TadoException;
import net.studioblueplanet.homecontrol.tado.TadoInterface;
import net.studioblueplanet.homecontrol.tado.entities.TadoAccount;
import net.studioblueplanet.homecontrol.tado.entities.TadoMe;
import net.studioblueplanet.homecontrol.tado.entities.TadoHome;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;


import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.springframework.http.HttpStatus;

/**
 *
 * @author jorgen
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceImplTest
{
    @TestConfiguration
    static class AccountServiceImplTestContextConfiguration 
    {
    }
    
    @Autowired
    private AccountService      instance;
    
    @MockBean
    private TadoInterface       tadoInterface;
    
    @MockBean
    private TadoAccountManager  accountManager;
    
    @MockBean
    private Persistence         persistence;
    
    private TadoMe me;
    private TadoMe friend1;
    private TadoMe friend2;
    private TadoAccount meAccount;
    private TadoAccount friend1Account;
    private TadoAccount friend2Account;
    
    public AccountServiceImplTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
        instance.reset();
        
        me=new TadoMe();
        me.setId("Me ID");
        me.setName("The dude");
        me.setEmail("my email");
        me.setLocale("locale");
        me.setUsername("theUser@email.com");
        ArrayList<TadoHome> homes=new ArrayList<>();
        TadoHome    home=new TadoHome();
        home.setId(12345);
        home.setName("Home");
        homes.add(home);
        me.setHomes(homes);
        meAccount=new TadoAccount("theUser@email.com", "", null);
        meAccount.setTadoMe(me);
        
        friend1=new TadoMe();
        friend1.setId("Friend 1 ID");
        friend1.setName("The dudes friend");
        friend1.setEmail("friend 1 email");
        friend1.setLocale("locale");
        friend1.setUsername("friend1@email.com");
        homes=new ArrayList<>();
        home=new TadoHome();
        home.setId(54321);
        home.setName("Friend 1 Home");
        homes.add(home);
        friend1.setHomes(homes);        
        friend1Account=new TadoAccount("friend1@email.com", "", null);
        friend1Account.setTadoMe(friend1);
        
        friend2=new TadoMe();
        friend2.setId("Friend 2 ID");
        friend2.setName("The dudes other friend");
        friend2.setEmail("friend 2 email");
        friend2.setLocale("locale");
        friend2.setUsername("friend2@email.com");
        homes=new ArrayList<>();
        home=new TadoHome();
        home.setId(54323);
        home.setName("Friend 2 1st Home");
        homes.add(home);
        home=new TadoHome();
        home.setId(54322);
        home.setName("Friend 2 2nd Home");
        homes.add(home);
        friend2.setHomes(homes);        
        friend2Account=new TadoAccount("friend2@email.com", "", null);
        friend2Account.setTadoMe(friend2);
        
        Mockito.when(persistence.restoreFriends()).thenReturn(new ArrayList<FriendAccount>());
    }
    
    @After
    public void tearDown()
    {
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();     
    /**
     * Test of reset method, of class AccountServiceImpl.
     */
    @Test
    public void testReset()
    {
        System.out.println("reset");

        Mockito.reset(tadoInterface);
        Mockito.when(accountManager.findAccount("theUser@email.com")).thenReturn(meAccount);
        Mockito.when(accountManager.findAccount("friend1@email.com")).thenReturn(friend1Account);
        Mockito.when(accountManager.findAccount("friend2@email.com")).thenReturn(friend2Account);

        Mockito.when(tadoInterface.tadoMe()).thenReturn(friend1);
        instance.addToFriendAccount("theUser@email.com");
        Mockito.when(tadoInterface.tadoMe()).thenReturn(friend2);
        instance.addToFriendAccount("theUser@email.com");
        Mockito.when(tadoInterface.tadoMe()).thenReturn(me);

        List<String> friends=instance.getFriendAccountUsernames();
        assertEquals(2, friends.size());     
        
        instance.reset();
        friends=instance.getFriendAccountUsernames();
        assertEquals(0, friends.size());     
    }    
    
    /**
     * Test of getAccount method, of class AccountServiceImpl.
     */
    @Test
    public void testGetAccount()
    {
        Account result;
        System.out.println("getAccount");

        Mockito.reset(tadoInterface);
        Mockito.when(tadoInterface.tadoMe()).thenReturn(me);
        result = instance.getAccount();
        assertEquals(me.getId(), result.getId());
        assertEquals(me.getName(), result.getName());
        assertEquals(me.getHomes().get(0).getName(), result.getOwnHomes().get(0).getName());
        verify(tadoInterface, times(2)).tadoMe();
    }

    
    /**
     * Test of getAccount method, of class AccountServiceImpl.
     */
    @Test
    public void testGetAccount_exceptions()
    {
        Account result;
        System.out.println("getAccount - exception");

        // Account not found
        Mockito.when(tadoInterface.tadoMe()).thenReturn(null);
        exceptionRule.expect(ServiceException.class);
        exceptionRule.expectMessage("Account not found. Unauthorized access.");
        result = instance.getAccount();

        // Error from tado
        String message="Exception message";
        int    code   =403;
        exceptionRule.expect(ServiceException.class);
        exceptionRule.expectMessage("Tado Client error: "+message+". Status code: "+code);
        Mockito.reset(tadoInterface);
        Mockito.when(tadoInterface.tadoMe()).thenThrow(new TadoException(TadoException.TadoExceptionType.CLIENT_ERROR, message, code));
        result = instance.getAccount();
    }

    /**
     * Test of getAccount method, of class AccountServiceImpl.
     */
    @Test
    public void testAddToFriendAccount()
    {
        Account meAccount;
        Account friendAccount;
        System.out.println("addToFriendAccount");


        Mockito.reset(tadoInterface);
        Mockito.when(tadoInterface.tadoMe()).thenReturn(me);

        instance.addToFriendAccount("friend1@email.com");
        List<String> friends=instance.getFriendAccountUsernames();
        assertNotNull(friends);
        assertEquals(0, friends.size());
        verify(tadoInterface, times(2)).tadoMe();
        
        Mockito.reset(tadoInterface);
        Mockito.when(tadoInterface.tadoMe()).thenReturn(friend1);
        friends=instance.getFriendAccountUsernames();
        assertNotNull(friends);
        assertEquals(1, friends.size());
        assertEquals(me.getUsername(), friends.get(0));
        verify(tadoInterface, times(1)).tadoMe();
    }
    
    /**
     * Test of getAccount method, of class AccountServiceImpl.
     */
    @Test
    public void testGetAvailableHomes()
    {
        System.out.println("getAvailableHomes");

        Mockito.reset(tadoInterface);
        Mockito.when(accountManager.findAccount("theUser@email.com")).thenReturn(meAccount);
        Mockito.when(accountManager.findAccount("friend1@email.com")).thenReturn(friend1Account);
        Mockito.when(accountManager.findAccount("friend2@email.com")).thenReturn(friend2Account);

        Mockito.when(tadoInterface.tadoMe()).thenReturn(friend1);
        instance.addToFriendAccount("theUser@email.com");
        instance.addToFriendAccount("friend2@email.com");
        Mockito.when(tadoInterface.tadoMe()).thenReturn(friend2);
        instance.addToFriendAccount("theUser@email.com");

        Mockito.when(tadoInterface.tadoMe()).thenReturn(me);
        List<HomeId> homes=instance.getAvailableHomes();
        assertEquals(4, homes.size());
        assertEquals(12345, homes.get(0).getId());
        assertEquals(54321, homes.get(1).getId());
        assertEquals(54323, homes.get(2).getId());
        assertEquals(54322, homes.get(3).getId());

        // Expect only own home
        Mockito.when(tadoInterface.tadoMe()).thenReturn(friend1);
        homes=instance.getAvailableHomes();
        assertEquals(1, homes.size());
        assertEquals(54321, homes.get(0).getId());
        assertEquals("friend1@email.com", homes.get(0).getAccountUserName());
        
        // Expect own home and friends home
        Mockito.when(tadoInterface.tadoMe()).thenReturn(friend2);
        homes=instance.getAvailableHomes();
        assertEquals(3, homes.size());
        assertEquals(54323, homes.get(0).getId());
        assertEquals(54322, homes.get(1).getId());
        assertEquals(54321, homes.get(2).getId());
        assertEquals("friend2@email.com", homes.get(0).getAccountUserName());
        assertEquals("friend2@email.com", homes.get(1).getAccountUserName());
        assertEquals("friend1@email.com", homes.get(2).getAccountUserName());
    }
    
    /**
     * Test of reset method, of class AccountServiceImpl.
     */
    @Test
    public void testGetFriendAccountUsernames()
    {
        System.out.println("getFriendAccountUsernames");

        Mockito.reset(tadoInterface);
        Mockito.when(accountManager.findAccount("theUser@email.com")).thenReturn(meAccount);
        Mockito.when(accountManager.findAccount("friend1@email.com")).thenReturn(friend1Account);
        Mockito.when(accountManager.findAccount("friend2@email.com")).thenReturn(friend2Account);

        Mockito.when(tadoInterface.tadoMe()).thenReturn(friend2);
        instance.addToFriendAccount("theUser@email.com");
        Mockito.when(tadoInterface.tadoMe()).thenReturn(friend1);
        instance.addToFriendAccount("theUser@email.com");
        Mockito.when(tadoInterface.tadoMe()).thenReturn(me);

        List<String> friends=instance.getFriendAccountUsernames();
        assertEquals(2, friends.size());
        assertEquals("friend1@email.com", friends.get(0));
        assertEquals("friend2@email.com", friends.get(1));
    }    
    
    
}
