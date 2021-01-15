package com.config;

import com.services.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private SimpleAuthenticationSuccessHandler authenticationSuccessHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(this.userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/file/**").hasAnyAuthority("ADMIN", "HR_MANAGER")
                .antMatchers("/download/**").hasAnyAuthority("ADMIN", "HR_MANAGER", "DATA_SPECIALIST")
                .antMatchers("/learn_example/**").hasAnyAuthority("ADMIN", "DATA_SPECIALIST")
                .antMatchers("/settings/**").hasAnyAuthority("ADMIN")
                .antMatchers("/speciality/all").hasAnyAuthority("ADMIN", "DATA_SPECIALIST", "HR_MANAGER")
                .antMatchers("/speciality/update").hasAnyAuthority("ADMIN", "DATA_SPECIALIST", "HR_MANAGER")
                .antMatchers("/speciality/list").hasAnyAuthority("ADMIN", "DATA_SPECIALIST", "HR_MANAGER")
                .antMatchers("/speciality/update_speciality").hasAnyAuthority("ADMIN")
                .antMatchers("/speciality/all_for_editing").hasAnyAuthority("ADMIN")
                .antMatchers("/speciality/remove").hasAnyAuthority("ADMIN")
                .antMatchers("/speciality/add").hasAnyAuthority("ADMIN")
                .antMatchers("/speciality/getall").hasAnyAuthority("ADMIN")
                .antMatchers("/users/hr_info**").hasAnyAuthority("ADMIN", "DATA_SPECIALIST", "HR_MANAGER")
                .antMatchers("/posts/all").hasAnyAuthority("ADMIN", "DATA_SPECIALIST", "HR_MANAGER")
                .antMatchers("/roles/all").hasAnyAuthority("ADMIN", "DATA_SPECIALIST", "HR_MANAGER")
                .antMatchers("/profile**").hasAnyAuthority("ADMIN", "DATA_SPECIALIST", "HR_MANAGER")
                .antMatchers("/users/all").hasAnyAuthority("ADMIN")
                .antMatchers("/users/add").hasAnyAuthority("ADMIN")
                .antMatchers("/users/loadAvatar**").hasAnyAuthority("ADMIN", "DATA_SPECIALIST", "HR_MANAGER")
                .antMatchers("/users/update").hasAnyAuthority("ADMIN")
                .antMatchers("/users/remove").hasAnyAuthority("ADMIN")
                .antMatchers("/tasks/**").hasAnyAuthority("ADMIN")
                .antMatchers("/task_types/**").hasAnyAuthority("ADMIN")
                .antMatchers("/fonts/**").permitAll()
                .antMatchers("/avatar/**").permitAll()
                .and().formLogin()
                .loginPage("/login")
                .successHandler(this.authenticationSuccessHandler);

        http.headers().frameOptions().disable();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
