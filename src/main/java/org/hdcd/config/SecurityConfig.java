package org.hdcd.config;

import org.hdcd.common.security.service.CustomUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService())
		.passwordEncoder(passwordEncoder());
	}	
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/js/**", "/imgs/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin()
		.loginPage("/auth/login");
		
		
		http.authorizeRequests()
		.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
		.antMatchers("/").permitAll()
		.antMatchers("/auth/login").permitAll()
		.antMatchers("/user/register", "/user/registerSuccess").permitAll()
		.anyRequest().authenticated();
		
		http.logout()
		.logoutUrl("/auth/logout")
		.logoutSuccessUrl("/");
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}		

	@Bean
	public UserDetailsService customUserDetailsService() {
		return new CustomUserDetailsService();
	}	
	
}
