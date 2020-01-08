package pl.kowalczyk.nowwork;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.kowalczyk.nowwork.model.User;
import pl.kowalczyk.nowwork.repository.UserRepo;
import pl.kowalczyk.nowwork.service.UserDetailsServiceImpl;


@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private UserDetailsServiceImpl userDetailsService;
    private UserRepo userRepo;

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, UserRepo userRepo) {
        this.userDetailsService = userDetailsService;
        this.userRepo = userRepo;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/chat")
                .authenticated()
                .and()
                .formLogin().permitAll();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @EventListener(ApplicationReadyEvent.class)
    public void saveUser() {
        User user = new User("test1", "test1@asf.com", passwordEncoder().encode("test1"), "USER", true);
        User user1 = new User("test2", "test2@asf.com", passwordEncoder().encode("test2"), "USER", true);
        userRepo.save(user);
        userRepo.save(user1);
    }

}
