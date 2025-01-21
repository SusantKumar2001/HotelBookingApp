package com.hmsapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
       http.csrf().disable().cors().disable();
       http.addFilterBefore(jwtFilter, AuthorizationFilter.class);
//       http.authorizeHttpRequests().anyRequest().permitAll();
        http.authorizeHttpRequests().requestMatchers("/api/hms/sign-up","/api/hms/login","api/hms/property/sign-up","/api/v1/property/{searchParam}","/api/hms/updateUser","api/v1/property/updateProperty/**","/api/v1/city/deleteCities"
                ,"/api/v1/images/upload","api/v1/property/upload/propertyPhotos","api/v1/property/getProperty/images"
                ,"/email/send","/api/v1/pdf/generate")
                .permitAll()
                .requestMatchers("api/v1/property/addProperty/**","api/v1/property/createProperty")
                .hasRole("OWNER")
                .requestMatchers("/api/v1/property/deleteProperty")
                .hasAnyRole("ADMIN","OWNER")
                .requestMatchers("/api/hms/blog/sign-up","/api/v1/city/addCity","api/v1/country/addCountry","/api/v1/city/addCities","api/v1/country/addCountries","/api/v1/city/delete/**","/api/v1/city/deleteName/**"
                        ,"/api/v1/country/delete/**","/api/v1/country/deleteName/**","/api/v1/property/deleteProperty","/api/hms/delUser")
                .hasRole("ADMIN")
                .requestMatchers("/api/v1/review/addReviews","/api/v1/review/user/review","/api/v1/review/userProfile"
                        ,"/api/v1/bookings/search/rooms")
                .hasRole("USER")
                .anyRequest().authenticated();
       return http.build();
    }

}
