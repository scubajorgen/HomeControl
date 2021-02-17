/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado.entities;

import java.util.Date;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
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
public class TadoTokenTest
{
    
    public TadoTokenTest()
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
     * Test of getAccessTokenExpires method, of class TadoToken.
     */
    @Test
    public void testGetAccessTokenExpires()
    {
        System.out.println("getAccessTokenExpires");
        TadoToken instance = new TadoToken();
        instance.setAccess_token("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI2MjljYjBlNS00MTg0LTQ3YjktYjFhMS1jN2Y4MjZkOTlkNjkiLCJ0YWRvX2hvbWVzIjpbeyJpZCI6NjMxMzE0fV0sImlzcyI6InRhZG8iLCJsb2NhbGUiOiJlbiIsImF1ZCI6InBhcnRuZXIiLCJuYmYiOjE2MTM1Njk0MjAsInRhZG9fc2NvcGUiOlsiaG9tZS51c2VyIl0sInRhZG9fdXNlcm5hbWUiOiJzY3ViYWpvcmdlbkBnbWFpbC5jb20iLCJuYW1lIjoiSm9yZ2VuIHZhbiBkZXIgVmVsZGUiLCJleHAiOjE2MTM1NzAwMjAsImlhdCI6MTYxMzU2OTQyMCwidGFkb19jbGllbnRfaWQiOiJ0YWRvLXdlYi1hcHAiLCJqdGkiOiI1ODExYWUzMi03ZDAyLTQ2NzQtYjkwMC04NDEzYmVkZjIxZWYiLCJlbWFpbCI6InNjdWJham9yZ2VuQGdtYWlsLmNvbSJ9.CNC91kQWXCSzjiyQmqXUOELTm03YySWbe10V72g3_N_3VefQ3tC9uzwfRDTNST8-co_r81NB1hA1lLFzexgNUUyNvYMb4CEhgSuEWIQZ4zyyvDqT8h010CW70yPdZhOOMN-e25rS9dllUCfpTAvuNNnawgvoVy1Nc6fv7b-R_T2uAURBTI2YzG4t3uoxWkcV_3_w7SaKtOuVEdLpqr5ZRvkv4vzkrUwiZDNRQ-n6L1BbTrplVHLyNWMEn59W2ddX_DercDxSISigcw9VUHnTCqNdrZhKIrYG1gkMdbueX8-LkwjKe-2dBRsibEdUeo-OF2zWe2oiPlnUwyXbIXaVLg");
        String expResult = "2021-02-17 13:53:40";
        Date result = instance.getAccessTokenExpires();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        assertEquals(expResult, format.format(result));
    }
    
}
