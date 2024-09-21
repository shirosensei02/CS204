package cs204.project.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/user/signup", "/user/login", "/css/**", "/js/**").permitAll()  
                .requestMatchers("/admin/signup", "/admin/login", "/css/**", "/js/**").permitAll()  
                .requestMatchers("/admin/**").hasRole("ADMIN")  
                .requestMatchers("/user/**").hasRole("USER")    
                .anyRequest().authenticated())
            .formLogin(login -> login
                .loginPage("/user/login")  
                .loginProcessingUrl("/user/login")  
                .defaultSuccessUrl("/user/home", true)  
                .permitAll())
            // Separate form login configuration for admins
            .formLogin(adminLogin -> adminLogin
                .loginPage("/admin/login") 
                .loginProcessingUrl("/admin/login")  
                .defaultSuccessUrl("/admin/home", true)  
                .permitAll())
            .logout(logout -> logout
                .permitAll())
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/403"));

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
}