/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import net.studioblueplanet.homecontrol.Application;
import net.studioblueplanet.homecontrol.tado.entities.TadoAirComfort;
import net.studioblueplanet.homecontrol.tado.entities.TadoDevice;
import net.studioblueplanet.homecontrol.tado.entities.TadoEmail;
import net.studioblueplanet.homecontrol.tado.entities.TadoMe;
import net.studioblueplanet.homecontrol.tado.entities.TadoName;
import net.studioblueplanet.homecontrol.tado.entities.TadoHome;
import net.studioblueplanet.homecontrol.tado.entities.TadoOverlay;
import net.studioblueplanet.homecontrol.tado.entities.TadoPassword;
import net.studioblueplanet.homecontrol.tado.entities.TadoPresence;
import net.studioblueplanet.homecontrol.tado.entities.TadoScheduleBlock;
import net.studioblueplanet.homecontrol.tado.entities.TadoState;
import net.studioblueplanet.homecontrol.tado.entities.TadoTemperature;
import net.studioblueplanet.homecontrol.tado.entities.TadoTimeTable;
import net.studioblueplanet.homecontrol.tado.entities.TadoToken;
import net.studioblueplanet.homecontrol.tado.entities.TadoWeather;
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
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;


/**
 * Integration test with the Tado API. In order to execute the test
 * modify application.properties: enable the test and provide credentials.
 * Note; this may disrupt your Tado operation.
 * @author jorgen
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@DirtiesContext(classMode=ClassMode.BEFORE_CLASS) // don't reuse context from other tests
@ContextConfiguration
/**
 *
 * @author jorgen
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TadoIntegrationTest
{
    private static final Logger     LOG = LoggerFactory.getLogger(TadoIntegrationTest.class);  

    private static boolean          authenticated=false;

    private static SimpleDateFormat dateFormat;
    private static SimpleDateFormat timeFormat;
    
    private static TadoToken        token;
    
    @Autowired
    private TadoInterface           tadoInterface;
    
    @Value("${enableTadoIntegrationTest:false}")
    private boolean integrationTestEnabled;
    
    @Value("${skipCredentialsTests:true}")
    private boolean skipCredentialsTests;
    
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
        timeFormat=new SimpleDateFormat("HH:mm:ss");
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
        LOG.info("#########################################################################################");
        if (!authenticated && integrationTestEnabled)
        {
            LOG.info("# First test: authenticate");
            token=tadoInterface.authenticate(username, password);
            // Make sure the account information is coupled to the account
            tadoInterface.tadoMe();
            if (token!=null)
            {
                LOG.info("### Authenticated. Token aquired: expires {}", dateFormat.format(token.getAccessTokenExpires()));
                authenticated=true;
            }
        }
        if (!integrationTestEnabled)
        {
            LOG.info("#########################################################################################");
            LOG.info("### Integration test skipped because they are disabled. Enable in application.properties.");
            LOG.info("#########################################################################################");
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
    @WithTadoUser
    public void test01Authenticate() throws Exception
    {
        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
        LOG.info("### Authenticate");
        assertNotNull(token);
        LOG.info("# Token aquired for scope '{}', type '{}': expires {}", token.getScope(), token.getToken_type(), dateFormat.format(token.getAccessTokenExpires()));
    }
    
    /**
     * Test of tadoMe method, of class TadoInterfaceImpl.
     */
    @Test
    @WithTadoUser
    public void test02TadoMe() throws Exception
    {
        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
        LOG.info("### tadoMe");
        TadoMe me=tadoInterface.tadoMe();
        assertNotNull(me);
        LOG.info("# TadoMe retrieved for user {}, username {}", me.getName(), me.getUsername());
        assertEquals(homeId, me.getHomes().get(0).getId());
    }    
    
    /**
     * Test of tadoHome method, of class TadoInterfaceImpl.
     */
    @Test
    @WithTadoUser
    public void test03TadoHome() throws Exception
    {
        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
        LOG.info("### tadoHome");
        TadoHome home=tadoInterface.tadoHome(homeId);
        assertNotNull(home);
        LOG.info("# TadoHome retrieved called {}, address {}, {}", home.getName(), 
                                                                   home.getAddress().getAddressLine1(), 
                                                                   home.getAddress().getCity());
        assertEquals(homeId, home.getId());
    }        

    /**
     * Test of tadoHome method, of class TadoInterfaceImpl.
     */
    @Test
    @WithTadoUser
    public void test04TadoZones() throws Exception
    {
        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
        LOG.info("### tadoZones");
        List<TadoZone> zones=tadoInterface.tadoZones(homeId);
        assertNotNull(zones);
        LOG.info("# TadoZones retrieved: Number of zones {}", zones.size());
        zones.stream().forEach(zone -> LOG.info("# {} - {}", zone.getName(), zone.getType()));
    }        

    /**
     * Test of tadoPresence method, of class TadoInterfaceImpl.
     */
    @Test
    @WithTadoUser
    public void test05TadoPresence() throws Exception
    {
        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
        LOG.info("### tadoPresence");
        TadoState presence=tadoInterface.tadoPresence(homeId);
        assertNotNull(presence);
        LOG.info("# Tado Presence retrieved: you are {}", presence.getPresence());
    }  

    /**
     * Test of tadoZoneState method, of class TadoInterfaceImpl.
     */
    @Test
    @WithTadoUser
    public void test06TadoZoneState() throws Exception
    {
        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
        LOG.info("### tadoZoneState");
        TadoZoneState state=tadoInterface.tadoZoneState(homeId, heatingZoneId);
        assertNotNull(state);
        LOG.info("# Tado ZoneState retrieved: Zone {}, Temperature {}", state.getSetting().getType(), state.getSetting().getTemperature().getCelsius());
    }  

    /**
     * Test of tadoZoneState method, of class TadoInterfaceImpl.
     */
    @Test
    @WithTadoUser
    public void test07SetPresence() throws Exception
    {
        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
        LOG.info("### setTadoPresence");
        TadoState presenceBackup=tadoInterface.tadoPresence(homeId);
        assertNotNull(presenceBackup);
        assertTrue(presenceBackup.getPresence().equals("HOME") || presenceBackup.getPresence().equals("AWAY"));
        LOG.info("# Presence backup: {}", presenceBackup.getPresence());
        
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
        LOG.info("# Presence set to {}", readback.getPresence());

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
        LOG.info("# Presence set back to {}", readback.getPresence());

    }  

    /**
     * Test of tadoOverlay method, of class TadoInterfaceImpl.
     */
    @Test
    @WithTadoUser
    public void test08TadoOverlay() throws Exception
    {
        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
        LOG.info("### tadoOverlay, setTadoOverlay, deleteTadoOverlay");

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
            LOG.info("# Test aborted. Manual control already set to HEATING test zone. Set to auto and retry.");
        }
        assumeTrue(("Client error: Tado reported: 404 - overlay of zone "+heatingZoneId+" not found").equals(message));
            
        // Set an overlay
        TadoOverlay result=tadoInterface.setTadoOverlay(homeId, heatingZoneId, "HEATING", "ON", 23.4, "MANUAL", 0);
        assertEquals(23.4, result.getSetting().getTemperature().getCelsius(), 0.01);
        assertEquals("MANUAL", result.getType());
        LOG.info("# Overlay set: type {}, temperature {}", result.getType(), result.getSetting().getTemperature().getCelsius());

        // Read it back
        result=tadoInterface.tadoOverlay(homeId, heatingZoneId);
        assertNotNull(result);
        assertEquals(23.4, result.getSetting().getTemperature().getCelsius(), 0.01);
        assertEquals("MANUAL", result.getType());
        LOG.info("# Overlay read back: type {}, temperature {}", result.getType(), result.getSetting().getTemperature().getCelsius());
        
        // Delete
        tadoInterface.deleteTadoOverlay(homeId, heatingZoneId);
        LOG.info("# Overlay deleted");

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
        LOG.info("# Overlay deletion verified");
    }  

    /**
     * Test of tadoZoneState method, of class TadoInterfaceImpl.
     */
    @Test
    @WithTadoUser
    public void test09TadoDevices() throws Exception
    {
        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
        LOG.info("### tadoDevices");
        List<TadoDevice> devices=tadoInterface.tadoDevices(homeId);
        assertNotNull(devices);
        LOG.info("# Tado Device list retrieved: Number of devices {}", devices.size());
        devices.stream().forEach(d -> LOG.info("# Device: {}", d.getSerialNo()));
    }      

    /**
     * Test of tadoZoneState method, of class TadoInterfaceImpl.
     */
    @Test
    @WithTadoUser(username="")
    public void test10SetName() throws Exception
    {
        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
        LOG.info("### setName");
        TadoMe backup=tadoInterface.tadoMe();
        assertNotNull(backup);
        LOG.info("# Current name: {}", backup.getName());
        
        String temporaryName="John Doe";
        LOG.info("# Setting name to: {}", temporaryName);
        TadoMe result=tadoInterface.setName(new TadoName(temporaryName));
        assertEquals(temporaryName, result.getName());

        LOG.info("# Setting name to: {}", backup.getName());
        result=tadoInterface.setName(new TadoName(backup.getName()));
        assertEquals(backup.getName(), result.getName());
        LOG.info("# Name set to: {}", result.getName());
    } 

    /**
     * Test of setEmail method, of class TadoInterfaceImpl.
     */
    @Test
    @WithTadoUser
    public void test11SetEmail() throws Exception
    {
        TadoToken localToken;

        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
        assumeFalse("Skipping credentials tests", skipCredentialsTests);
        LOG.info("### setEmail");
        TadoMe backup=tadoInterface.tadoMe();
        assertNotNull(backup);
        LOG.info("# Current email: {}, current username: {}", backup.getEmail(), backup.getUsername());
        
        String temporaryEmail="johntadoe@gmail.com";
        LOG.info("# Setting email to: {}", temporaryEmail);
        TadoMe result=tadoInterface.setEmail(new TadoEmail(temporaryEmail, password));
        assertEquals(temporaryEmail, result.getEmail());
        assertEquals(temporaryEmail, result.getUsername());

        // Change the logged in user in the Spring security context
        User principal =new User(temporaryEmail, password, new ArrayList<GrantedAuthority>());
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);        
        
        // Authenticate at Tado
        localToken=tadoInterface.authenticate(temporaryEmail, password);
        assertNotNull(localToken);
        
        LOG.info("# Setting email to: {}", backup.getEmail());
        result=tadoInterface.setEmail(new TadoEmail(backup.getEmail(), password));
        assertEquals(backup.getEmail(), result.getEmail());
        assertEquals(backup.getEmail(), result.getUsername());
        LOG.info("# Email set to: {}, username is: {}", result.getEmail(), result.getUsername());

        // Change the logged in user in the Spring security context
        principal =new User(temporaryEmail, password, new ArrayList<GrantedAuthority>());
        auth = new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
        sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);        

        tadoInterface.authenticate(username, password);
        assertNotNull(localToken);
    } 

    /**
     * Test of setPassword method, of class TadoInterfaceImpl.
     */
    @Test
    @WithTadoUser
    public void test12SetPassword() throws Exception
    {
        TadoToken localToken;

        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
        assumeFalse("Skipping credentials tests", skipCredentialsTests);
        LOG.info("### setPassword");
        
        String temporaryPassword="TempPasword123$#";
        LOG.info("# Setting password to: {}", temporaryPassword);
        tadoInterface.setPassword(new TadoPassword(temporaryPassword, password));
        // Authenticate at Tado
        localToken=tadoInterface.authenticate(username, temporaryPassword);
        assertNotNull(localToken);
        
        LOG.info("# Setting password to original password: {}", password);
        tadoInterface.setPassword(new TadoPassword(password, temporaryPassword));
        // Authenticate at Tado
        localToken=tadoInterface.authenticate(username, password);
        assertNotNull(localToken);
    }     

    /**
     * Test of tadoTimeTables method, of class TadoInterfaceImpl.
     */
    @Test
    @WithTadoUser
    public void test13TadoTimeTables() throws Exception
    {
        TadoToken localToken;

        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
        LOG.info("### tadoTimeTables");
        
        List<TadoTimeTable> result=tadoInterface.tadoTimeTables(homeId, heatingZoneId);
        assertNotNull(result);
        LOG.info("# Timetables available for home {} zone {}: ", homeId, heatingZoneId);
        result.stream().forEach(t -> LOG.info("# Time table: type {} ({})", t.getType(), t.getId()));
    }  

    /**
     * Test of tadoTimeTables method, of class TadoInterfaceImpl.
     */
    @Test
    @WithTadoUser
    public void test14TadoActiveTimeTable() throws Exception
    {
        TadoToken localToken;

        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
        LOG.info("### tadoActiveTimeTable, tadoSetActiveTimeTable");
        
        TadoTimeTable result=tadoInterface.tadoActiveTimeTable(homeId, heatingZoneId);
        assertNotNull(result);
        LOG.info("# Active time table for home {} zone {}: {} - {}", homeId, heatingZoneId, result.getId(), result.getType());

        int backupId=result.getId();
        int newTimeTableId=(backupId+1)%3;
        
        result=tadoInterface.tadoSetActiveTimeTable(homeId, heatingZoneId, new TadoTimeTable(newTimeTableId));
        assertNotNull(result);
        assertEquals(newTimeTableId, result.getId());
        LOG.info("# Active time table set to {} - {}", result.getId(), result.getType());

        result=tadoInterface.tadoSetActiveTimeTable(homeId, heatingZoneId, new TadoTimeTable(backupId));
        assertNotNull(result);
        assertEquals(backupId, result.getId());
        LOG.info("# Active time table reset to {} - {}", result.getId(), result.getType());
    }   
    
    
    /**
     * Test of tadoScheduleBlocks method, of class TadoInterfaceImpl.
     */
    @Test
    @WithTadoUser
    public void test15TadoScheduleBlocks() throws Exception
    {
        TadoToken localToken;
        
        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
        LOG.info("### tadoScheduleBlocks, tadoActiveTimeTable");
        
        TadoTimeTable current=tadoInterface.tadoActiveTimeTable(homeId, heatingZoneId);
        assertNotNull(current);
        LOG.info("# Current time table for home {}, zone {}: {}", homeId, heatingZoneId, current.getType());
        
        List<TadoScheduleBlock> result=tadoInterface.tadoScheduleBlocks(homeId, heatingZoneId, current.getId());
        assertNotNull(result);
        LOG.info("# Schedule available for home {} zone {} for {}: ", homeId, heatingZoneId, current.getType());
        result.stream().forEach(t -> LOG.info("# {} {} - {}: {} at {} C", 
                                                t.getDayType(),
                                                t.getStart().toString(),
                                                t.getEnd().toString(),
                                                t.getSetting().getPower(),
                                                t.getSetting().getTemperature()!=null?t.getSetting().getTemperature().getCelsius():0));
    }

    /**
     * Test of tadoSetScheduleBlocks method, of class TadoInterfaceImpl.
     */
    @Test
    @WithTadoUser
    public void test16TadoSetScheduleBlocks() throws Exception
    {
        TadoToken localToken;
        
        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
        LOG.info("### tadoSetScheduleBlocks, tadoScheduleBlocks");
        
        TadoTimeTable activeTimeTable=tadoInterface.tadoActiveTimeTable(homeId, heatingZoneId);
        assertNotNull(activeTimeTable);
        int activeTimeTableId=activeTimeTable.getId();
        LOG.info("# Current time table for home {}, zone {}: {}", homeId, heatingZoneId, activeTimeTable.getType());

        List<TadoScheduleBlock> schedule=tadoInterface.tadoScheduleBlocks(homeId, heatingZoneId, activeTimeTableId);
        assertNotNull(schedule);
        LOG.info("# Schedule available for home {} zone {} for schedule {}: {}-{}", homeId, heatingZoneId, activeTimeTableId, activeTimeTable.getType());

        String firstDay=schedule.get(0).getDayType();
        
        List<TadoScheduleBlock> blocks=schedule.stream().filter(f -> f.getDayType().equals(firstDay)).collect(Collectors.toList());
        
        String backupPower=blocks.get(0).getSetting().getPower();
        TadoTemperature backupTemperature=null;
        
        if (backupPower.equals("ON"))
        {
            blocks.get(0).getSetting().setPower("OFF");
            backupTemperature=blocks.get(0).getSetting().getTemperature();
            
        }
        else
        {
            blocks.get(0).getSetting().setPower("ON");
            blocks.get(0).getSetting().setTemperature(new TadoTemperature(20.1));
        }

        // Write modified schedule
        List<TadoScheduleBlock> readback=tadoInterface.tadoSetScheduleBlocks(homeId, heatingZoneId, activeTimeTableId, blocks);
        assertNotNull(readback);
        if (backupPower.equals("ON"))
        {
            assertEquals("OFF", readback.get(0).getSetting().getPower());
            assertNull(readback.get(0).getSetting().getTemperature());
            blocks.get(0).getSetting().setPower("ON");
            blocks.get(0).getSetting().setTemperature(backupTemperature);
            
        }
        else
        {
            assertEquals("ON", readback.get(0).getSetting().getPower());
            assertEquals(20.1, readback.get(0).getSetting().getTemperature().getCelsius(), 0.001);
            blocks.get(0).getSetting().setPower("OFF");
        }
        // Write original schedule
        readback=tadoInterface.tadoSetScheduleBlocks(homeId, heatingZoneId, activeTimeTableId, blocks);
        assertNotNull(readback);
    }

    /**
     * Test of tadoAirComfort method, of class TadoInterfaceImpl.
     */
    @Test
    @WithTadoUser
    public void test17TadoAirComfort() throws Exception
    {
        TadoToken localToken;
        
        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
        LOG.info("### tadoAirComfort");
        
        TadoAirComfort comfort=tadoInterface.tadoAirComfort(homeId);
        assertNotNull(comfort);
        LOG.info("# Freshness: {}, last open window {}", comfort.getFreshness().getValue(), 
                                                         dateFormat.format(comfort.getFreshness().getLastOpenWindow()));
        comfort.getComfort().stream().forEach(c -> LOG.info("# Zone {}: temperature {}, humidity {}",
                                                            c.getRoomId(), c.getTemperatureLevel(), c.getHumidityLevel()));                

    }

    /**
     * Test of tadoWeather method, of class TadoInterfaceImpl.
     */
    @Test
    @WithTadoUser
    public void test18TadoWeather() throws Exception
    {
        TadoToken localToken;
        
        assumeTrue("Integration test disabled, test not executed", integrationTestEnabled);
        LOG.info("### tadoWeather");
        
        TadoWeather weather=tadoInterface.tadoWeather(homeId);
        assertNotNull(weather);
        LOG.info("# Sun Intensity: {}%", weather.getSolarIntensity().getPercentage());
        LOG.info("# Outside temperature {} C", weather.getOutsideTemperature().getCelsius());
        LOG.info("# The weather is {}", weather.getWeatherState().getValue());
    }
}
