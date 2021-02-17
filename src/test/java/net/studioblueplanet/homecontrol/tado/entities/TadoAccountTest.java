/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jorgen
 */
public class TadoAccountTest
{
    private TadoAccount instance;
    private TadoToken   tadoToken;
    
    public TadoAccountTest()
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
        tadoToken       =new TadoToken();
        tadoToken.setExpires_in(2);
        tadoToken.setAccess_token("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI2MjljYjBlNS00MTg0LTQ3YjktYjFhMS1jN2Y4MjZkOTlkNjkiLCJ0YWRvX2hvbWVzIjpbeyJpZCI6NjMxMzE0fV0sImlzcyI6InRhZG8iLCJsb2NhbGUiOiJlbiIsImF1ZCI6InBhcnRuZXIiLCJuYmYiOjE2MTM1Njk0MjAsInRhZG9fc2NvcGUiOlsiaG9tZS51c2VyIl0sInRhZG9fdXNlcm5hbWUiOiJzY3ViYWpvcmdlbkBnbWFpbC5jb20iLCJuYW1lIjoiSm9yZ2VuIHZhbiBkZXIgVmVsZGUiLCJleHAiOjE2MTM1NzAwMjAsImlhdCI6MTYxMzU2OTQyMCwidGFkb19jbGllbnRfaWQiOiJ0YWRvLXdlYi1hcHAiLCJqdGkiOiI1ODExYWUzMi03ZDAyLTQ2NzQtYjkwMC04NDEzYmVkZjIxZWYiLCJlbWFpbCI6InNjdWJham9yZ2VuQGdtYWlsLmNvbSJ9.CNC91kQWXCSzjiyQmqXUOELTm03YySWbe10V72g3_N_3VefQ3tC9uzwfRDTNST8-co_r81NB1hA1lLFzexgNUUyNvYMb4CEhgSuEWIQZ4zyyvDqT8h010CW70yPdZhOOMN-e25rS9dllUCfpTAvuNNnawgvoVy1Nc6fv7b-R_T2uAURBTI2YzG4t3uoxWkcV_3_w7SaKtOuVEdLpqr5ZRvkv4vzkrUwiZDNRQ-n6L1BbTrplVHLyNWMEn59W2ddX_DercDxSISigcw9VUHnTCqNdrZhKIrYG1gkMdbueX8-LkwjKe-2dBRsibEdUeo-OF2zWe2oiPlnUwyXbIXaVLg");
        instance        = new TadoAccount("name", "password", tadoToken);
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of getLastRefresh method, of class TadoAccount.
     */
    @Test
    public void testGetLastRefresh()
    {
        System.out.println("getLastRefresh");
        Date expResult = new Date();
        Date result = instance.getLastRefresh();
        assertEquals(expResult.getTime(), result.getTime());
    }

    /**
     * Test of getLastRefreshFormatted method, of class TadoAccount.
     */
    @Test
    public void testGetLastRefreshFormatted()
    {
        System.out.println("getLastRefreshFormatted");
        String expResult =(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")).format(new Date());
        String result = instance.getLastRefreshFormatted();
        assertEquals(expResult, result);
    }

    /**
     * Test of expiryDate method, of class TadoAccount.
     */
    @Test
    public void testExpiryDate()
    {
        System.out.println("expiryDate");
        String expResult        = "2021-02-17 13:53:40";
        Date result             = instance.expiryDate();
        SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        assertEquals(expResult, format.format(result));
    }

    /**
     * Test of expiresIn method, of class TadoAccount.
     */
    @Test
    public void testExpiresIn()
    {
        System.out.println("expiresIn");
        int expResult = 2;
        int result = instance.expiresIn();
        assertEquals(expResult, result);
        try
        {
            Thread.sleep(1000);
        }
        catch (Exception e)
        {
            
        }
        expResult = 1;
        result = instance.expiresIn();
        assertEquals(expResult, result);        
        try
        {
            Thread.sleep(2000);
        }
        catch (Exception e)
        {
            
        }
        expResult = 0;
        result = instance.expiresIn();
        assertEquals(expResult, result); 
    }

    /**
     * Test of needsRefresh method, of class TadoAccount.
     */
    @Test
    public void testNeedsRefresh()
    {
        System.out.println("needsRefresh");
        boolean result = instance.needsRefresh();
        assertEquals(true, result);
        
        instance.setToken(tadoToken);
        tadoToken.setExpires_in(599);
        result = instance.needsRefresh();
        assertEquals(false, result);
        
        instance.setToken(tadoToken);
        tadoToken.setExpires_in(60);
        result = instance.needsRefresh();
        assertEquals(false, result);
        
        instance.setToken(tadoToken);
        tadoToken.setExpires_in(59);
        result = instance.needsRefresh();
        assertEquals(true, result);
    }
    
}
