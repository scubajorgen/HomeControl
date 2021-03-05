/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol.tado;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

/**
 *
 * @author jorgen
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithTadoSecurityContextFactory.class)
public @interface WithTadoUser
{
	String username() default "tadoUser";

	String name() default "Tado the User";    
}
