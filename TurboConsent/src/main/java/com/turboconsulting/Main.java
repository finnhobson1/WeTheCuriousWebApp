package com.turboconsulting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;

//************************************************
//USE THIS CODE TO JAR DEPLOYMENT (RUNNING IN IDE)
//************************************************
/*@SpringBootApplication
@EnableConfigurationProperties
public class Main {

    public static void main(String args[])  {

        SpringApplication.run(Main.class, args);

    }


}*/

//********************************************************************
//USE THIS CODE FOR WAR DEPLOYMENT (RUNNING ON PRODUCTION ENVIRONMENT)
//********************************************************************
@SpringBootApplication
@EnableConfigurationProperties
public class Main extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Main.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
