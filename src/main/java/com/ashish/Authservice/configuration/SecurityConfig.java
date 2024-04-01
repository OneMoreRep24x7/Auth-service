    package com.ashish.Authservice.configuration;


    import com.ashish.Authservice.configuration.Oauth.GoogleOpaqueTokenIntrospector;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationProvider;
    import org.springframework.security.config.Customizer;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    import org.springframework.security.web.authentication.logout.LogoutHandler;
    import org.springframework.web.reactive.function.client.WebClient;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {
        @Autowired
        private JWTAuthenticationFilter jwtAuthenticationFilter;
        @Autowired
        private AuthenticationProvider authenticationProvider;
        @Autowired
        LogoutHandler logoutHandler;
        @Autowired
        private WebClient userInfoClient;



        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception{
            return http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(
                            request->request
                                    .requestMatchers("api/v1/auth/**")
                                    .permitAll()
                                    .anyRequest()
                                    .authenticated()
                    ).authenticationProvider(authenticationProvider)
                    .sessionManagement(
                            session->session
                                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                    .logout(logout ->
                            logout.logoutUrl("/logout")
                                    .addLogoutHandler(logoutHandler)
                                    .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()))
                    )
                    .oauth2ResourceServer(c -> c.opaqueToken(Customizer.withDefaults()))
                    .build();
        }

        @Bean
        public OpaqueTokenIntrospector introspector() {
            return new GoogleOpaqueTokenIntrospector(userInfoClient);
        }



    }
