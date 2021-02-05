/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol;

import net.studioblueplanet.homecontrol.tado.entities.TadoMe;
import net.studioblueplanet.homecontrol.tado.TadoInterface;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.AfterClass;
//import org.junit.jupiter.api.Test;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.MvcResult;

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
    WebApplicationContext webApplicationContext;
  
    @MockBean
    private TadoInterface tadoInterface;
    
    public HomeControllerTest()
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
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of me method, of class HomeController.
     */
    @Test
    public void testMe() throws Exception   
    {
        System.out.println("me");

        TadoMe me=new TadoMe();
        me.setName("test name");

        // Good scenario
        Mockito.when(this.tadoInterface.tadoMe()).thenReturn(me);
        String uri = "/api/me";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        assertEquals("test name", me.getName());

        // Bad flow: no connection with Tado
        Mockito.when(this.tadoInterface.tadoMe()).thenReturn(null);
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }
    
}
