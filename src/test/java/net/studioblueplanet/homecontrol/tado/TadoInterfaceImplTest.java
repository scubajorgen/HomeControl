/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado;

import java.util.List;
import java.util.ArrayList;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import net.studioblueplanet.homecontrol.tado.entities.TadoAirComfort;
import net.studioblueplanet.homecontrol.tado.entities.TadoAccount;
import net.studioblueplanet.homecontrol.tado.entities.TadoDevice;
import net.studioblueplanet.homecontrol.tado.entities.TadoEmail;
import net.studioblueplanet.homecontrol.tado.entities.TadoHome;
import net.studioblueplanet.homecontrol.tado.entities.TadoLanguage;
import net.studioblueplanet.homecontrol.tado.entities.TadoMe;
import net.studioblueplanet.homecontrol.tado.entities.TadoName;
import net.studioblueplanet.homecontrol.tado.entities.TadoOverlay;
import net.studioblueplanet.homecontrol.tado.entities.TadoPassword;
import net.studioblueplanet.homecontrol.tado.entities.TadoPresence.TadoHomePresence;
import net.studioblueplanet.homecontrol.tado.entities.TadoScheduleBlock;
import net.studioblueplanet.homecontrol.tado.entities.TadoSetting;
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
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;      
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import net.studioblueplanet.homecontrol.Application;
import org.springframework.test.context.ContextConfiguration;
import java.io.File;
import java.nio.file.Files;
import java.util.TimeZone;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 *
 * @author jorgen
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ContextConfiguration
public class TadoInterfaceImplTest
{
    @Autowired
    TadoInterface                   tadoInterface;
    
    @Autowired
    private RestTemplate            restTemplate;
    
    @MockBean
    private TadoAccountManager      accountManager;

    private MockRestServiceServer   mockServer;
    private static SimpleDateFormat dateFormat;
    
    private TadoAccount             meAccount;
    private TadoAccount             friendAccount;
    
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();    
    
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
        // Forget account information
        tadoInterface.reset();
        
        TadoToken token=new TadoToken();
        token.setAccess_token("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI2MjljYjBlNS00MTg0LTQ3YjktYjFhMS1jN2Y4MjZkOTlkNjkiLCJ0YWRvX2hvbWVzIjpbeyJpZCI6NjMxMzE0fV0sImlzcyI6InRhZG8iLCJsb2NhbGUiOiJlbiIsImF1ZCI6InBhcnRuZXIiLCJuYmYiOjE2MTIyMDAzMjEsInRhZG9fc2NvcGUiOlsiaG9tZS51c2VyIl0sInRhZG9fdXNlcm5hbWUiOiJzY3ViYWpvcmdlbkBnbWFpbC5jb20iLCJuYW1lIjoiSm9yZ2VuIHZhbiBkZXIgVmVsZGUiLCJleHAiOjE2MTIyMDA5MjEsImlhdCI6MTYxMjIwMDMyMSwidGFkb19jbGllbnRfaWQiOiJ0YWRvLXdlYi1hcHAiLCJqdGkiOiJmZjIyNTcxZC1kMmU1LTQ1NTgtOWRlMi04NzI2ZjljZTc3ZDciLCJlbWFpbCI6InNjdWJham9yZ2VuQGdtYWlsLmNvbSJ9.k3Ug6k6h4LGQiXUIGAoAAfEEpOKlCJho0sMQY7Ed-cZu87rcQVrbtnVkDDEzGRWyRIrhhuxEzlN9_NKzs9qHFMvi0smMB--OYOZzLQyB4zGerkpvZ0WnJyxjqizFvtqFbYqG_5JMTES-QY8b5KQWioLKJZx8CBQlFIuM2EhXqRp2DE5iL-aBKvaGU8YS8VPdA1dmicO1TZd0fLKMU7TwyErdecVYfNVoENRdZOVjv_9b-IzlEh-NjaP5hOYJl0XEObVCydYdg-Jpwt_75iWtfD154qg0_k0UtDyJ7hOxwLMLx7Z2hZ_8stM58RtxpYWJYc8nIhmo2RNvEcxK8FMbHw");
        meAccount=new TadoAccount("username@email.com", "noonewillguessthis", token);
        TadoMe me=new TadoMe();
        List<TadoHome> myHomes=new ArrayList<>();
        TadoHome myHome=new TadoHome();
        myHome.setId(123456);
        myHomes.add(myHome);
        me.setHomes(myHomes);
        meAccount.setUsername("user@email.com");
        meAccount.setTadoMe(me);
        
        friendAccount=new TadoAccount("friend@email.com", "noonewillguessthis", token);
        TadoMe friend=new TadoMe();
        List<TadoHome> friendHomes=new ArrayList<>();
        TadoHome friendHome=new TadoHome();
        friendHome.setId(654321);
        friendHomes.add(friendHome);
        friend.setHomes(friendHomes);
        friendAccount.setUsername("friend@email.com");
        friendAccount.setTadoMe(friend);
        
