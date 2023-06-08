package com.crazypug.datalineage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;


@SpringBootApplication
public class LineageApplication {


  static   Logger log = LoggerFactory.getLogger(LineageApplication.class);
    public static ConfigurableApplicationContext ac;


    public static void main(String[] args) throws UnknownHostException {
        ac = SpringApplication.run(LineageApplication.class, args);
        Environment env = ac.getEnvironment();
        String host = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}/doc.html\n\t" +
                        "External: \thttp://{}:{}/doc.html\n\t" +
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                host, port);
    }
}
