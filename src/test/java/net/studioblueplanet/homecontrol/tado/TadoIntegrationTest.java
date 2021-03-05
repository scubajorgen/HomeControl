/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado;


import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import net.studioblueplanet.homecontrol.Application;
import net.studioblueplanet.homecontrol.tado.entities.TadoMe;
import net.studioblueplanet.homecontrol.tado.entities.TadoHome;
import net.studioblueplanet.homecontrol.tado.entities.TadoToken;
import net.studioblueplanet.homecontrol.tado.entities.TadoZone;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author jorgen
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ContextConfiguration
/**
 *
 * @author jorgen
 */
public class TadoIntegrationTest
{
    private static boolean          authenticated=false;
    
    @Autowired
    private TadoInterface           tadoInterface;
    
    @Autowired
    private RestTemplate            restTemplate;
    
    private static SimpleDateFormat dateFormat;
    
    private TadoToken               token;
    
    @Value("${enableTadoIntegrationTest}")
    private boolean integrationTestEnabled;
    
    @Value("${tadoUsername}")
    private String username;
    
    @Value("${tadoPassword}")
    private String password;
    
    @Value("${homeId}")
    private int homeId;
    
    @Value("${heatingZoneId}")
    private int heatingZoneId;
    
    @Value("${hotWaterZoneId}")
    private int hotWaterZoneId;
    
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();    
    
   
    @BeforeClass
    public static void setUpClass()
    {
        dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
        if (!authenticated && integrationTestEnabled)
        {
            token=tadoInterface.authenticate(username, password);
            // Make sure the account information is coupled to the account
            //tadoInterface.tadoMe();
            if (token!=null)
            {
                authenticated=true;
            }
        }
    }
    
    @After
    public void tearDown()
    {
    }
    
    
    /**
     * Test of authenticate method, of class TadoInterfaceImpl.
     */
    @Test
    public void testAuthenticate() throws Exception
    {
        assumeTrue(integrationTestEnabled);
        System.out.println("### Authenticate");
        assertNotNull(token);
        System.out.println("Token aquired: expires "+dateFormat.format(token.getAccessTokenExpires()));
    }
    
    /**
     * Test of tadoMe method, of class TadoInterfaceImpl.
     */
    @Test
    @WithTadoUser
    public void testTadoMe() throws Exception
    {
        assumeTrue(integrationTestEnabled);
        System.out.println("### tadoMe");
        TadoMe me=tadoInterface.tadoMe();
        assertNotNull(me);
        System.out.println("TadoMe retrieved for user "+me.getName());
        assertEquals(homeId, me.getHomes().get(0).getId());
    }    
    
    /**
     * Test of tadoHome method, of class TadoInterfaceImpl.
     */
    @Test
    @WithTadoUser
    public void testTadoHome() throws Exception
    {
        assumeTrue(integrationTestEnabled);
        System.out.println("### tadoHome");
        // TO DO: WHY IS NEXT STATEMENT NEEDED???
        tadoInterface.tadoMe();
        TadoHome home=tadoInterface.tadoHome(homeId);
        assertNotNull(home);
        System.out.println("TadoHome retrieved called "+home.getName());
        assertEquals(homeId, home.getId());
    }        

    /**
     * Test of tadoHome method, of class TadoInterfaceImpl.
     */
    @Test
    @WithTadoUser
    public void testTadoZones() throws Exception
    {
        assumeTrue(integrationTestEnabled);
        System.out.println("### tadoZones");
        // TO DO: WHY IS NEXT STATEMENT NEEDED???
        tadoInterface.tadoMe();
        List<TadoZone> zones=tadoInterface.tadoZones(homeId);
        assertNotNull(zones);
        System.out.println("TadoZones retrieved: Number of zones "+zones.size());
        zones.stream().forEach(zone -> System.out.println(zone.getName()+" - "+zone.getType()));
    }        

}
