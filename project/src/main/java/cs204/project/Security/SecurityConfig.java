package cs204.project.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


import cs204.project.Model.Admin.AdminService;
import cs204.project.Model.User.MyAppUserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private final MyAppUserService appUserService;

    @Autowired
    private final AdminService adminService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // User-specific security filter chain
    @Bean
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/user/**")  // Apply this filter chain only for /user paths
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/user/signup", "/user/login", "/css/**", "/js/**").permitAll()
                .anyRequest().hasRole("USER"))
            .formLogin(login -> login
                .loginPage("/user/login")
                .loginProcessingUrl("/user/login")
                .defaultSuccessUrl("/user/home", true)
                .failureUrl("/user/login?error=true")
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/user/logout")
                .logoutSuccessUrl("/user/login?logout=true")
                .permitAll());
        
        return http.build();
    }

    // Admin-specific security filter chain
    @Bean
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/admin/**")  // Apply this filter chain only for /admin paths
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/login", "/css/**", "/js/**").permitAll()
                .anyRequest().hasRole("ADMIN"))
            .formLogin(login -> login
                .loginPage("/admin/login")
                .loginProcessingUrl("/admin/login")
                .defaultSuccessUrl("/admin/home", true)
                .failureUrl("/admin/login?error=true")
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/admin/logout")
                .logoutSuccessUrl("/admin/login?logout=true")
                .permitAll());
        
        return http.build();
    }

    @Bean
public AuthenticationProvider userAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(appUserService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
}

@Bean
public AuthenticationProvider adminAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(adminService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
}

@Bean
public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    authManagerBuilder.authenticationProvider(userAuthenticationProvider());
    authManagerBuilder.authenticationProvider(adminAuthenticationProvider());
    return authManagerBuilder.build();
}
}