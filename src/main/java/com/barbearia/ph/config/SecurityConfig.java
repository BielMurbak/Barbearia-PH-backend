package com.barbearia.ph.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        // Rotas públicas
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/clientes").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/profissionais").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/clientes/**").permitAll()

                        // Serviços e ProfissionalServico: leitura é pública (o cliente precisa
                        // ver preço/serviço sem estar logado pra montar o agendamento).
                        // Escrita (criar/editar/apagar) exige ADMIN.
                        .requestMatchers(HttpMethod.GET, "/api/servicos/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/servicos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/servicos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/servicos/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/profissionais/servicos/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/profissionais/servicos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/profissionais/servicos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/profissionais/servicos/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/agendamentos/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/agendamentos").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/agendamentos/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/agendamentos/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/agendamentos/**").authenticated()

                        // Rotas protegidas
                        .requestMatchers("/api/profissionais/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/clientes/**").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers(HttpMethod.DELETE, "/api/clientes/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess == null) return List.of();
            Collection<String> roles = (Collection<String>) realmAccess.get("roles");
            if (roles == null) return List.of();
            return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toList());
        });
        return converter;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedHeaders(Arrays.asList(
            HttpHeaders.AUTHORIZATION,
            HttpHeaders.CONTENT_TYPE,
            HttpHeaders.ACCEPT
        ));
        config.setAllowedMethods(Arrays.asList(
            HttpMethod.GET.name(),
            HttpMethod.POST.name(),
            HttpMethod.PUT.name(),
            HttpMethod.PATCH.name(),
            HttpMethod.DELETE.name()
        ));
        config.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(-102);
        return bean;
    }
}
