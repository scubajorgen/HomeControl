/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol;

import java.util.ArrayList;


import net.studioblueplanet.homecontrol.tado.TadoInterface;
import net.studioblueplanet.homecontrol.service.AccountService;
import net.studioblueplanet.homecontrol.service.HomeService;
import net.studioblueplanet.homecontrol.service.entities.Account;
import net.studioblueplanet.homecontrol.service.entities.Home;
import net.studioblueplanet.homecontrol.service.entities.HomeId;
import net.studioblueplanet.homecontrol.service.entities.HomeState;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;


import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.mockito.Mockito;

import org.springframework.boot.test.mock.mockito.MockBean;

/**
 *
 * @author jorgen
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class HomeControllerTest
{
    protected MockMvc mvc;
 
    @Autowired
    WebApplicationContext   webApplicationContext;
  
    @MockBean
    private TadoInterface   tadoInterface;
    
    @MockBean
    private AccountService  accountService;
    
    @MockBean
    private HomeService     homeService;

    private static Account  account;
    
    public HomeControllerTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
        int homeId=12345;
        account=new Account();
        HomeId home=new HomeId();
        home.setId(homeId);
        ArrayList<HomeId> homes=new ArrayList<>();
        homes.add(home);
        account.setOwnHomes(homes);
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of me method, of class HomeController.
     */ 
    @Test
    public void testAccount() throws Exception   
    {
        System.out.println("account");
        int homeId=12345;
        Account account=new Account();
        account.setId("it's me");
        account.setName("John");
        account.setUsername("Doe");
        HomeId home=new HomeId();
        home.setId(homeId);
        ArrayList<HomeId> homes=new ArrayList<>();
        homes.add(home);
        account.setOwnHomes(homes);

        Mockito.when(this.accountService.getAccount()).thenReturn(account);
        
        mvc.perform(MockMvcRequestBuilders.get("/api/account").accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value("it's me"))
        .andExpect(jsonPath("$.name").value("John"))
        .andExpect(jsonPath("$.username").value("Doe"))
        .andDo(print())
        .andReturn();        

    }
    
    
    /**
     * Test of state method, of class HomeController.
     */ 
    @Test
    public void testState() throws Exception   
    {
        System.out.println("state");

        int homeId=12345;
        Account account=new Account();
        HomeId home=new HomeId();
        home.setId(homeId);
        ArrayList<HomeId> homes=new ArrayList<>();
        homes.add(home);
        account.setOwnHomes(homes);
        Mockito.when(this.accountService.getAccount()).thenReturn(account);

        HomeState state=new HomeState();
        state.setPresence("AWAY");
        state.setPresenceLocked(false);
        state.setShowHomePresenceSwitchButton(true);
        Mockito.when(this.homeService.getHomeState(homeId)).thenReturn(state);
        
        mvc.perform(MockMvcRequestBuilders.get("/api/state/"+homeId).accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.presence").value("AWAY"))
        .andExpect(jsonPath("$.presenceLocked").value(false))
        .andExpect(jsonPath("$.showHomePresenceSwitchButton").value(true))
        .andDo(print())
        .andReturn();        
    }

    /**
     * Test of home method, of class HomeController.
     */ 
    @Test
    public void testHome() throws Exception   
    {
        System.out.println("home");

        int homeId=12345;
        Account account=new Account();
        HomeId home=new HomeId();
        home.setId(homeId);
        ArrayList<HomeId> homes=new ArrayList<>();
        homes.add(home);
        account.setOwnHomes(homes);
        Mockito.when(this.accountService.getAccount()).thenReturn(account);

        Home theHome=new Home();
        theHome.setId(homeId);
        theHome.setName("HQ");
        Mockito.when(this.homeService.getHome(homeId)).thenReturn(theHome);
        
         mvc.perform(MockMvcRequestBuilders.get("/api/home/"+homeId)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(homeId))
        .andExpect(jsonPath("$.name").value("HQ"))
        .andDo(print())
        .andReturn();        
    }   
    
    /**
     * Test of home method, of class HomeController.
     */ 
    @Test
    public void testPresence() throws Exception   
    {
        System.out.println("presence");

        Mockito.when(this.accountService.getAccount()).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.put("/api/home/12345/presence")
        .content("{\"presence\": \"AWAY\"}")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isPreconditionFailed())
        .andDo(print())
        .andReturn();        

        Mockito.when(this.accountService.getAccount()).thenReturn(account);
        mvc.perform(MockMvcRequestBuilders.put("/api/home/12345/presence")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"presence\": \"AWAY\"}")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();        

    }   
    
    
}
