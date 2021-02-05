/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import net.studioblueplanet.homecontrol.tado.TadoInterface;
import net.studioblueplanet.homecontrol.tado.entities.TadoToken;

/**
 *
 * @author jorgen
 */
@Component
public class TadoAuthenticationProvider implements AuthenticationProvider
{
    @Autowired
    private TadoInterface tado;
    
    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException
    {
        String name     = authentication.getName();
        String password = authentication.getCredentials().toString();

        TadoToken token =tado.authenticate(name, password);
        if (token!=null)
        {
            // use the credentials
            // and authenticate against the third-party system
            return new UsernamePasswordAuthenticationToken(
                    name, password, new ArrayList<>());
        } 
        else
        {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication)
    {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
