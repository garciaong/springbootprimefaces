package com.springboot.primefaces;

import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.springboot.primefaces.csrf.CustomCsrfFilter;
import com.springboot.primefaces.csrf.CustomFilter;
import com.springboot.primefaces.csrf.NoneSpringFilter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		/*
		 * Used for forwarding to the index page by default. This will trigger
		 * the login.
		 */
		registry.addViewController("/").setViewName("forward:/index.xhtml");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
		super.addViewControllers(registry);
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}

	//This only works for spring mvc
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
//	    registry.addInterceptor(new RequestInterceptor());
	    registry.addInterceptor(new RequestInterceptor()).addPathPatterns("/posted*");
	}
	
//	@Bean
    public Filter customFilter() {
        return new CustomFilter();
    }
	
	@Bean
    public Filter customCsrfFilter() {
        return new CustomCsrfFilter();
    }
	
//	@Bean
	public Filter noneSpringFilter() {
		return new NoneSpringFilter();
	}
}