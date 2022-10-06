package se.experis.boxinator.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.HashSet;

//security configuration for endpoints
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("/api/${app.version}")
    private String version;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // Enable CORS -- this is further configured on the controllers
                .cors().and()

                // Sessions will not be used
                .sessionManagement().disable()

                // Disable CSRF -- not necessary when there are sessions
                .csrf().disable()

                // Enable security for http requests
                .authorizeRequests(authorize -> {
                    authorize
                            // Specify paths where public access is allowed
                            .antMatchers("/v3/api-docs", "/v3/api-docs/*", "/v3/api-docs/**").permitAll()
                            .antMatchers("/swagger-ui", "/swagger-ui/*", "/swagger-ui/**").permitAll()
                            // uncomment to test without auth
                            //.antMatchers("/api/v0/*", "/api/v0/**").permitAll()

                            // guests should be able to post to shipments
                            .antMatchers(HttpMethod.POST, version + "/shipments").permitAll()
                            // role authorization for all shipment endpoints
                            .antMatchers(version + "/shipments/*", version + "/shipments/**").hasRole("Customer")
                            .antMatchers(HttpMethod.GET, version + "/shipments").hasRole("Customer")
                            // all users have access to get for all countries
                            .antMatchers(HttpMethod.GET, version + "/settings/countries").permitAll()
                            // role authorization for all country endpoints
                            .antMatchers(version + "/settings/countries",version + "/settings/countries/*", version + "/settings/countries/**").hasRole("Administrator")
                            // role authorization for all account endpoints
                            .antMatchers(version + "/accounts", version + "/accounts/*", version + "/accounts/**").hasRole("Customer")

                            .antMatchers(version + "/status").hasRole("Customer")


                            // All remaining paths require authentication
                            .anyRequest().authenticated();
                })

                // Configure OAuth2 Resource Server (JWT authentication)
                .oauth2ResourceServer(oauth2 -> {
                    // Convert Jwt to AbstractAuthenticationToken
                    JwtAuthenticationConverter authnConverter = new JwtAuthenticationConverter();

                    // Convert Jwt scopes claim to GrantedAuthorities
                    JwtGrantedAuthoritiesConverter scopeConverter = new JwtGrantedAuthoritiesConverter();


                    // Convert Jwt roles claim to GrantedAuthorities
                    JwtGrantedAuthoritiesConverter roleConverter = new JwtGrantedAuthoritiesConverter();
                    roleConverter.setAuthorityPrefix("ROLE_");
                    roleConverter.setAuthoritiesClaimName("roles");

                    // Jwt -> GrantedAuthorities -> AbstractAuthenticationToken
                    authnConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
                        // This will read the 'scope' claim inside the payload
                        Collection<GrantedAuthority> scopes = scopeConverter.convert(jwt);

                        // This will read the 'roles' claim you configured above
                        // jwt["roles"] -> new GrantedAuthority("ROLE_roleName")
                        Collection<GrantedAuthority> roles = roleConverter.convert(jwt);

                        // Merge the above sets
                        HashSet<GrantedAuthority> union = new HashSet<>();
                        union.addAll(scopes);
                        union.addAll(roles);

                        for (var a : union) {
                            logger.warn("JWT Authority: {}", a.getAuthority());
                        }

                        return union;
                    });

                    // Enable JWT authentication and access control from JWT claims
                    oauth2.jwt().jwtAuthenticationConverter(authnConverter);
                });
    }
}