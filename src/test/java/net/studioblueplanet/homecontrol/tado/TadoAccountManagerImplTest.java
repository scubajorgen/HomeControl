/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado;

import java.util.ArrayList;
import java.util.List;
import net.studioblueplanet.homecontrol.Application;
import net.studioblueplanet.homecontrol.tado.entities.TadoAccount;
import net.studioblueplanet.homecontrol.tado.entities.TadoHome;
import net.studioblueplanet.homecontrol.tado.entities.TadoMe;
import net.studioblueplanet.homecontrol.tado.entities.TadoToken;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


/**
 *
 * @author jorgen
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ContextConfiguration
public class TadoAccountManagerImplTest
{
    private static TadoAccount  meAccount;
    private static TadoAccount  friendAccount;
    
    @Autowired
    private TadoAccountManager  instance;
    
    public TadoAccountManagerImplTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
        TadoToken token=new TadoToken();
        token.setAccess_token("accesstoken");
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
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
        instance.deleteAllAccounts();
        instance.addAccount(meAccount);
        instance.addAccount(friendAccount);
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of isAuthorized method, of class TadoAccountManagerImpl.
     */
    @Test
    @WithMockUser("user@email.com")
    public void testIsAuthorized()
    {
        System.out.println("isAuthorized - yes");
        assertEquals(true, instance.isAuthorized());
    }    

    /**
     * Test of isAuthorized method, of class TadoAccountManagerImpl.
     */
    @Test
    @WithMockUser("notAuthorized@email.com")
    public void testIsAuthorizedFail()
    {
        System.out.println("isAuthorized - no");
        assertEquals(false, instance.isAuthorized());
    }    
        
    /**
     * Test of loggedInUsername method, of class TadoAccountManagerImpl.
     */
    @Test
    @WithMockUser("user@email.com")
    public void testLoggedInUsername()
    {
        System.out.println("loggedInUsername - yes");
        assertEquals("user@email.com", instance.loggedInUsername());
    }      
    
    /**
     * Test of loggedInUsername method, of class TadoAccountManagerImpl.
     */
    @Test
    @WithMockUser("notAuthorized@email.com")
    public void testLoggedInUsernameFail()
    {
        System.out.println("loggedInUsername - fail");
        assertNull(instance.loggedInUsername());
    }      

    /**
     * Test of findAccount method, of class TadoAccountManagerImpl.
     */
    @Test
    public void testFindAccount_String()
    {
        System.out.println("findAccount");
        TadoAccount result=instance.findAccount("user@email.com");
        assertNotNull(result);
        assertEquals("user@email.com", result.getUsername());

        result=instance.findAccount("friend@email.com");
        assertNotNull(result);
        assertEquals("friend@email.com", result.getUsername());

        result=instance.findAccount("nonExistent@email.com");
        assertNull(result);
    }

    /**
     * Test of findAccount method, of class TadoAccountManagerImpl.
     */
    @Test
    public void testFindAccount_int()
    {
        System.out.println("findAccount");
        TadoAccount result=instance.findAccount(123456);
        assertNotNull(result);
        assertEquals("user@email.com", result.getUsername());

        result=instance.findAccount(654321);
        assertNotNull(result);
        assertEquals("friend@email.com", result.getUsername());
        
        // Non existent
        result=instance.findAccount(31415);
        assertNull(result);

    }

    /**
     * Test of loggedInAccount method, of class TadoAccountManagerImpl.
     */
    @Test
    @WithMockUser("user@email.com")
    public void testLoggedInAccount()
    {
        System.out.println("loggedInAccount");
        TadoAccount result      = instance.loggedInAccount();
        assertNotNull(result);
        assertEquals("user@email.com", result.getUsername());
    }

    /**
     * Test of loggedInAccount method, of class TadoAccountManagerImpl.
     */
    @Test
    @WithMockUser("notFound@email.com")
    public void testLoggedInAccountNotFound()
    {
        System.out.println("loggedInAccount - not found");
        TadoAccount result      = instance.loggedInAccount();
        assertNull(result);
    }

    /**
     * Test of getAccounts method, of class TadoAccountManagerImpl.
     */
    @Test
    public void testGetAccounts()
    {
        System.out.println("getAccounts");
        List<TadoAccount> result=instance.getAccounts();
        assertEquals(2, result.size());
        assertEquals("user@email.com", result.get(0).getUsername());
        assertEquals("friend@email.com", result.get(1).getUsername());
    }

    /**
     * Test of addAccount method, of class TadoAccountManagerImpl.
     */
    @Test
    public void testAddAccount()
    {
        System.out.println("addAccount");

        TadoAccount account=new TadoAccount("username@email.com", "noonewillguessthis", null);
        account.setUsername("new@email.com");
        assertEquals(2, instance.getAccounts().size());        
        instance.addAccount(account);
        assertEquals(3, instance.getAccounts().size());
        TadoAccount result=instance.findAccount("new@email.com");
        assertNotNull(result);
        assertEquals("new@email.com", result.getUsername());
    }

    /**
     * Test of deleteAccount method, of class TadoAccountManagerImpl.
     */
    @Test
    public void testDeleteAccount()
    {
        System.out.println("deleteAccount");
        assertEquals(2, instance.getAccounts().size());
        instance.deleteAccount("user@email.com");
        assertEquals(1, instance.getAccounts().size());
        assertEquals("friend@email.com", instance.getAccounts().get(0).getUsername());
    }

    /**
     * Test of deleteAllAccounts method, of class TadoAccountManagerImpl.
     */
    @Test
    public void testDeleteAllAccounts()
    {
        System.out.println("deleteAllAccounts");
        assertEquals(2, instance.getAccounts().size());
        instance.deleteAllAccounts();
        assertEquals(0, instance.getAccounts().size());
    }
    
}
