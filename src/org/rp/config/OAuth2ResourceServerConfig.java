package org.rp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	@Override
    public void configure(HttpSecurity http) throws Exception {
		http
			.antMatcher("/rp/api/**")
			.authorizeRequests()
			
			.antMatchers(
					"/rp/api/a/**",
					"/rp/api/anon/**"
					)
			.permitAll()
						
			.anyRequest()
			.access("hasRole('ROLE_COMP')");
		;
    }

}