package ar.tunuyan.eda.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import com.wordnik.swagger.model.ApiInfo;

@Configuration
@EnableSwagger
public class SwaggerConfiguration {
	public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";

	@Inject
	private Environment env;

	/**
	 * Swagger Spring MVC configuration
	 */
	@Bean
	public SwaggerSpringMvcPlugin swaggerSpringMvcPlugin(SpringSwaggerConfig springSwaggerConfig) {
		return new SwaggerSpringMvcPlugin(springSwaggerConfig).apiInfo(apiInfo()).genericModelSubstitutes(ResponseEntity.class)
				.includePatterns(DEFAULT_INCLUDE_PATTERN);
	}

	/**
	 * API Info as it appears on the swagger-ui page
	 */
	private ApiInfo apiInfo() {
		return new ApiInfo(env.getProperty("title"), env.getProperty("description"), env.getProperty("termsOfServiceUrl"),
				env.getProperty("contact"), env.getProperty("license"), env.getProperty("licenseUrl"));
	}
}
