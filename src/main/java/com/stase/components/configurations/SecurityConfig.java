package com.stase.components.configurations;

import com.stase.services.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl dbUserDetailsService;
    private final List<String> allowedOrigins;

    public SecurityConfig(
        UserDetailsServiceImpl dbUserDetailsService,
        // origines autorisees pour le CORS, surchargeables via app.cors.allowed-origins
        @Value(
            "${app.cors.allowed-origins:http://localhost:3000,http://localhost:5173,http://localhost:4200}"
        ) String allowedOrigins
    ) {
        this.dbUserDetailsService = dbUserDetailsService;
        this.allowedOrigins = List.of(allowedOrigins.split(","));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt : hachage sale, les mots de passe ne sont jamais stockes en clair
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            // API stateless sans cookies (HTTP Basic) : le CSRF n'a pas de prise ici
            .csrf(AbstractHttpConfigurer::disable)
            // CORS strict : uniquement les origines declarees dans allowedOrigins
            .cors(Customizer.withDefaults())
            .sessionManagement(s ->
                s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .headers(h -> h.frameOptions(f -> f.deny()))
            .authorizeHttpRequests(auth ->
                auth
                    // le CRUD utilisateur est protege : authentification obligatoire
                    .requestMatchers("/api/users/**")
                    .authenticated()
                    .requestMatchers("/api/games")
                    .authenticated()
                    .anyRequest()
                    .permitAll()
            )
            .exceptionHandling(e ->
                e
                    // reponses d'erreur JSON, sans page HTML par defaut de Spring
                    .authenticationEntryPoint((request, response, ex) ->
                        writeJsonError(
                            response,
                            401,
                            "Authentification requise"
                        )
                    )
                    .accessDeniedHandler((request, response, ex) ->
                        writeJsonError(response, 403, "Acces refuse")
                    )
            )
            .httpBasic(Customizer.withDefaults())
            .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // liste explicite d'origines de confiance : jamais de "*" sur une API authentifiee
        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedMethods(
            List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );
        config.setAllowedHeaders(
            List.of("Authorization", "Content-Type", "X-Requested-With")
        );
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // compte admin de secours en memoire ; les vrais utilisateurs viennent de la bdd
        InMemoryUserDetailsManager adminFallback =
            new InMemoryUserDetailsManager(
                org.springframework.security.core.userdetails.User.builder()
                    .username("admin")
                    // appel de la methode @Bean locale : le proxy CGLIB renvoie le singleton
                    .password(passwordEncoder().encode("test123"))
                    .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                    .build()
            );
        return username -> {
            try {
                return dbUserDetailsService.loadUserByUsername(username);
            } catch (UsernameNotFoundException e) {
                return adminFallback.loadUserByUsername(username);
            }
        };
    }

    private void writeJsonError(
        HttpServletResponse response,
        int status,
        String message
    ) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"error\":\"" + message + "\"}");
    }
}
