package com.springboot.jwt.demo.configuration;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {
	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.springboot.jwt.demo")).paths(PathSelectors.any())
				.build().apiInfo(apiDetails())
				.globalRequestParameters(Arrays.asList(new RequestParameterBuilder().in(ParameterType.HEADER)
						.name("Authorization").description("Description of header")
						.query(q -> q.model(m -> m.scalarModel(ScalarType.STRING))).required(true).build()));
	}

	static final String TITLE = "JWT Demo Example";
	static final String DESCRIPTION = "It is a simple project for understanding JWT";
	static final String VERSION = "0.0.1";
	static final String TERMS_OF_SERVICE_URL = "https://www.linkedin.com/in/akash-pal-0007/";
	static final String LICENSE = "@akash";
	static final String LICENSE_URL = "akashpal442@gmail.com";

	static final String CONTACT_NAME = "Akash Pal";
	static final String CONTACT_URL = "https://www.linkedin.com/in/akash-pal-0007/";
	static final String CONTACT_EMAIL = "akashpal442@gmail.com";

	private ApiInfo apiDetails() {
		Contact contact = new Contact(CONTACT_NAME, CONTACT_URL, CONTACT_EMAIL);
		return new ApiInfoBuilder().title(TITLE).description(DESCRIPTION).termsOfServiceUrl(TERMS_OF_SERVICE_URL)
				.contact(contact).license(LICENSE).licenseUrl(LICENSE_URL).version(VERSION).build();
	}

	/*
	 * If your project have spring security then add below in your security
	 * configuration
	 */
//	private static final String[] AUTH_WHITELIST = {
//	        "/swagger-resources/**",
//	        "/swagger-ui/",
//	        "/v2/api-docs",
//	        "ui"
//	};
	/* Add in config method */
//	.antMatchers(AUTH_WHITELIST).permitAll()

}
