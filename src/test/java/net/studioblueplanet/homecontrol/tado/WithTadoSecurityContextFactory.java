/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.GrantedAuthority;
/**
 *
 * @author jorgen
 */
public class WithTadoSecurityContextFactory implements WithSecurityContextFactory<WithTadoUser> 
{
    @Value("${tadoUsername}")
    private String username;
    
    @Value("${tadoPassword}")
    private String password;
    
    @Override
    public SecurityContext createSecurityContext(WithTadoUser customUser) 
    {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User principal =new User(username, password, new ArrayList<GrantedAuthority>());
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
