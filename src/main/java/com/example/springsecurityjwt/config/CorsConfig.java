package com.example.springsecurityjwt.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * Configuration class for setting up Cross-Origin Resource Sharing (CORS) in the application.
 * <p>
 * This configuration is crucial for enabling the front-end application (such as an Angular app running on http://localhost:4200)
 * to communicate with the Spring Boot back-end securely and without encountering CORS policy issues.
 *
 * <p>Key Features:</p>
 * <ul>
 *   <li>Allows cross-origin requests from the specified origin (http://localhost:4200).</li>
 *   <li>Specifies allowed HTTP headers: Authorization, Content-Type, Accept, Origin, and Access-Control-Allow-Origin.</li>
 *   <li>Permits specific HTTP methods: GET, PUT, DELETE, POST.</li>
 *   <li>Enables credentials (cookies, authorization headers, or TLS client certificates) to be included in cross-origin requests.</li>
 *   <li>Sets the maximum age (3600 seconds) for which the results of a preflight request can be cached.</li>
 * </ul>
 * <p>
 * This setup ensures seamless and secure integration between the front-end and back-end during development and helps prevent CORS-related issues.
 */
@Configuration
public class CorsConfig {

    /**
     * Cross-Origin Resource Sharing (CORS) is a security feature implemented in web browsers
     * to prevent malicious websites from making unauthorized requests to another domain.
     * A CORS attack leverages this mechanism to exploit any weaknesses in the server's CORS configuration,
     * potentially allowing unauthorized access to resources.
     *
     * */

    /**
     * Creates and registers a CorsFilter bean to handle CORS requests based on the defined configuration.
     *
     * @return a FilterRegistrationBean containing the CorsFilter with the specified CORS configuration
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfig = new CorsConfiguration();

        // Allow credentials such as cookies and authorization headers
        corsConfig.setAllowCredentials(true);

        // Allow requests from the specified origin (Angular app running on http://localhost:4200)
        corsConfig.addAllowedOrigin("http://localhost:4200");//avoid to make it * from production (and try to make it specific)

        // Specify allowed headers
        corsConfig.setAllowedHeaders(List.of(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE, HttpHeaders.ACCEPT, HttpHeaders.ORIGIN, HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));

        // Specify allowed HTTP methods
        corsConfig.setAllowedMethods(List.of(HttpMethod.GET.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name(), HttpMethod.POST.name()));

        // Expose specific headers to the client
        corsConfig.setExposedHeaders(List.of(HttpHeaders.AUTHORIZATION));

        // Set the maximum age for caching preflight request results
        corsConfig.setMaxAge(3600L);

        // Register the CORS configuration for all endpoints
        source.registerCorsConfiguration("/**", corsConfig);

        // Create and configure the FilterRegistrationBean for the CorsFilter
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        // Set the order of the filter to ensure it runs at the appropriate time in the filter chain
        bean.setOrder(-102);

        return bean;
    }
}
