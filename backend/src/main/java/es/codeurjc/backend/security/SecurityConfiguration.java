package es.codeurjc.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

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
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authenticationProvider(authenticationProvider());
		http
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/").permitAll()
						.requestMatchers("/css/**").permitAll()
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