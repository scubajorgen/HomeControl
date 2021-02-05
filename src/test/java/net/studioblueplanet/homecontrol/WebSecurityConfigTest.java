/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol;

import net.studioblueplanet.homecontrol.tado.TadoInterface;
import net.studioblueplanet.homecontrol.tado.entities.TadoToken;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
/**
 *
 * @author jorgen
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class WebSecurityConfigTest
{
    protected MockMvc mvc;
 
    @Autowired
    WebApplicationContext webApplicationContext;
  
    @MockBean
    private TadoInterface tadoInterface;
    
    public WebSecurityConfigTest()
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
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mvc = MockMvcBuilders
          .webAppContextSetup(webApplicationContext)
          .apply(springSecurity())
          .build();
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Integration test WebSecurityConfig.
     */
    @Test
    public void validLogin() throws Exception
    {
        MvcResult mvcResult;

        System.out.println("Integration test - authenticated login");        
        // 200 - ok
        TadoToken token=new TadoToken();
        Mockito.when(this.tadoInterface.authenticate(Mockito.any(), Mockito.any())).thenReturn(token);
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/main").contentType(MediaType.APPLICATION_JSON)).andReturn();
        assertEquals(302, mvcResult.getResponse().getStatus());
    }

    /**
     * Integration test WebSecurityConfig.
     */
    @Test
    public void failedLogin() throws Exception
    {
        MvcResult mvcResult;

        // 302 - forbidden
        System.out.println("Integration test - unauthenticated login attempt");        
        Mockito.when(this.tadoInterface.authenticate(Mockito.any(), Mockito.any())).thenReturn(null);
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/main").contentType(MediaType.APPLICATION_JSON)).andReturn();
        assertEquals(302, mvcResult.getResponse().getStatus());
    }

    /**
     * Integration test WebSecurityConfig.
     */
    @Test
    public void unauthenticatedLogin() throws Exception
    {
        MvcResult mvcResult;

        // 200 - ok: file without authentication
        System.out.println("Integration test - unauthenticated login");        
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/about").contentType(MediaType.APPLICATION_JSON)).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
    }
}
