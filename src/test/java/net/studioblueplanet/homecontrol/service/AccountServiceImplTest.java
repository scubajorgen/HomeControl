/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.service;

import java.util.ArrayList;

import net.studioblueplanet.homecontrol.service.entities.Account;
import net.studioblueplanet.homecontrol.tado.TadoInterface;
import net.studioblueplanet.homecontrol.tado.entities.TadoMe;
import net.studioblueplanet.homecontrol.tado.entities.TadoHome;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;


import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    private AccountService instance;
    
    @MockBean
    private TadoInterface tadoInterface;
    
    private TadoMe me;
    
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
        me=new TadoMe();
        me.setId("Me ID");
        me.setName("this is me");
        me.setEmail("my email");
        me.setLocale("locale");
        me.setUsername("my username");

        TadoHome    home=new TadoHome();
        home.setId(12345);
        home.setName("Home");
        ArrayList<TadoHome> homes=new ArrayList<>();
        homes.add(home);
        me.setHomes(homes);
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of getAccount method, of class AccountServiceImpl.
     */
    @Test
    public void testGetAccount()
    {
        Account result;
        System.out.println("getAccount");

        Mockito.when(tadoInterface.tadoMe()).thenReturn(null);
        result = instance.getAccount();
        assertNull(result);

        Mockito.reset(tadoInterface);
        Mockito.when(tadoInterface.tadoMe()).thenReturn(me);
        result = instance.getAccount();
        assertEquals(me.getId(), result.getId());
        assertEquals(me.getName(), result.getName());
        assertEquals(me.getHomes().get(0).getName(), result.getHomes().get(0).getName());
        verify(tadoInterface, times(1)).tadoMe();
        
        // On the second call the account is fetched from cache and not from Tado
        result = instance.getAccount();
        assertEquals(me.getId(), result.getId());
        verify(tadoInterface, times(1)).tadoMe();  
    }
    
}
