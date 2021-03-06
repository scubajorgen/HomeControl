/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import net.studioblueplanet.homecontrol.Application;
import net.studioblueplanet.homecontrol.tado.entities.TadoMe;
import net.studioblueplanet.homecontrol.tado.entities.TadoHome;
import net.studioblueplanet.homecontrol.tado.entities.TadoOverlay;
import net.studioblueplanet.homecontrol.tado.entities.TadoPresence;
import net.studioblueplanet.homecontrol.tado.entities.TadoState;
import net.studioblueplanet.homecontrol.tado.entities.TadoToken;
import net.studioblueplanet.homecontrol.tado.entities.TadoZone;
import net.studioblueplanet.homecontrol.tado.entities.TadoZoneState;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
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
 * Integration test with the Tado API. In order to execute the test
 * modify application.properties: enable the test and provide credentials.
 * Note; this may disrupt your Tado operation.
 * @author jorgen
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ContextConfiguration
/**
 *
 * @author jorgen
 */
@FixMethodOrder(MethodSorters.DEFAULT)
public class TadoIntegrationTest
{
    private static boolean          authenticated=false;

    private static SimpleDateFormat dateFormat;
    
    private static TadoToken        token;
    
    @Autowired
    private TadoInterface           tadoInterface;
    
    @Autowired
    private RestTemplate            restTemplate;
    
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
            tadoInterface.tadoMe();
            if (token!=null)
            {
                System.out.println("Authenticated. Token aquired: expires "+dateFormat.format(token.getAccessTokenExpires()));
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
        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
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
        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
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
        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
        System.out.println("### tadoHome");
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
        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
        System.out.println("### tadoZones");
        List<TadoZone> zones=tadoInterface.tadoZones(homeId);
        assertNotNull(zones);
        System.out.println("TadoZones retrieved: Number of zones "+zones.size());
        zones.stream().forEach(zone -> System.out.println(zone.getName()+" - "+zone.getType()));
    }        

    /**
     * Test of tadoPresence method, of class TadoInterfaceImpl.
     */
    @Test
    @WithTadoUser
    public void testTadoPresence() throws Exception
    {
        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
        System.out.println("### tadoPresence");
        TadoState presence=tadoInterface.tadoPresence(homeId);
        assertNotNull(presence);
        System.out.println("Tado Presence retrieved: you are "+presence.getPresence());
    }  

    /**
     * Test of tadoZoneState method, of class TadoInterfaceImpl.
     */
    @Test
    @WithTadoUser
    public void testTadoZoneState() throws Exception
    {
        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
        System.out.println("### tadoZoneState");
        TadoZoneState state=tadoInterface.tadoZoneState(homeId, heatingZoneId);
        assertNotNull(state);
        System.out.println("Tado ZoneState retrieved: Zone "+state.getSetting().getType()+" Temperature "+state.getSetting().getTemperature().getCelsius());
    }  

    /**
     * Test of tadoZoneState method, of class TadoInterfaceImpl.
     */
    @Test
    @WithTadoUser
    public void testSetPresence() throws Exception
    {
        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
        System.out.println("### setTadoPresence");
        TadoState presenceBackup=tadoInterface.tadoPresence(homeId);
        assertNotNull(presenceBackup);
        assertTrue(presenceBackup.getPresence().equals("HOME") || presenceBackup.getPresence().equals("AWAY"));
        System.out.println("Presence backup: "+presenceBackup.getPresence());
        
        TadoState readback;
        if (presenceBackup.getPresence().equals("HOME"))
        {
            tadoInterface.setTadoPresence(homeId, TadoPresence.TadoHomePresence.AWAY);
            readback=tadoInterface.tadoPresence(homeId);
            assertNotNull(readback);
            assertTrue(readback.getPresence().equals("AWAY"));
        }
        else
        {
            tadoInterface.setTadoPresence(homeId, TadoPresence.TadoHomePresence.HOME);
            readback=tadoInterface.tadoPresence(homeId);
            assertNotNull(readback);
            assertTrue(readback.getPresence().equals("HOME"));
        }
        System.out.println("Presence set to "+readback.getPresence());

        if (presenceBackup.getPresence().equals("HOME"))
        {
            tadoInterface.setTadoPresence(homeId, TadoPresence.TadoHomePresence.HOME);
            readback=tadoInterface.tadoPresence(homeId);
            assertNotNull(readback);
            assertTrue(readback.getPresence().equals("HOME"));
        }
        else
        {
            tadoInterface.setTadoPresence(homeId, TadoPresence.TadoHomePresence.AWAY);
            readback=tadoInterface.tadoPresence(homeId);
            assertNotNull(readback);
            assertTrue(readback.getPresence().equals("AWAY"));
        }
        System.out.println("Presence set back to "+readback.getPresence());

    }  

    /**
     * Test of tadoOverlay method, of class TadoInterfaceImpl.
     */
    @Test
    @WithTadoUser
    public void testTadoOverlay() throws Exception
    {
        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
        System.out.println("### tadoOverlay, setTadoOverlay, deleteTadoOverlay");

        // We assume no overlay has been set. Therefore a 404 exception
        String message=null;
        try
        {
            TadoOverlay overlay=tadoInterface.tadoOverlay(homeId, heatingZoneId);
        }
        catch (TadoException e)
        {
            message=e.getMessage();
        }
        if (!("Client error: Tado reported: 404 - overlay of zone "+heatingZoneId+" not found").equals(message))
        {
            System.out.println("Test aborted. Manual control already set to HEATING test zone. Set to auto and retry.");
        }
        assumeTrue(("Client error: Tado reported: 404 - overlay of zone "+heatingZoneId+" not found").equals(message));
            
        // Set an overlay
        TadoOverlay result=tadoInterface.setTadoOverlay(homeId, heatingZoneId, "HEATING", "ON", 23.4, "MANUAL", 0);
        assertEquals(23.4, result.getSetting().getTemperature().getCelsius(), 0.01);
        assertEquals("MANUAL", result.getType());

        // Read it back
        result=tadoInterface.tadoOverlay(homeId, heatingZoneId);
        assertNotNull(result);
        assertEquals(23.4, result.getSetting().getTemperature().getCelsius(), 0.01);
        assertEquals("MANUAL", result.getType());
        
        // Delete
        tadoInterface.deleteTadoOverlay(homeId, heatingZoneId);

        // We assume no overlay has been set. Therefore a 404 exception
        message=null;
        try
        {
            TadoOverlay overlay=tadoInterface.tadoOverlay(homeId, heatingZoneId);
        }
        catch (TadoException e)
        {
            message=e.getMessage();
        }
        assertEquals(("Client error: Tado reported: 404 - overlay of zone "+heatingZoneId+" not found"), message);
    }  

}
