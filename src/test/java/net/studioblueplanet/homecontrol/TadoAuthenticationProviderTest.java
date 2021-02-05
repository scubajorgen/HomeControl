/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol;

import net.studioblueplanet.homecontrol.tado.TadoInterface;
import net.studioblueplanet.homecontrol.tado.entities.TadoToken;

import org.junit.runner.RunWith;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
/**
 *
 * @author jorgen
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TadoAuthenticationProvider.class)
public class TadoAuthenticationProviderTest
{
    @Autowired
    TadoAuthenticationProvider instance;
    
    @MockBean
    private TadoInterface tadoInterface;   
    
    public TadoAuthenticationProviderTest()
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
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of authenticate method, of class TadoAuthenticationProvider.
     */
    @Test
    public void testAuthenticate()
    {
        System.out.println("authenticate");
        Authentication authentication = new UsernamePasswordAuthenticationToken("name", "password");
        TadoToken token=new TadoToken();
        Mockito.when(tadoInterface.authenticate("name", "password")).thenReturn(token);
        Authentication result = instance.authenticate(authentication);
        assertNotNull(result);
        assertEquals("name", result.getPrincipal().toString());
    }

    /**
     * Test of supports method, of class TadoAuthenticationProvider.
     */
    @Test
    public void testSupports()
    {
        System.out.println("supports");
        Class authentication =  UsernamePasswordAuthenticationToken.class;
        boolean expResult = true;
        boolean result = instance.supports(authentication);
        assertEquals(expResult, result);

        authentication =  String.class;
        expResult = false;
        result = instance.supports(authentication);
        assertEquals(expResult, result);
        
        
    }
    
}
