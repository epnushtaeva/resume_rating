package com.controllers;

import com.classes.LoadAjaxQueriesResult;
import com.dto.UserDto;
import com.services.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
@RequestMapping("/profile")
public class ProfileController extends BaseController{
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("")
    public ModelAndView getProfile(Principal principal){
        ModelAndView profileModelAndView = new ModelAndView("profile");
        this.addUserRightsInViewModel(profileModelAndView, principal);
        return profileModelAndView;
    }

    @PostMapping("/update")
    @ResponseBody
    public LoadAjaxQueriesResult updateProfile(@RequestBody UserDto userDto, Principal principal){
        this.userDetailsService.updateCurrentUser(userDto, principal);
        LoadAjaxQueriesResult res = new LoadAjaxQueriesResult();
        res.setRes("OK");
        return res;
    }

    @Override
    public UserDetailsServiceImpl getUserDetailsService() {
        return this.userDetailsService;
    }
}
