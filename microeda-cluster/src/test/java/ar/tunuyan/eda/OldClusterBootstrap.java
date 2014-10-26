package ar.tunuyan.eda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ar.tunuyan.eda.config.ClusterConfig;

//TODO: improve scanning of services using @Configuration. At the moment is not possible to load a module with @Configuration. The workaround is using xml config.
public class OldClusterBootstrap {

	private static final Logger LOGGER = LoggerFactory.getLogger(OldClusterBootstrap.class);

//	@SuppressWarnings({ "unused", "resource" })
//	public static void main(String[] args) {
//		LOGGER.debug("Starting Microservice node....");
//
//		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(ClusterConfig.class);
//
//		LOGGER.debug("Microservice node started successfully.");
//	}
}
