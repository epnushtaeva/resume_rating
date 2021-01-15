package com.controllers;

import com.classes.LoadAjaxQueriesResult;
import com.data_base.entities.Settings;
import com.services.SettingsService;
import com.services.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
@RequestMapping("/settings")
public class SettingsController  extends BaseController{
    @Autowired
    private SettingsService settingsService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("/all")
    public ModelAndView editSettings(Principal principal){
        ModelAndView result = new ModelAndView("settings");
        result.addObject("email", settingsService.getSettings().getEmail());

        this.addUserRightsInViewModel(result, principal);

        return result;
    }

    @PostMapping("/edit")
    @ResponseBody
    public LoadAjaxQueriesResult updateEmail(@RequestBody Settings settings){
        this.settingsService.updateEmail(settings.getEmail());

        LoadAjaxQueriesResult result = new LoadAjaxQueriesResult();
        result.setRes("OK");
        return result;
    }

    @Override
    public UserDetailsServiceImpl getUserDetailsService() {
        return this.userDetailsService;
    }
}
