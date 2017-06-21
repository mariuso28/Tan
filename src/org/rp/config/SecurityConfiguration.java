package org.rp.config;

import org.rp.config.service.CustomAuthenticationFailureHandler;
import org.rp.config.service.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailsService customUserDetailsService;
	@Autowired
	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	@Autowired
	private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
	
	@Bean(name="filterMultipartResolver")
	public CommonsMultipartResolver createMultipartResolver() {
		CommonsMultipartResolver resolver=new CommonsMultipartResolver();
		resolver.setMaxUploadSize(100000000);
		resolver.setDefaultEncoding("utf-8");
		return resolver;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth
		.userDetailsService(customUserDetailsService)

		.and()
		.authenticationProvider(authenticationProvider())

		;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		.antMatchers(
				"/rp/logon/**",
				"/css/**/**",
				"/fonts/**",
				"/images/**",
				"/js/**"
				)
		.permitAll()
		
		.antMatchers(
				"/rp/admin/**"
				)
		
		.access("hasRole('ROLE_ADMIN')")
			
		
		.and().formLogin()
		.loginPage("/rp/logon/signin")
		.usernameParameter("email")
		.passwordParameter("password")
		.successHandler(customAuthenticationSuccessHandler)
		.failureHandler(customAuthenticationFailureHandler)
	//	.failureUrl("/rp/logon/signin?error&message=Authentication%20Error")
		.permitAll()

		.and().logout()
		.permitAll()

	
		.and().sessionManagement()
		.invalidSessionUrl("/rp/logon/signin")
		.and().exceptionHandling()
		.accessDeniedPage("/rp/logon/access_denied")
//		.and().csrf().disable()
		;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/rp/logon/storeImage");
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(customUserDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}

	
}