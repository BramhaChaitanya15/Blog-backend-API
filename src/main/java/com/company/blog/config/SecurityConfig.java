package com.company.blog.config;

import com.company.blog.security.JwtAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.company.blog.security.CustomUserDetailService;
import com.company.blog.security.JwtAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	public static final String[] PUBLIC_URLS = { "/api/v1/auth/**", "/api/v1/auth/**", "/v3/api-docs/**",
			"/swagger-ui/**", "/swagger-ui.html" };

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Autowired
	private JwtAuthenticationEntryPoint authenticationEntryPoint;

	@Autowired
	private CustomUserDetailService customUserDetailService;

	// Configures HTTP security settings for the application
	@Bean
	SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		// Configuring HTTP security settings
		httpSecurity
				// disable CSRF for stateless REST API
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth.requestMatchers(PUBLIC_URLS).permitAll()
						.requestMatchers(HttpMethod.GET).permitAll().anyRequest().authenticated())
				.exceptionHandling(exception -> exception.authenticationEntryPoint(this.authenticationEntryPoint))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return httpSecurity.build();

	}

	// Bean to configure the authentication provider, using
	// DaoAuthenticationProvider
	@Bean
	DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(
				this.customUserDetailService);
		daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());
		return daoAuthenticationProvider;
	}

	// Bean for configuring the AuthenticationManager, used for authentication
	@Bean
	AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	// Bean to provide BCryptPasswordEncoder to encrypt user passwords
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	FilterRegistrationBean<CorsFilter> corsFilter() {

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfig = new CorsConfiguration();

		corsConfig.setAllowCredentials(true);
		corsConfig.addAllowedOriginPattern("*");
		corsConfig.addAllowedHeader("Authorization");
		corsConfig.addAllowedHeader("Content-Type");
		corsConfig.addAllowedHeader("Accept");
		corsConfig.addAllowedMethod("POST");
		corsConfig.addAllowedMethod("GET");
		corsConfig.addAllowedMethod("DELETE");
		corsConfig.addAllowedMethod("PUT");
		corsConfig.addAllowedMethod("OPTIONS");
		corsConfig.setMaxAge(3600L);

		source.registerCorsConfiguration("/**", corsConfig);

		FilterRegistrationBean<CorsFilter> frBean = new FilterRegistrationBean<>(new CorsFilter(source));
		
		frBean.setOrder(-110);

		return frBean;
	}

}
