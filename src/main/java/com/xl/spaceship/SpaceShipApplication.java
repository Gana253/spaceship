package com.xl.spaceship;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * SpaceShip Application
 *
 */

@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
public class SpaceShipApplication 
{
    public static void main( String[] args )
    {
    	 SpringApplication.run(SpaceShipApplication.class, args);
    }
}
