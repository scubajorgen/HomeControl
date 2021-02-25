/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.homecontrol;

import java.io.File;
import net.studioblueplanet.homecontrol.tado.TadoInterface;
import net.studioblueplanet.homecontrol.tado.TadoInterfaceErrorHandling;
import net.studioblueplanet.homecontrol.tado.TadoInterfaceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@SpringBootApplication
public class Application
{
    private static final String FRIENDACCOUNTSJSONFILE="friendAccounts.json";
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);  
    
    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) 
    {
        builder=new RestTemplateBuilder();
        return builder
                .errorHandler(new TadoInterfaceErrorHandling())
                .build();
    } 
    
    @Bean
    public TadoInterface tadoInterface()
    {
        return new TadoInterfaceImpl();
    }
   
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx)
    {
        return args ->
        {
            LOG.info("Application started");
        };
    }
    
    @Bean
    public MapperFactory mapperFactory()
    {
        return new DefaultMapperFactory.Builder().build();
    }
    
    @Bean
    public File friendAccountJsonFile()
    {
        return new File(FRIENDACCOUNTSJSONFILE);
    }
}
