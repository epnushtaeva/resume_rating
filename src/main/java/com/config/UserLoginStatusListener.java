package com.config;

import com.data_base.entities.User;
import com.data_base.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.time.LocalDate;
import java.util.Date;

@Component
public class UserLoginStatusListener implements HttpSessionBindingListener {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        UserDetails user = (UserDetails) event.getValue();

        User updatedUser = this.userRepository.findOneByUsername(user.getUsername());
        updatedUser.setOnline(true);
        updatedUser.setLastLogin(java.sql.Date.valueOf(LocalDate.now()));

        this.userRepository.save(updatedUser);
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        UserDetails user = (UserDetails) event.getValue();

        User updatedUser = this.userRepository.findOneByUsername(user.getUsername());
        updatedUser.setOnline(false);

        this.userRepository.save(updatedUser);
    }
}
