package com.controllers;

import com.classes.DataTableRequest;
import com.classes.LoadAjaxQueriesResult;
import com.classes.UsersDataTableResult;
import com.dto.HrDto;
import com.dto.UserDto;
import com.object_mappers.DataTableOptionsToPageSettingsMapper;
import com.object_mappers.DataTableOptionsToUserFiltersMapper;
import com.services.UsersService;
import com.services.classes.PageSettings;
import com.services.classes.UsersFilters;
import com.services.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UsersController extends BaseController{
    @Autowired
    private DataTableOptionsToPageSettingsMapper dataTableOptionsToPageSettingsMapper;

    @Autowired
    private DataTableOptionsToUserFiltersMapper dataTableOptionsToUserFiltersMapper;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UsersService usersService;

    @GetMapping("/hr_info")
    @ResponseBody
    public HrDto getHrInfo(@RequestParam(name = "id") long hrId){
        return this.userDetailsService.loadHrInfoByUserId(hrId);
    }

    @GetMapping("/all")
    public ModelAndView getAllUsers(Principal principal){
        ModelAndView allUsersModelAndView = new ModelAndView("users");
        this.addUserRightsInViewModel(allUsersModelAndView, principal);
        return allUsersModelAndView;
    }

    @PostMapping("/all")
    @ResponseBody
    public UsersDataTableResult getAllUsersPost(@RequestBody DataTableRequest dataTableRequest){
        PageSettings pageSettings = this.dataTableOptionsToPageSettingsMapper.distractPageSettings(dataTableRequest);
        UsersFilters usersFilters = this.dataTableOptionsToUserFiltersMapper.distractUserFilters(dataTableRequest);

        UsersDataTableResult usersDataTableResult = new UsersDataTableResult();
        usersDataTableResult.setData(this.usersService.getAllUsers(pageSettings, usersFilters));
        usersDataTableResult.setTotalRecords(this.usersService.getUsersCount(usersFilters));
        return usersDataTableResult;
    }

    @PostMapping("/add")
    @ResponseBody
    public LoadAjaxQueriesResult addUser(@RequestBody UserDto userDto){
        String addResult = this.usersService.addUser(userDto);
        LoadAjaxQueriesResult loadAjaxQueriesResult = new LoadAjaxQueriesResult();
        loadAjaxQueriesResult.setRes("OK");
        loadAjaxQueriesResult.setHrFullName(addResult);
        loadAjaxQueriesResult.setHrId(userDto.getId());
        return loadAjaxQueriesResult;
    }

    @PostMapping("/update")
    @ResponseBody
    public LoadAjaxQueriesResult updateUser(@RequestBody UserDto userDto){
        this.usersService.updateUser(userDto);
        LoadAjaxQueriesResult loadAjaxQueriesResult = new LoadAjaxQueriesResult();
        loadAjaxQueriesResult.setRes("OK");
        return loadAjaxQueriesResult;
    }

    @PostMapping("/remove")
    @ResponseBody
    public LoadAjaxQueriesResult removeUser(@RequestParam("userId") long userId){
        this.usersService.removeUser(userId);
        LoadAjaxQueriesResult loadAjaxQueriesResult = new LoadAjaxQueriesResult();
        loadAjaxQueriesResult.setRes("OK");
        return loadAjaxQueriesResult;
    }

    @PostMapping("/loadAvatar/{userId}")
    @ResponseBody
    public LoadAjaxQueriesResult updateUserAvatar(@PathVariable("userId") long userId, @RequestParam("avatar") MultipartFile file){
        String avatarPath = this.usersService.updateUserAvatar(userId, file);
        LoadAjaxQueriesResult loadAjaxQueriesResult = new LoadAjaxQueriesResult();
        loadAjaxQueriesResult.setRes("OK");
        loadAjaxQueriesResult.setHrFullName(avatarPath);
        return loadAjaxQueriesResult;
    }

    @Override
    public UserDetailsServiceImpl getUserDetailsService() {
        return this.userDetailsService;
    }
}
