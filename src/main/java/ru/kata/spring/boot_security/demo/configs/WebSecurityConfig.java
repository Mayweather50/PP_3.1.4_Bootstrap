package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import ru.kata.spring.boot_security.demo.services.UserServices;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;
    private final UserServices userServices;

    public WebSecurityConfig(SuccessUserHandler successUserHandler,@Lazy UserServices userServices) {
        this.successUserHandler = successUserHandler;
        this.userServices = userServices;
    }

   @Override
   protected void configure(HttpSecurity http) throws Exception {
       http    .csrf().disable()
               .authorizeRequests()
               .antMatchers("/login").not().fullyAuthenticated()
               .antMatchers("/", "/index").permitAll()
               .antMatchers("/admin/**").hasRole("ADMIN")
               .antMatchers("/user/**").hasAnyRole("USER","ADMIN")
               .antMatchers("/").permitAll()
               .antMatchers("/login").permitAll()
               .antMatchers("/api").permitAll()
               .anyRequest().authenticated()
               .and()
               .formLogin().successHandler(successUserHandler)
               .loginPage("/login")
               .usernameParameter("email")
               .passwordParameter("password")
               .permitAll()
               .and()
               .logout();
   }







    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userServices);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }



    @Bean
    protected BCryptPasswordEncoder passwordEncoder() {
        return  new BCryptPasswordEncoder();
    }
}