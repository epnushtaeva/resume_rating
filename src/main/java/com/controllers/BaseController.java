package com.controllers;

import com.services.impl.UserDetailsServiceImpl;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

public abstract class BaseController {

    public void addUserRightsInViewModel(ModelAndView modelAndView, Principal principal){
        modelAndView.addObject("isCanSeeManu", this.getUserDetailsService().isCanSeeMenu(principal));
        modelAndView.addObject("isCanLoadFiles", this.getUserDetailsService().isCanLoadFiles(principal));
        modelAndView.addObject("isCanEditSettings", this.getUserDetailsService().isCanEditSettings(principal));
        modelAndView.addObject("userAvatar", this.getUserDetailsService().getUserAvatar(principal));
        modelAndView.addObject("userFullName", this.getUserDetailsService().getUserFullName(principal));
        modelAndView.addObject("email", this.getUserDetailsService().getUserEmail(principal));
        modelAndView.addObject("phone", this.getUserDetailsService().getUserPhone(principal));
        modelAndView.addObject("userId", this.getUserDetailsService().getUserId(principal));
    }

    public abstract UserDetailsServiceImpl getUserDetailsService();
}
