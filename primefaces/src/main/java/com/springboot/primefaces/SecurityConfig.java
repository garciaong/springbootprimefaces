package com.springboot.primefaces;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password(bCryptPasswordEncoder.encode("123")).roles("USER");
	}
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// Build the request matcher for CSFR
//	    RequestMatcher csrfRequestMatcher = new RequestMatcher() {
//
//	      private RegexRequestMatcher requestMatcher =
//	          new RegexRequestMatcher("/posted.xhtml", null);
//
//	      @Override
//	      public boolean matches(HttpServletRequest request) {
//	          // Enable the CSRF
//	          if(requestMatcher.matches(request))
//	              return true;
//	          // You can add here any other rule on the request object, returning 
//	          // true if the CSRF must be enabled, false otherwise
//	          // No CSRF for other requests
//	          return false;
//	      }
//	    };
	    //Alternative request matcher
		RequestMatcher csrfRequestMatcher = new RequestMatcher() {
			@Override
			public boolean matches(HttpServletRequest request) {
				return request.getServletPath().contains("/posted.xhtml");
			}
		};
	    
		//Default enabled
		//httpSecurity.csrf().disable();
	    //Enable csrf only on some request matches
	    httpSecurity.csrf().requireCsrfProtectionMatcher(csrfRequestMatcher);
		httpSecurity.authorizeRequests().anyRequest().authenticated().and().formLogin().defaultSuccessUrl("/post.xhtml");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
	}
}