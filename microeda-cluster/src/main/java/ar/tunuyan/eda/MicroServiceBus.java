package ar.tunuyan.eda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

//TODO: improve scanning of services using @Configuration. At the moment is not possible to load a module with @Configuration. The workaround is using xml config.
@Configuration
@EnableAutoConfiguration
@PropertySource({ "classpath:reactor-cluster.properties" })
@ImportResource({ "classpath*:META-INF/spring/app-config.xml" })
@ComponentScan(basePackages = { "ar.tunuyan.eda", "com", "au" })
public class MicroServiceBus {

	public static void main(String[] args) {
		SpringApplication.run(MicroServiceBus.class, args);
	}
}