        Mockito.when(accountManager.findAccount(123456)).thenReturn(meAccount);
        Mockito.when(accountManager.loggedInAccount()).thenReturn(meAccount);
    }
    
    @After
    public void tearDown()
    {
    }
    
    
    private TadoToken authenticate() throws Exception
    {
        String username = "username@email.com";
        String password = "noonewillguessthis";
        String expectedRequestBody="client_id=tado-web-app&scope=home.user&"+
                                   "grant_type=password&username="+URLEncoder.encode(username, StandardCharsets.UTF_8.toString())+"&password="+password+"&"+
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
     * Test of authenticate method, of class TadoInterfaceImpl.
     */
    @Test
    public void testAuthenticate() throws Exception
    {
        System.out.println("authenticate");

        Mockito.when(accountManager.findAccount("username@email.com")).thenReturn(null);

        String username = "username@email.com";
        String password = "noonewillguessthis";
        String expectedRequestBody="client_id=tado-web-app&scope=home.user&"+
                                   "grant_type=password&username="+URLEncoder.encode(username, StandardCharsets.UTF_8.toString())+"&password="+password+"&"+
                                   "client_secret=wZaRN7rpjn3FoNyF5IFuxg9uMzYJcvOoQ8QWiIqS3hfk6gLhVlG57j5YNoZL2Rtc";

        String body = new String(Files.readAllBytes((new File("src/test/resources/tadoToken.json")).toPath()));        
        mockServer.expect(ExpectedCount.times(2), 
          requestTo(new URI("https://auth.tado.com/oauth/token")))
          .andExpect(method(HttpMethod.POST))
          .andExpect(content().string(expectedRequestBody))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(body)
        );                                   

        
        TadoToken result;
        tadoInterface.reset();
        result=tadoInterface.authenticate(username, password);
        assertEquals("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI2MjljYjBlNS00MTg0LTQ3YjktYjFhMS1jN2Y4MjZkOTlkNjkiLCJ0YWRvX2hvbWVzIjpbeyJpZCI6NjMxMzE0fV0sImlzcyI6InRhZG8iLCJsb2NhbGUiOiJlbiIsImF1ZCI6InBhcnRuZXIiLCJuYmYiOjE2MTIyMDAzMjEsInRhZG9fc2NvcGUiOlsiaG9tZS51c2VyIl0sInRhZG9fdXNlcm5hbWUiOiJzY3ViYWpvcmdlbkBnbWFpbC5jb20iLCJuYW1lIjoiSm9yZ2VuIHZhbiBkZXIgVmVsZGUiLCJleHAiOjE2MTIyMDA5MjEsImlhdCI6MTYxMjIwMDMyMSwidGFkb19jbGllbnRfaWQiOiJ0YWRvLXdlYi1hcHAiLCJqdGkiOiJmZjIyNTcxZC1kMmU1LTQ1NTgtOWRlMi04NzI2ZjljZTc3ZDciLCJlbWFpbCI6InNjdWJham9yZ2VuQGdtYWlsLmNvbSJ9.k3Ug6k6h4LGQiXUIGAoAAfEEpOKlCJho0sMQY7Ed-cZu87rcQVrbtnVkDDEzGRWyRIrhhuxEzlN9_NKzs9qHFMvi0smMB--OYOZzLQyB4zGerkpvZ0WnJyxjqizFvtqFbYqG_5JMTES-QY8b5KQWioLKJZx8CBQlFIuM2EhXqRp2DE5iL-aBKvaGU8YS8VPdA1dmicO1TZd0fLKMU7TwyErdecVYfNVoENRdZOVjv_9b-IzlEh-NjaP5hOYJl0XEObVCydYdg-Jpwt_75iWtfD154qg0_k0UtDyJ7hOxwLMLx7Z2hZ_8stM58RtxpYWJYc8nIhmo2RNvEcxK8FMbHw", result.getAccess_token());
        
        // On second authenticate we again expect a call to the server
        Mockito.when(accountManager.findAccount("username@email.com")).thenReturn(meAccount);
        result = tadoInterface.authenticate("username@email.com", "noonewillguessthis");
        assertEquals("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI2MjljYjBlNS00MTg0LTQ3YjktYjFhMS1jN2Y4MjZkOTlkNjkiLCJ0YWRvX2hvbWVzIjpbeyJpZCI6NjMxMzE0fV0sImlzcyI6InRhZG8iLCJsb2NhbGUiOiJlbiIsImF1ZCI6InBhcnRuZXIiLCJuYmYiOjE2MTIyMDAzMjEsInRhZG9fc2NvcGUiOlsiaG9tZS51c2VyIl0sInRhZG9fdXNlcm5hbWUiOiJzY3ViYWpvcmdlbkBnbWFpbC5jb20iLCJuYW1lIjoiSm9yZ2VuIHZhbiBkZXIgVmVsZGUiLCJleHAiOjE2MTIyMDA5MjEsImlhdCI6MTYxMjIwMDMyMSwidGFkb19jbGllbnRfaWQiOiJ0YWRvLXdlYi1hcHAiLCJqdGkiOiJmZjIyNTcxZC1kMmU1LTQ1NTgtOWRlMi04NzI2ZjljZTc3ZDciLCJlbWFpbCI6InNjdWJham9yZ2VuQGdtYWlsLmNvbSJ9.k3Ug6k6h4LGQiXUIGAoAAfEEpOKlCJho0sMQY7Ed-cZu87rcQVrbtnVkDDEzGRWyRIrhhuxEzlN9_NKzs9qHFMvi0smMB--OYOZzLQyB4zGerkpvZ0WnJyxjqizFvtqFbYqG_5JMTES-QY8b5KQWioLKJZx8CBQlFIuM2EhXqRp2DE5iL-aBKvaGU8YS8VPdA1dmicO1TZd0fLKMU7TwyErdecVYfNVoENRdZOVjv_9b-IzlEh-NjaP5hOYJl0XEObVCydYdg-Jpwt_75iWtfD154qg0_k0UtDyJ7hOxwLMLx7Z2hZ_8stM58RtxpYWJYc8nIhmo2RNvEcxK8FMbHw", result.getAccess_token());
        mockServer.verify();
    }

    /**
     * Test of authenticate method, of class TadoInterfaceImpl.
     */
    @Test
    public void testAuthenticateInvalidCredentials() throws Exception
    {
        System.out.println("authenticate - invalid credentials");

        TadoToken result;

        
        // On second authenticate we don't expect a call to the server
        mockServer.reset();
    
        String username = "username@email.com";
        String password = "noonewillguessthis";
        String expectedRequestBody="client_id=tado-web-app&scope=home.user&"+
                                   "grant_type=password&username="+URLEncoder.encode(username, StandardCharsets.UTF_8.toString())+"&password="+password+"&"+
                                   "client_secret=wZaRN7rpjn3FoNyF5IFuxg9uMzYJcvOoQ8QWiIqS3hfk6gLhVlG57j5YNoZL2Rtc";

        String body = "{\"error\": \"invalid_grant\", \"error_description\": \"Bad credentials\"}";        
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://auth.tado.com/oauth/token")))
          .andExpect(method(HttpMethod.POST))
          .andExpect(content().string(expectedRequestBody))
          .andRespond(withStatus(HttpStatus.BAD_REQUEST)        // invalid credentials results in BAD REQUEST
          .contentType(MediaType.APPLICATION_JSON)
          .body(body)
        );        
        tadoInterface.reset();
        result = tadoInterface.authenticate(username, password);
        assertNull(result);
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
     * Test of signOut method, of class TadoInterfaceImpl.
     */
    @Test
    public void reset()
    {
        System.out.println("reset");
        
        Mockito.reset(accountManager);
        tadoInterface.reset();
        Mockito.verify(accountManager, Mockito.times(1)).deleteAllAccounts();
    }    
    /**
     * Test of tadoMe method, of class TadoInterfaceImpl.
     */
    @Test
    public void testTadoMe() throws Exception
    {
        System.out.println("tadoMe");

        meAccount.setTadoMe(null);

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
        assertEquals("username@email.com", result.getUsername());
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
     * Test of tadoMe method, of class TadoInterfaceImpl.
     */

    @Test
    public void testTadoMeRepeated() throws Exception
    {
        System.out.println("tadoMe");
        meAccount.setTadoMe(null);

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
        mockServer.verify();
       
        // Second call is retrieved from cache
        result = tadoInterface.tadoMe();
        assertEquals("12345678-1234-1234-1234-0123456789AB", result.getId());  
        mockServer.verify();
    }

    /**
     * Test of tadoMe method, of class TadoInterfaceImpl.
     */
    @Test
    public void testTadoMeUnauthorized() throws Exception
    {
        System.out.println("tadoMe unauthorized");

        meAccount.setTadoMe(null);

        mockServer.reset();
        String body = "{\"errors\": [{\"code\": \"unauthorized\", \"title\": \"Bad credentials\"}]}";
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/me/")))
          .andExpect(method(HttpMethod.GET))
          .andRespond(withStatus(HttpStatus.UNAUTHORIZED)
          .contentType(MediaType.APPLICATION_JSON)
          .body(body)
        );               
        exceptionRule.expect(TadoException.class);
        exceptionRule.expectMessage("Client error: Tado reported: 401 - Bad credentials");        
        TadoMe result = tadoInterface.tadoMe();
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
     * Test of tadoHome method, of class TadoInterfaceImpl.
     */
    @Test
    public void testTadoHome_Exception() throws Exception
    {
        System.out.println("tadoHome - exception");
        authenticate();

        mockServer.reset();
        String body = "{\"errors\": [{\"code\":\"notFound\",\"title\": \"home 123456 not found\"}]}";
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/homes/123456")))
          .andExpect(method(HttpMethod.GET))
          .andRespond(withStatus(HttpStatus.NOT_FOUND)
          .contentType(MediaType.APPLICATION_JSON)
          .body(body)
        );   
        exceptionRule.expect(TadoException.class);
        exceptionRule.expectMessage("Client error: Tado reported: 404 - home 123456 not found");
            
        TadoHome result = tadoInterface.tadoHome(123456);
    }
    /**
     * Test of tadoHome method, of class TadoInterfaceImpl.
     */
    @Test
    public void testTadoHome_Exception2() throws Exception
    {
        System.out.println("tadoHome - exception");
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
        
        Mockito.when(accountManager.findAccount(123456)).thenReturn(null);
        exceptionRule.expect(TadoException.class);
        exceptionRule.expectMessage("Application error: No account found for home 123456. Unauthorized access.");
            
        TadoHome result = tadoInterface.tadoHome(123456);
    }
    
    
    /**
     * Test of tadoZones method, of class TadoInterfaceImpl.
     */
    @Test
    public void testTadoZones() throws Exception
    {
        System.out.println("tadoZones");

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
     * Test of tadoZones method, of class TadoInterfaceImpl.
     */
    @Test
    public void testTadoZones_Exception() throws Exception
    {
        System.out.println("tadoZones");

        mockServer.reset();
        String body = "{\"errors\":[{\"code\": \"notFound\",\"title\":\"home 123456 not found\"}]}";
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/homes/123456/zones")))
          .andExpect(method(HttpMethod.GET))
          .andRespond(withStatus(HttpStatus.NOT_FOUND)
          .contentType(MediaType.APPLICATION_JSON)
          .body(body)
        );                                   
            
        exceptionRule.expect(TadoException.class);
        exceptionRule.expectMessage("Client error: Tado reported: 404 - home 123456 not found");
        List<TadoZone> result = tadoInterface.tadoZones(123456);
    }
    
    /**
     * Test of tadoState method, of class TadoInterfaceImpl.
     */
    @Test
    public void testTadoPresence() throws Exception
    {
        System.out.println("tadoPresence");

        mockServer.reset();
        String body = new String(Files.readAllBytes((new File("src/test/resources/tadoState.json")).toPath()));
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/homes/123456/state")))
          .andExpect(method(HttpMethod.GET))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(body)
        );                                   
            
        TadoState result = tadoInterface.tadoPresence(123456);
        assertEquals("AWAY", result.getPresence());
        assertEquals(true, result.isPresenceLocked());
        assertEquals(false, result.isShowHomePresenceSwitchButton());

        mockServer.verify();
    }
    
    /**
     * Test of tadoZoneState method, of class TadoInterfaceImpl.
     */
    @Test
    public void testTadoZoneStateHeating() throws Exception
    {
        System.out.println("tadoZoneState - Heating");

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
    /**
     * Test of tadoZoneState method, of class TadoInterfaceImpl.
     */
    @Test
    public void testSetTadoPresence() throws Exception
    {
        System.out.println("setTadoPresence");

        mockServer.reset();
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/homes/123456/presenceLock")))
          .andExpect(method(HttpMethod.PUT))
          .andExpect(content().string("{\"homePresence\":\"AWAY\"}"))
          .andRespond(withStatus(HttpStatus.NO_CONTENT)
        );                                       
        tadoInterface.setTadoPresence(123456, TadoHomePresence.AWAY);
        mockServer.verify();

        mockServer.reset();
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/homes/123456/presenceLock")))
          .andExpect(method(HttpMethod.PUT))
          .andExpect(content().string("{\"homePresence\":\"HOME\"}"))
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andRespond(withStatus(HttpStatus.NO_CONTENT)
        );                                       
        tadoInterface.setTadoPresence(123456, TadoHomePresence.HOME);
        mockServer.verify();
    }
    
    /**
     * Test of tadoDevices method, of class TadoInterfaceImpl.
     */
    @Test
    public void testTadoDevices() throws Exception
    {
        System.out.println("tadoDevices");

        mockServer.reset();
        String body = new String(Files.readAllBytes((new File("src/test/resources/tadoDevices.json")).toPath()));
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v1/home/123456/devices")))
          .andExpect(method(HttpMethod.GET))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(body)
        );                                   
            
        List<TadoDevice> result = tadoInterface.tadoDevices(123456);

        assertEquals(6, result.size());
        assertEquals("IB01", result.get(0).getDeviceType());
        assertEquals("RU3912770304", result.get(1).getSerialNo());
        assertEquals("RU3912770304", result.get(1).getShortSerialNo());
        assertEquals(true, result.get(1).getConnected());
        assertEquals(true, result.get(2).getConnectionState().isValue());
        assertEquals("2021-02-17 15:48:07", dateFormat.format(result.get(2).getConnectionState().getTimestamp()));
        assertEquals(true, result.get(5).getRegisteredToHome());
        mockServer.verify();
    }
    
    /**
     * Test of tadoZones method, of class TadoInterfaceImpl.
     */
    @Test
    public void testTadoOverlay() throws Exception
    {
        System.out.println("tadoOverlay");
    }
    
    /**
     * Test of deleteTadoOverlay method, of class TadoInterfaceImpl.
     */
    @Test
    public void testDeleteTadoOverlay() throws Exception
    {
        System.out.println("deleteTadoOverlay");
        mockServer.reset();
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/homes/123456/presenceLock")))
          .andExpect(method(HttpMethod.PUT))
          .andExpect(content().string("{\"homePresence\":\"AWAY\"}"))
          .andRespond(withStatus(HttpStatus.NO_CONTENT)
        );                                       
        tadoInterface.setTadoPresence(123456, TadoHomePresence.AWAY);
        mockServer.verify();

        mockServer.reset();
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/homes/123456/presenceLock")))
          .andExpect(method(HttpMethod.PUT))
          .andExpect(content().string("{\"homePresence\":\"HOME\"}"))
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andRespond(withStatus(HttpStatus.NO_CONTENT)
        );                                       
        tadoInterface.setTadoPresence(123456, TadoHomePresence.HOME);
        mockServer.verify();
    }

    /**
     * Test of setTadoOverlay method, of class TadoInterfaceImpl.
     */
    @Test
    public void testSetTadoOverlayBoost() throws Exception
    {
        System.out.println("setTadoOverlay - Boost");

        mockServer.reset();
        String expectedRequestBody = new String(Files.readAllBytes((new File("src/test/resources/tadoOverlayBoostReq.json")).toPath()));
        String responseBody = new String(Files.readAllBytes((new File("src/test/resources/tadoOverlayBoostResp.json")).toPath()));
        
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/homes/123456/zones/1/overlay")))
          .andExpect(method(HttpMethod.PUT))
          .andExpect(content().string(expectedRequestBody.replaceAll("\\s", "")))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(responseBody)
        );          
        
        TadoOverlay response=tadoInterface.setTadoOverlay(123456, 1, "HEATING", "ON", 25.0, "TIMER", 1800);
        
        assertEquals("MANUAL", response.getType());
        assertEquals("HEATING", response.getSetting().getType());
        assertEquals("ON", response.getSetting().getPower());
        assertEquals(25.0, response.getSetting().getTemperature().getCelsius(), 0.0001);
        assertEquals(77.0, response.getSetting().getTemperature().getFahrenheit(), 0.0001);
        assertEquals("TIMER", response.getTermination().getType());
        assertEquals("TIMER", response.getTermination().getTypeSkillBasedApp());
        assertEquals(1800, response.getTermination().getDurationInSeconds().intValue());
        assertEquals(1799, response.getTermination().getRemainingTimeInSeconds().intValue());
        assertEquals("2021-02-15 07:52:14", dateFormat.format(response.getTermination().getExpiry()));
        assertEquals("2021-02-15 07:52:14", dateFormat.format(response.getTermination().getProjectedExpiry()));
        mockServer.verify();
   }

    /**
     * Test of setTadoOverlay method, of class TadoInterfaceImpl.
     */
    @Test
    public void testSetTadoOverlayInfinity() throws Exception
    {
        System.out.println("setTadoOverlay - Infinity");

        mockServer.reset();
        String expectedRequestBody = new String(Files.readAllBytes((new File("src/test/resources/tadoOverlayInfReq.json")).toPath()));
        String responseBody = new String(Files.readAllBytes((new File("src/test/resources/tadoOverlayInfResp.json")).toPath()));
        
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/homes/123456/zones/1/overlay")))
          .andExpect(method(HttpMethod.PUT))
          .andExpect(content().string(expectedRequestBody.replaceAll("\\s", "")))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(responseBody)
        );          
        
        TadoOverlay response=tadoInterface.setTadoOverlay(123456, 1, "HEATING", "ON", 17.0, "MANUAL", 0);
        
        assertEquals("MANUAL", response.getType());
        assertEquals("HEATING", response.getSetting().getType());
        assertEquals("ON", response.getSetting().getPower());
        assertEquals(17.0, response.getSetting().getTemperature().getCelsius(), 0.0001);
        assertEquals(62.6   , response.getSetting().getTemperature().getFahrenheit(), 0.0001);
        assertEquals("MANUAL", response.getTermination().getType());
        assertEquals("MANUAL", response.getTermination().getTypeSkillBasedApp());
        assertNull(response.getTermination().getDurationInSeconds());
        assertNull(response.getTermination().getRemainingTimeInSeconds());
        assertNull(response.getTermination().getExpiry());
        assertNull(response.getTermination().getProjectedExpiry());
        mockServer.verify();
    }

    /**
     * Test of setTadoOverlay method, of class TadoInterfaceImpl.
     */
    @Test
    public void testSetTadoOverlayNextTimeBlock() throws Exception
    {
        System.out.println("setTadoOverlay - Next Time Block");

        mockServer.reset();
        String expectedRequestBody = new String(Files.readAllBytes((new File("src/test/resources/tadoOverlayNextReq.json")).toPath()));
        String responseBody = new String(Files.readAllBytes((new File("src/test/resources/tadoOverlayNextResp.json")).toPath()));
        
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/homes/123456/zones/1/overlay")))
          .andExpect(method(HttpMethod.PUT))
          .andExpect(content().string(expectedRequestBody.replaceAll("\\s", "")))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(responseBody)
        );          
        
        TadoOverlay response=tadoInterface.setTadoOverlay(123456, 1, "HEATING", "ON", 16.0, "NEXT_TIME_BLOCK", 1800);
        
        assertEquals("MANUAL", response.getType());
        assertEquals("HEATING", response.getSetting().getType());
        assertEquals("ON", response.getSetting().getPower());
        assertEquals(16.0, response.getSetting().getTemperature().getCelsius(), 0.0001);
        assertEquals(60.8, response.getSetting().getTemperature().getFahrenheit(), 0.0001);
        assertEquals("TIMER", response.getTermination().getType());
        assertEquals("NEXT_TIME_BLOCK", response.getTermination().getTypeSkillBasedApp());
        assertEquals(1800, response.getTermination().getDurationInSeconds().intValue());
        assertEquals(30408, response.getTermination().getRemainingTimeInSeconds().intValue());
        assertEquals("2021-02-15 16:00:00", dateFormat.format(response.getTermination().getExpiry()));
        assertEquals("2021-02-15 16:00:00", dateFormat.format(response.getTermination().getProjectedExpiry()));
        mockServer.verify();
    }    

    /**
     * Test of setTadoOverlay method, of class TadoInterfaceImpl.
     */
    @Test
    public void testSetTadoOverlayHotWaterNextTimeBlock() throws Exception
    {
        System.out.println("setTadoOverlay - Next Time Block - Hot water off");

        mockServer.reset();
        String expectedRequestBody = new String(Files.readAllBytes((new File("src/test/resources/tadoOverlayHwNextReq.json")).toPath()));
        String responseBody = new String(Files.readAllBytes((new File("src/test/resources/tadoOverlayHwNextResp.json")).toPath()));
        
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/homes/123456/zones/2/overlay")))
          .andExpect(method(HttpMethod.PUT))
          .andExpect(content().string(expectedRequestBody.replaceAll("\\s", "")))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(responseBody)
        );          
        
        TadoOverlay response=tadoInterface.setTadoOverlay(123456, 2, "HOT_WATER", "OFF", 0, "NEXT_TIME_BLOCK", 0);
        
        assertEquals("MANUAL", response.getType());
        assertEquals("HOT_WATER", response.getSetting().getType());
        assertEquals("OFF", response.getSetting().getPower());
        assertNull(response.getSetting().getTemperature());
        assertEquals("TIMER", response.getTermination().getType());
        assertEquals("NEXT_TIME_BLOCK", response.getTermination().getTypeSkillBasedApp());
        assertEquals(1800, response.getTermination().getDurationInSeconds().intValue());
        assertEquals(2687, response.getTermination().getRemainingTimeInSeconds().intValue());
        assertEquals("2021-02-18 21:00:00", dateFormat.format(response.getTermination().getExpiry()));
        assertEquals("2021-02-18 21:00:00", dateFormat.format(response.getTermination().getProjectedExpiry()));
        mockServer.verify();
    }

    /**
     * Test of setTadoOverlay method, of class TadoInterfaceImpl.
     */
    @Test
    public void testSetTadoOverlayHotWaterInfiniteTimeBlock() throws Exception
    {
        System.out.println("setTadoOverlay - Inf - Hot water off");

        mockServer.reset();
        String expectedRequestBody = new String(Files.readAllBytes((new File("src/test/resources/tadoOverlayHwInfReq.json")).toPath()));
        String responseBody = new String(Files.readAllBytes((new File("src/test/resources/tadoOverlayHwInfResp.json")).toPath()));
        
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/homes/123456/zones/2/overlay")))
          .andExpect(method(HttpMethod.PUT))
          .andExpect(content().string(expectedRequestBody.replaceAll("\\s", "")))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(responseBody)
        );          
        
        TadoOverlay response=tadoInterface.setTadoOverlay(123456, 2, "HOT_WATER", "ON", 50.0, "MANUAL", 0);
        
        assertEquals("MANUAL", response.getType());
        assertEquals("HOT_WATER", response.getSetting().getType());
        assertEquals("ON", response.getSetting().getPower());
        assertEquals(50.0, response.getSetting().getTemperature().getCelsius(), 0.0001);
        assertEquals(122.0, response.getSetting().getTemperature().getFahrenheit(), 0.0001);
        assertEquals("MANUAL", response.getTermination().getType());
        assertEquals("MANUAL", response.getTermination().getTypeSkillBasedApp());
        assertNull(response.getTermination().getDurationInSeconds());
        assertNull(response.getTermination().getRemainingTimeInSeconds());
        assertNull(response.getTermination().getExpiry());
        assertNull(response.getTermination().getProjectedExpiry());
        mockServer.verify();
    }

    /**
     * Test of setName method, of class TadoInterfaceImpl.
     */
    @Test
    public void testSetName() throws Exception
    {
        System.out.println("setName");

        mockServer.reset();
        String expectedRequestBody = "{\"name\":\"User name\"}";
        String responseBody = new String(Files.readAllBytes((new File("src/test/resources/tadoMe.json")).toPath()));
        
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/users/user@email.com/name")))
          .andExpect(method(HttpMethod.PUT))
          .andExpect(content().string(expectedRequestBody))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(responseBody)
        );          
        
        TadoName name=new TadoName("User name");
        TadoMe response=tadoInterface.setName(name);
        assertEquals("User name", response.getName());
        mockServer.verify();
    }
    /**
     * Test of setName method, of class TadoInterfaceImpl.
     */
    @Test
    public void testSetName_fail() throws Exception
    {
        System.out.println("setName");

    
        mockServer.reset();
        String expectedRequestBody = "{\"name\":\"User name fail\"}";
        String responseBody = new String(Files.readAllBytes((new File("src/test/resources/tadoMe.json")).toPath()));
        
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/users/user@email.com/name")))
          .andExpect(method(HttpMethod.PUT))
          .andExpect(content().string(expectedRequestBody))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(responseBody)
        );  

        exceptionRule.expect(TadoException.class);
        exceptionRule.expectMessage("Application error: Setting name failed");        
        TadoName name=new TadoName("User name fail");
        TadoMe   response=tadoInterface.setName(name);
    }

    /**
     * Test of setLanguage method, of class TadoInterfaceImpl.
     */
    @Test
    public void testSetLanguage() throws Exception
    {
        System.out.println("setName");

        mockServer.reset();
        String expectedRequestBody = "{\"language\":\"en\"}";
        String responseBody = "";
        
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/users/user@email.com/language")))
          .andExpect(method(HttpMethod.PUT))
          .andExpect(content().string(expectedRequestBody))
          .andRespond(withStatus(HttpStatus.NO_CONTENT)
          .contentType(MediaType.APPLICATION_JSON)
          .body(responseBody)
        );          
        
        TadoLanguage lang=new TadoLanguage(TadoLanguage.ENGHLISH);
        tadoInterface.setLanguage(lang);
        mockServer.verify();
    }

    /**
     * Test of setName method, of class TadoInterfaceImpl.
     */
    @Test
    public void testSetEmail() throws Exception
    {
        System.out.println("setName");

        mockServer.reset();
        String expectedRequestBody = "{\"email\":\"user@email.com\",\"currentPassword\":\"password\"}";
        String responseBody = new String(Files.readAllBytes((new File("src/test/resources/tadoMe.json")).toPath()));
        
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/users/user@email.com/email")))
          .andExpect(method(HttpMethod.PUT))
          .andExpect(content().string(expectedRequestBody))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(responseBody)
        );          
        
        TadoEmail email=new TadoEmail("user@email.com", "password");
        TadoMe response=tadoInterface.setEmail(email);
        mockServer.verify();
    }

    /**
     * Test of setPassword method, of class TadoInterfaceImpl.
     */
    @Test
    public void testSetPassword() throws Exception
    {
        System.out.println("setName");

        mockServer.reset();
        String expectedRequestBody = "{\"password\":\"newpassword\",\"currentPassword\":\"oldpassword\"}";
        String responseBody = "";
        
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/users/user@email.com/password")))
          .andExpect(method(HttpMethod.PUT))
          .andExpect(content().string(expectedRequestBody))
          .andRespond(withStatus(HttpStatus.NO_CONTENT)
          .contentType(MediaType.APPLICATION_JSON)
          .body(responseBody)
        );          
        
        tadoInterface.setPassword(new TadoPassword("newpassword","oldpassword"));
        mockServer.verify();
    }
        
    /**
     * Test of tadoTimeTables method, of class TadoInterfaceImpl.
     */
    @Test
    public void testTadoTimeTables() throws Exception
    {
        System.out.println("tadoTimeTables");

        mockServer.reset();
        String body = new String(Files.readAllBytes((new File("src/test/resources/tadoTimeTables.json")).toPath()));
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/homes/123456/zones/0/schedule/timetables")))
          .andExpect(method(HttpMethod.GET))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(body)
        );                                   
            
        List<TadoTimeTable> result = tadoInterface.tadoTimeTables(123456, 0);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(0, result.get(0).getId());
        assertEquals("THREE_DAY", result.get(1).getType());
        mockServer.verify();
    }

    /**
     * Test of tadoTimeTables method, of class TadoInterfaceImpl.
     */
    @Test
    public void testTadoActiveTimeTable() throws Exception
    {
        System.out.println("tadoActiveTimeTable");

        mockServer.reset();
        String body = new String(Files.readAllBytes((new File("src/test/resources/tadoActiveTimeTable.json")).toPath()));
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/homes/123456/zones/0/schedule/activeTimetable")))
          .andExpect(method(HttpMethod.GET))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(body)
        );                                   
            
        TadoTimeTable result = tadoInterface.tadoActiveTimeTable(123456, 0);
        assertNotNull(result);
        assertEquals(2, result.getId());
        assertEquals("SEVEN_DAY", result.getType());
        mockServer.verify();
    }

    /**
     * Test of tadoTimeTables method, of class TadoInterfaceImpl.
     */
    @Test
    public void testTadoSetActiveTimeTable() throws Exception
    {
        System.out.println("tadoSetActiveTimeTable");

        mockServer.reset();
        String body = new String(Files.readAllBytes((new File("src/test/resources/tadoSetActiveTimeTable.json")).toPath()));
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/homes/123456/zones/0/schedule/activeTimetable")))
          .andExpect(method(HttpMethod.PUT))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(body)
        );                                   
            
        TadoTimeTable result = tadoInterface.tadoSetActiveTimeTable(123456, 0, new TadoTimeTable(2));
        assertNotNull(result);
        assertEquals(2, result.getId());
        assertEquals("SEVEN_DAY", result.getType());
        mockServer.verify();
    }

    /**
     * Test of tadoScheduleBlocks method, of class TadoInterfaceImpl.
     */
    @Test
    public void testTadoScheduleBlocks() throws Exception
    {
        System.out.println("tadoScheduleBlocks");

        mockServer.reset();
        String body = new String(Files.readAllBytes((new File("src/test/resources/tadoScheduleBlocks.json")).toPath()));
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/homes/123456/zones/0/schedule/timetables/2/blocks")))
          .andExpect(method(HttpMethod.GET))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(body)
        );                                   
            
        List<TadoScheduleBlock> result = tadoInterface.tadoScheduleBlocks(123456, 0, 2);
        assertNotNull(result);
        assertEquals(27, result.size());
        assertEquals("MONDAY", result.get(0).getDayType());
        assertEquals("07:00", result.get(1).getStart().toString());
        assertEquals("08:30", result.get(1).getEnd().toString());
        assertEquals(true, result.get(0).isGeolocationOverride());
        assertEquals(false, result.get(1).isGeolocationOverride());
        assertEquals("HEATING", result.get(0).getSetting().getType());
        assertEquals("ON", result.get(26).getSetting().getPower());
        assertEquals(17.00, result.get(0).getSetting().getTemperature().getCelsius(), 0.001);
        assertEquals(62.60, result.get(0).getSetting().getTemperature().getFahrenheit(), 0.001);
        mockServer.verify();
    }

    /**
     * Test of tadoScheduleBlocks method, of class TadoInterfaceImpl.
     */
    @Test
    public void testTadoSetScheduleBlocks() throws Exception
    {
        System.out.println("tadoSetScheduleBlocks");

        mockServer.reset();
        String responseBody = new String(Files.readAllBytes((new File("src/test/resources/tadoSetScheduleBlocksResp.json")).toPath()));
        String requestBody = new String(Files.readAllBytes((new File("src/test/resources/tadoSetScheduleBlocksReq.json")).toPath()));
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/homes/123456/zones/0/schedule/timetables/2/blocks/MONDAY")))
          .andExpect(method(HttpMethod.PUT))
          .andExpect(content().string(requestBody.replaceAll("\\s", "")))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(responseBody)
        );                                   
            
        List<TadoScheduleBlock> blocks = new ArrayList<>();
        TadoScheduleBlock block=new TadoScheduleBlock();
        block.setStart(LocalTime.of(00, 00));
        block.setEnd(LocalTime.of(07, 00));
        block.setDayType("MONDAY");
        TadoSetting setting=new TadoSetting();
        setting.setPower("ON");
        setting.setType("HEATING");
        setting.setTemperature(new TadoTemperature(17));
        block.setSetting(setting);
        blocks.add(block);
        block=new TadoScheduleBlock();
        block.setStart(LocalTime.of(07, 00));
        block.setEnd(LocalTime.of(00, 00));
        block.setDayType("MONDAY");
        setting=new TadoSetting();
        setting.setPower("ON");
        setting.setType("HEATING");
        setting.setTemperature(new TadoTemperature(19.50));
        block.setSetting(setting);
        blocks.add(block);        
        
        List<TadoScheduleBlock> result=tadoInterface.tadoSetScheduleBlocks(123456, 0, 2, blocks);
        mockServer.verify();
    }

    /**
     * Test of tadoAirComfort method, of class TadoInterfaceImpl.
     */
    @Test
    public void testTadoAirComfort() throws Exception
    {
        System.out.println("tadoAirComfort");

        mockServer.reset();
        String body = new String(Files.readAllBytes((new File("src/test/resources/tadoAirComfort.json")).toPath()));
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/homes/123456/airComfort")))
          .andExpect(method(HttpMethod.GET))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(body)
        );                                   
            
        TadoAirComfort result = tadoInterface.tadoAirComfort(123456);
        assertNotNull(result);
        assertEquals("FAIR", result.getFreshness().getValue());
        assertEquals("2021-03-09 06:56:03", dateFormat.format(result.getFreshness().getLastOpenWindow()));
        assertEquals(3, result.getComfort().size());
        assertEquals(3, result.getComfort().get(1).getRoomId());
        assertEquals("COLD", result.getComfort().get(1).getTemperatureLevel());
        assertEquals("HUMID", result.getComfort().get(2).getHumidityLevel());
        assertEquals(1.00, result.getComfort().get(2).getCoordinate().getRadial(), 0.001);
        assertEquals(295, result.getComfort().get(2).getCoordinate().getAngular());
        
        mockServer.verify();
    }

    /**
     * Test of tadoWeather method, of class TadoInterfaceImpl.
     */
    @Test
    public void testTadoWeather() throws Exception
    {
        System.out.println("tadoWeather");

        mockServer.reset();
        String body = new String(Files.readAllBytes((new File("src/test/resources/tadoWeather.json")).toPath()));
        mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI("https://my.tado.com/api/v2/homes/123456/weather")))
          .andExpect(method(HttpMethod.GET))
          .andRespond(withStatus(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(body)
        );                                   
            
        TadoWeather result = tadoInterface.tadoWeather(123456);
        assertNotNull(result);
        assertEquals("PERCENTAGE", result.getSolarIntensity().getType());
        assertEquals(21.10, result.getSolarIntensity().getPercentage(), 0.001);
        assertEquals("2021-03-11 16:10:35", dateFormat.format(result.getSolarIntensity().getTimestamp()));

        assertEquals("TEMPERATURE", result.getOutsideTemperature().getType());
        assertEquals(9.57, result.getOutsideTemperature().getCelsius(), 0.001);
        assertEquals(49.23, result.getOutsideTemperature().getFahrenheit(), 0.001);
        assertEquals(0.01, result.getOutsideTemperature().getPrecision().getCelsius(), 0.001);
        assertEquals(0.01, result.getOutsideTemperature().getPrecision().getFahrenheit(), 0.001);
        assertEquals("2021-03-11 16:10:35", dateFormat.format(result.getOutsideTemperature().getTimestamp()));

        assertEquals("WEATHER_STATE", result.getWeatherState().getType());
        assertEquals("CLOUDY_PARTLY", result.getWeatherState().getValue());
        assertEquals("2021-03-11 16:10:35", dateFormat.format(result.getWeatherState().getTimestamp()));

        
        mockServer.verify();
    }
}
