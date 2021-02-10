/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado;

import java.util.List;
import java.net.URI;
import java.text.SimpleDateFormat;
import net.studioblueplanet.homecontrol.tado.entities.TadoHome;
import net.studioblueplanet.homecontrol.tado.entities.TadoMe;
import net.studioblueplanet.homecontrol.tado.entities.TadoToken;
import net.studioblueplanet.homecontrol.tado.entities.TadoState;
import net.studioblueplanet.homecontrol.tado.entities.TadoZone;
import net.studioblueplanet.homecontrol.tado.entities.TadoZoneState;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;      
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import net.studioblueplanet.homecontrol.Application;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.File;
import java.nio.file.Files;
import java.util.TimeZone;

/**
 *
 * @author jorgen
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TadoInterfaceImplTest
{
    @Autowired
    TadoInterface                   tadoInterface;
    
    @Autowired
    private RestTemplate            restTemplate;

    private MockRestServiceServer   mockServer;
    private static SimpleDateFormat dateFormat;
    
    public TadoInterfaceImplTest()
    {
    }
    
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
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }
    
    @After
    public void tearDown()
    {
    }
    
    
    private TadoToken authenticate() throws Exception
    {
        String username = "name";
        String password = "noonewillguessthis";
        String expectedRequestBody="client_id=tado-web-app&scope=home.user&"+
                                   "grant_type=password&username="+username+"&password="+password+"&"+
                                   "client_secret=wZaRN7rpjn3FoNyF5IFuxg9uMzYJcvOoQ8QWiIqS3hfk6gLhVlG57j5YNoZL2Rtc";

        String body = new String(Files.readAllBytes((new File("src/test/resources/tadoToken.json")).toPath()));        
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://auth.tado.com/oauth/token")))
          .andExpect(method(HttpMethod.POST))
          .andExpect(content().string(expectedRequestBody))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(body)
        );                                   
                       
        TadoToken result = tadoInterface.authenticate(username, password);
        return result;
    }

    /**
     * Test of run method, of class TadoInterfaceImpl.
     */
    /**
    @Test
    public void testRun()
    {
        System.out.println("run");
        TadoInterfaceImpl instance = new TadoInterfaceImpl();
        instance.run();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    */

    /**
     * Test of authenticate method, of class TadoInterfaceImpl.
     */
    @Test
    public void testAuthenticate() throws Exception
    {
        System.out.println("authenticate");

        TadoToken result;
        result=authenticate();
        assertEquals("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI2MjljYjBlNS00MTg0LTQ3YjktYjFhMS1jN2Y4MjZkOTlkNjkiLCJ0YWRvX2hvbWVzIjpbeyJpZCI6NjMxMzE0fV0sImlzcyI6InRhZG8iLCJsb2NhbGUiOiJlbiIsImF1ZCI6InBhcnRuZXIiLCJuYmYiOjE2MTIyMDAzMjEsInRhZG9fc2NvcGUiOlsiaG9tZS51c2VyIl0sInRhZG9fdXNlcm5hbWUiOiJzY3ViYWpvcmdlbkBnbWFpbC5jb20iLCJuYW1lIjoiSm9yZ2VuIHZhbiBkZXIgVmVsZGUiLCJleHAiOjE2MTIyMDA5MjEsImlhdCI6MTYxMjIwMDMyMSwidGFkb19jbGllbnRfaWQiOiJ0YWRvLXdlYi1hcHAiLCJqdGkiOiJmZjIyNTcxZC1kMmU1LTQ1NTgtOWRlMi04NzI2ZjljZTc3ZDciLCJlbWFpbCI6InNjdWJham9yZ2VuQGdtYWlsLmNvbSJ9.k3Ug6k6h4LGQiXUIGAoAAfEEpOKlCJho0sMQY7Ed-cZu87rcQVrbtnVkDDEzGRWyRIrhhuxEzlN9_NKzs9qHFMvi0smMB--OYOZzLQyB4zGerkpvZ0WnJyxjqizFvtqFbYqG_5JMTES-QY8b5KQWioLKJZx8CBQlFIuM2EhXqRp2DE5iL-aBKvaGU8YS8VPdA1dmicO1TZd0fLKMU7TwyErdecVYfNVoENRdZOVjv_9b-IzlEh-NjaP5hOYJl0XEObVCydYdg-Jpwt_75iWtfD154qg0_k0UtDyJ7hOxwLMLx7Z2hZ_8stM58RtxpYWJYc8nIhmo2RNvEcxK8FMbHw", result.getAccess_token());
        mockServer.verify();
    }

    /**
     * Test of signOut method, of class TadoInterfaceImpl.
     */
    @Test
    public void testSignOut()
    {
        System.out.println("signOut");
        tadoInterface.signOut();
    }

    /**
     * Test of tadoMe method, of class TadoInterfaceImpl.
     */

    @Test
    public void testTadoMe() throws Exception
    {
        System.out.println("tadoMe");
        authenticate();

        mockServer.reset();
        String body = new String(Files.readAllBytes((new File("src/test/resources/tadoMe.json")).toPath()));
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/me/")))
          .andExpect(method(HttpMethod.GET))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(body)
        );                                   
            
        TadoMe result = tadoInterface.tadoMe();
        assertEquals("12345678-1234-1234-1234-0123456789AB", result.getId());
        assertEquals("User name", result.getName());
        assertEquals("user@email.com", result.getEmail());
        assertEquals(1, result.getHomes().size());
        assertEquals(123456, result.getHomes().get(0).getId());
        assertEquals("HQ", result.getHomes().get(0).getName());
        assertEquals("en", result.getLocale());
        assertEquals(1, result.getMobileDevices().size());
        assertEquals("Sergeant Major", result.getMobileDevices().get(0).getName());
        assertEquals(2404784, result.getMobileDevices().get(0).getId());
        assertEquals(true, result.getMobileDevices().get(0).getSettings().isGeoTrackingEnabled());
        assertEquals(false, result.getMobileDevices().get(0).getSettings().isOnDemandLogRetrievalEnabled());
        assertEquals(true, result.getMobileDevices().get(0).getSettings().getPushNotifications().isLowBatteryReminder());
        assertEquals(true, result.getMobileDevices().get(0).getSettings().getPushNotifications().isAwayModeReminder());
        assertEquals(true, result.getMobileDevices().get(0).getSettings().getPushNotifications().isHomeModeReminder());
        assertEquals(true, result.getMobileDevices().get(0).getSettings().getPushNotifications().isOpenWindowReminder());
        assertEquals(true, result.getMobileDevices().get(0).getSettings().getPushNotifications().isEnergySavingsReportReminder());
        assertEquals(false, result.getMobileDevices().get(0).getSettings().getPushNotifications().isIncidentDetection());
        assertEquals(false, result.getMobileDevices().get(0).getLocation().isStale());
        assertEquals(false, result.getMobileDevices().get(0).getLocation().isAtHome());
        assertEquals(164.84135792101787, result.getMobileDevices().get(0).getLocation().getBearing().getDegrees(), 0.00001);
        assertEquals(2.8770244391801967, result.getMobileDevices().get(0).getLocation().getBearing().getRadians(), 0.00001);
        assertEquals(0.5597495393964038, result.getMobileDevices().get(0).getLocation().getRelativeDistanceFromHomeFence(), 0.00001);
        assertEquals("Android", result.getMobileDevices().get(0).getDeviceMetadata().getPlatform());
        assertEquals("8.0.0", result.getMobileDevices().get(0).getDeviceMetadata().getOsVersion());
        assertEquals("Samsung_SM-G930F", result.getMobileDevices().get(0).getDeviceMetadata().getModel());
        assertEquals("en", result.getMobileDevices().get(0).getDeviceMetadata().getLocale());
        
        mockServer.verify();
    }

    
    /**
     * Test of tadoHome method, of class TadoInterfaceImpl.
     */
    @Test
    public void testTadoHome() throws Exception
    {
        System.out.println("tadoHome");
        authenticate();

        mockServer.reset();
        String body = new String(Files.readAllBytes((new File("src/test/resources/tadoHome.json")).toPath()));
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/homes/123456")))
          .andExpect(method(HttpMethod.GET))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(body)
        );                                   
            
        TadoHome result = tadoInterface.tadoHome(123456);
        assertEquals(123456, result.getId());
        assertEquals("HQ", result.getName());
        assertEquals("2021-01-04 19:48:15", dateFormat.format(result.getDateCreated()));
        assertEquals("CELSIUS", result.getTemperatureUnit());
        assertEquals(null, result.getPartner());
        assertEquals(true, result.isSimpleSmartScheduleEnabled());
        assertEquals(400.0, result.getAwayRadiusInMeters(), 0.01);
        assertEquals(true, result.isInstallationCompleted());
        assertEquals(false, result.getIncidentDetection().isEnabled());
        assertEquals(true, result.getIncidentDetection().isSupported());
        assertEquals(false, result.isAutoAssistFreeTrialEnabled());
        assertEquals(0, result.getSkills().size());
        assertEquals("Jorgen van der Velde", result.getContactDetails().getName());
        assertEquals("user@email.com", result.getContactDetails().getEmail());
        assertEquals("+31612345678", result.getContactDetails().getPhone());
        assertEquals("Streetname 123", result.getAddress().getAddressLine1());
        assertEquals(null, result.getAddress().getAddressLine2());
        assertEquals("9737 JL", result.getAddress().getZipCode());
        assertEquals("Groningen", result.getAddress().getCity());
        assertEquals(null, result.getAddress().getState());
        assertEquals("NLD", result.getAddress().getCountry());
        assertEquals(53.2526633, result.getGeolocation().getLatitude(), 0.00001);
        assertEquals(6.5885474, result.getGeolocation().getLongitude(), 0.00001);
        assertEquals(true, result.isConsentGrantSkippable());
        
        mockServer.verify();
    }

    /**
     * Test of tadoZones method, of class TadoInterfaceImpl.
     */
    @Test
    public void testTadoZones() throws Exception
    {
        System.out.println("tadoZones");
        authenticate();

        mockServer.reset();
        String body = new String(Files.readAllBytes((new File("src/test/resources/tadoZones.json")).toPath()));
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/homes/123456/zones")))
          .andExpect(method(HttpMethod.GET))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(body)
        );                                   
            
        List<TadoZone> result = tadoInterface.tadoZones(123456);
        assertEquals(4, result.size());
        assertEquals("Huiskamer", result.get(0).getName());
        assertEquals(3, result.get(1).getId());
        assertEquals("HEATING", result.get(0).getType());
        assertEquals("2021-01-04 19:59:01", dateFormat.format(result.get(0).getDateCreated()));
        assertEquals(3, result.get(0).getDeviceTypes().size());
        assertEquals("RU02", result.get(0).getDeviceTypes().get(0));
        assertEquals(3, result.get(0).getDevices().size());
        assertEquals("VA02", result.get(0).getDevices().get(2).getDeviceType());
        assertEquals("VA1712727296", result.get(0).getDevices().get(2).getSerialNo());
        assertEquals("VA1712727296", result.get(0).getDevices().get(2).getShortSerialNo());
        assertEquals("75.2", result.get(0).getDevices().get(2).getCurrentFwVersion());
        assertEquals("2021-02-04 11:49:37", dateFormat.format(result.get(0).getDevices().get(2).getConnectionState().getTimestamp()));
        assertEquals(true, result.get(0).getDevices().get(2).getConnectionState().isValue());
        assertEquals("IDENTIFY", result.get(0).getDevices().get(2).getCharacteristics().getCapabilities().get(1));
        assertEquals("2021-01-07 20:39:13", dateFormat.format(result.get(0).getDevices().get(2).getMountingState().getTimestamp()));
        assertEquals("CALIBRATED", result.get(0).getDevices().get(2).getMountingState().getValue());
        assertEquals("NORMAL", result.get(0).getDevices().get(2).getBatteryState());
        assertEquals("ZONE_DRIVER", result.get(0).getDevices().get(2).getDuties().get(1));
        assertEquals(false, result.get(0).isReportAvailable());
        assertEquals(true, result.get(0).isSupportsDazzle());
        assertEquals(true, result.get(0).isDazzleEnabled());
        assertEquals(true, result.get(0).getDazzleMode().isSupported());
        assertEquals(true, result.get(0).getDazzleMode().isEnabled());
        assertEquals(900, result.get(0).getOpenWindowDetection().getTimeoutInSeconds());
        assertEquals(true, result.get(0).getOpenWindowDetection().isSupported());
        assertEquals(true, result.get(0).getOpenWindowDetection().isEnabled());
        
        assertEquals("Werkkamer", result.get(1).getName());
        assertEquals("Badkamer" , result.get(2).getName());
        assertEquals("Hot Water", result.get(3).getName());
        assertEquals(null, result.get(3).getDevices().get(0).getMountingState());
        mockServer.verify();
    }
    
    /**
     * Test of tadoState method, of class TadoInterfaceImpl.
     */
    @Test
    public void testTadoState() throws Exception
    {
        System.out.println("tadoState");
        authenticate();

        mockServer.reset();
        String body = new String(Files.readAllBytes((new File("src/test/resources/tadoState.json")).toPath()));
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/homes/123456/state")))
          .andExpect(method(HttpMethod.GET))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(body)
        );                                   
            
        TadoState result = tadoInterface.tadoState(123456);
        assertEquals("AWAY", result.getPresence());
        assertEquals(true, result.isPresenceLocked());

        mockServer.verify();
    }
    
    /**
     * Test of tadoZoneState method, of class TadoInterfaceImpl.
     */
    @Test
    public void testTadoZoneStateHeating() throws Exception
    {
        System.out.println("tadoZoneState - Heating");
        authenticate();

        mockServer.reset();
        String body = new String(Files.readAllBytes((new File("src/test/resources/tadoZoneState.json")).toPath()));
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/homes/123456/zones/1/state")))
          .andExpect(method(HttpMethod.GET))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(body)
        );                                   
            
        TadoZoneState result = tadoInterface.tadoZoneState(123456, 1);
        assertEquals("AWAY", result.getTadoMode());
        assertEquals(false, result.isGeolocationOverride());
        assertNull(result.getGeolocationOverrideDisableTime());
        assertNull(result.getPreparation());
        assertEquals("HEATING", result.getSetting().getType());
        assertEquals("ON", result.getSetting().getPower());
        assertEquals(17.00, result.getSetting().getTemperature().getCelsius(), 0.0001);
        assertEquals(62.60, result.getSetting().getTemperature().getFahrenheit(), 0.0001);
        assertNull(result.getOverlayType());
        assertNull(result.getOverlay());
        assertNull(result.getOpenWindow());
        assertNull(result.getNextScheduleChange());
        assertEquals("2021-02-08 06:00:00", dateFormat.format(result.getNextTimeBlock().getStart()));
        assertEquals("ONLINE", result.getLink().getState());
        
        assertEquals("PERCENTAGE", result.getActivityDataPoints().getHeatingPower().getType());
        assertEquals(32.00, result.getActivityDataPoints().getHeatingPower().getPercentage(), 0.0001);
        assertEquals("2021-02-06 19:20:28", dateFormat.format(result.getActivityDataPoints().getHeatingPower().getTimestamp()));
        
        assertEquals("TEMPERATURE", result.getSensorDataPoints().getInsideTemperature().getType());
        assertEquals(16.97, result.getSensorDataPoints().getInsideTemperature().getCelsius(), 0.0001);
        assertEquals(62.55, result.getSensorDataPoints().getInsideTemperature().getFahrenheit(), 0.0001);
        assertEquals("2021-02-06 19:18:03", dateFormat.format(result.getSensorDataPoints().getInsideTemperature().getTimestamp()));
        assertEquals(0.1, result.getSensorDataPoints().getInsideTemperature().getPrecision().getCelsius(), 0.0001);
        assertEquals(0.1, result.getSensorDataPoints().getInsideTemperature().getPrecision().getFahrenheit(), 0.0001);

        assertEquals("PERCENTAGE", result.getSensorDataPoints().getHumidity().getType());
        assertEquals(34.30, result.getSensorDataPoints().getHumidity().getPercentage(), 0.0001);
        assertEquals("2021-02-06 19:18:04", dateFormat.format(result.getSensorDataPoints().getHumidity().getTimestamp()));
        
        mockServer.verify();
    }

    /**
     * Test of tadoZoneState method, of class TadoInterfaceImpl.
     */
    @Test
    public void testTadoZoneStateHotwater() throws Exception
    {
        System.out.println("tadoZoneState - HW");
        authenticate();

        mockServer.reset();
        String body = new String(Files.readAllBytes((new File("src/test/resources/tadoZoneStateHw.json")).toPath()));
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/homes/123456/zones/0/state")))
          .andExpect(method(HttpMethod.GET))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(body)
        );                                   
            
        TadoZoneState result = tadoInterface.tadoZoneState(123456, 0);
        assertEquals("AWAY", result.getTadoMode());
        assertEquals(false, result.isGeolocationOverride());
        assertNull(result.getGeolocationOverrideDisableTime());
        assertNull(result.getPreparation());
        assertEquals("HOT_WATER", result.getSetting().getType());
        assertEquals("OFF", result.getSetting().getPower());
        assertNull(result.getSetting().getTemperature());
        assertNull(result.getOverlayType());
        assertNull(result.getOverlay());
        assertNull(result.getOpenWindow());
        assertNull(result.getNextScheduleChange());
        assertEquals("2021-02-07 21:00:00", dateFormat.format(result.getNextTimeBlock().getStart()));
        assertEquals("ONLINE", result.getLink().getState());
        
        assertNull(result.getActivityDataPoints().getHeatingPower());
        assertNull(result.getSensorDataPoints().getInsideTemperature());
        assertNull(result.getSensorDataPoints().getHumidity());
        
        mockServer.verify();
    }
}
