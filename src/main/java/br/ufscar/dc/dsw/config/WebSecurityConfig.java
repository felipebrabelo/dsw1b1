package br.ufscar.dc.dsw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import br.ufscar.dc.dsw.security.UsuarioDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	public UserDetailsService userDetailsService() {
		return new UsuarioDetailsServiceImpl();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests((authz) -> authz
						.requestMatchers("/error", "/login/**", "/js/**").permitAll()
						.requestMatchers("/css/**", "/image/**", "/webjars/**").permitAll()
						.requestMatchers("/vagas/salvar", "/vagas/cadastrar", "/vagas/candidaturas/**", "/vagas/excluir/**").hasAnyRole("EMPRESA", "ADMIN")
						.requestMatchers("/vagas/**").permitAll()
						.requestMatchers("/candidaturas", "/candidaturas/cadastrar").hasAnyRole("PROFISSIONAL", "ADMIN")
						.requestMatchers("/candidaturas/**").hasAnyRole("EMPRESA", "ADMIN")
						.requestMatchers("/empresas/vagas").hasAnyRole("EMPRESA")
						.requestMatchers("/empresas/**", "/profissionais/**").hasRole("ADMIN")
						.requestMatchers("/api/**").permitAll()
						.anyRequest().authenticated())
				.formLogin((form) -> form
						.loginPage("/login")
						.permitAll())
				.logout((logout) -> logout
						.logoutSuccessUrl("/").permitAll());

		return http.build();
	}
}