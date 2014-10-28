package ar.tunuyan.eda;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import ar.tunuyan.eda.config.ApplicationConfig;

@Configuration
@PropertySource({ "classpath:reactor.properties" })
@ComponentScan(basePackages = { "ar.com.tunuyan" })
@Import(value = { ApplicationConfig.class})
public class ApplicationTestConfig {

	private final Logger log = LoggerFactory.getLogger(ApplicationTestConfig.class);

	@Inject
	private Environment env;

	/**
	 * Initializes web-async test context.
	 * <p/>
	 * Spring profiles can be configured with a system property
	 * -Dspring.profiles.active=your-active-profile
	 * <p/>
	 */
	@PostConstruct
	public void initApplication() throws IOException {
		log.debug("Looking for Spring profiles...");
		if (env.getActiveProfiles().length == 0) {
			log.debug("No Spring profile configured, running with default configuration");
		} else {
			for (String profile : env.getActiveProfiles()) {
				log.debug("Detected Spring profile : {}", profile);
			}
		}
	}
}