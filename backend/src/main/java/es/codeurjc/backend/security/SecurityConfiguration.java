package es.codeurjc.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import es.codeurjc.backend.security.jwt.JwtRequestFilter;
import es.codeurjc.backend.security.jwt.UnauthorizedHandlerJwt;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

	@Autowired
	public RepositoryUserDetailsService userDetailService;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	@Order(1)
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

		http.authenticationProvider(authenticationProvider());

		http
				.securityMatcher("/api/**")
				.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));

		http
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(HttpMethod.GET, "/api/posts").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/posts/*").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/posts/*/image").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/threads").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/threads/*").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/users").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/users/*").permitAll()
						// PRIVATE ENDPOINTS
						.requestMatchers(HttpMethod.POST, "/api/posts").hasRole("USER")
						.requestMatchers(HttpMethod.PUT, "/api/posts/*").hasRole("USER")
						.requestMatchers(HttpMethod.PUT, "/api/posts/*/image").hasRole("USER")
						.requestMatchers(HttpMethod.DELETE, "/api/posts/*").hasRole("USER")
						.requestMatchers(HttpMethod.GET, "/api/chart/threads/weekly").hasRole("USER")
						.requestMatchers(HttpMethod.GET, "/api/chart/posts/weekly").hasRole("USER")
						.requestMatchers(HttpMethod.GET, "/api/chart/threads/monthly").hasRole("USER")
						.requestMatchers(HttpMethod.GET, "/api/chart/posts/monthly").hasRole("USER")
						.requestMatchers(HttpMethod.GET, "/api/chart/threads/annually").hasRole("USER")
						.requestMatchers(HttpMethod.GET, "/api/chart/posts/annually").hasRole("USER")
						.requestMatchers(HttpMethod.POST, "/api/threads").hasRole("USER")
						.requestMatchers(HttpMethod.DELETE, "/api/threads/*").hasAnyRole("USER")
						// PUBLIC ENDPOINTS
						.anyRequest().permitAll());

		// Disable Form login Authentication
		http.formLogin(formLogin -> formLogin.disable());

		// Disable CSRF protection (it is difficult to implement in REST APIs)
		http.csrf(csrf -> csrf.disable());

		// Disable Basic Authentication
		http.httpBasic(httpBasic -> httpBasic.disable());

		// Stateless session
		http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Add JWT Token filter
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
		http.authenticationProvider(authenticationProvider());
		http
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/").permitAll()
						.requestMatchers("/css/**").permitAll()
						.requestMatchers("/new/**").permitAll()
						.requestMatchers("/js/**").permitAll()
						.requestMatchers("/register").permitAll()
						.requestMatchers("/register/post").permitAll()
						.requestMatchers("/user/activation/*").permitAll()
						.requestMatchers("/user/profile/*").permitAll()
						.requestMatchers("/image/**").permitAll()
						.requestMatchers("/f/*").permitAll()
						.requestMatchers("/t/*").permitAll()
						.requestMatchers("/error").permitAll()
						.requestMatchers("/thread/user/*").permitAll()
						.requestMatchers("/thread/paginated").permitAll()
						.requestMatchers("/p/like/*").permitAll()
						.requestMatchers("/p/dislike/*").permitAll()
						.requestMatchers("/p/threads/*/posts").permitAll()
						// PRIVATE PAGES
						.requestMatchers("/user/edit-profile/*").hasAnyRole("USER")
						.requestMatchers("/user/update-profile").hasAnyRole("USER")
						.requestMatchers("/user/delete/*").hasAnyRole("USER")
						.requestMatchers("/chart").hasAnyRole("USER")
						.requestMatchers("/chart-rest/threads/weekly").hasAnyRole("USER")
						.requestMatchers("/chart-rest/threads/monthly").hasAnyRole("USER")
						.requestMatchers("/chart-rest/threads/annually").hasAnyRole("USER")
						.requestMatchers("/chart-rest/posts/weekly").hasAnyRole("USER")
						.requestMatchers("/chart-rest/posts/monthly").hasAnyRole("USER")
						.requestMatchers("/chart-rest/posts/annually").hasAnyRole("USER")
						.requestMatchers("/t/*/delete").hasAnyRole("USER")
						.requestMatchers("/t/*/addPost").hasAnyRole("USER")
						.requestMatchers("/t/*/updatePost").hasAnyRole("USER")
						.requestMatchers("/p/delete/*/*").hasAnyRole("USER")
						.requestMatchers("/p/update/*").hasAnyRole("USER")
						.requestMatchers("/p/report/*").hasAnyRole("USER")
						.requestMatchers("/user/users").hasAnyRole("ADMIN")
						.requestMatchers("/user/paginated").hasAnyRole("ADMIN")
						.requestMatchers("/user/search").hasAnyRole("ADMIN")
						.requestMatchers("/reports").hasAnyRole("ADMIN")
						.requestMatchers("/p/reports").hasAnyRole("ADMIN")
						.requestMatchers("/p/validate/*").hasAnyRole("ADMIN")
						.anyRequest().permitAll())
				.formLogin(formLogin -> formLogin
						.loginPage("/login")
						.failureUrl("/loginerror")
						.defaultSuccessUrl("/isActive")
						.permitAll());
		return http.build();
	}
}