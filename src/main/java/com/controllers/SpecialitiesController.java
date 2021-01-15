package com.controllers;

import com.classes.DataTableRequest;
import com.classes.LoadAjaxQueriesResult;
import com.classes.SpecialityDataTableResult;
import com.data_base.entities.Speciality;
import com.dto.HrDto;
import com.dto.SpecialityDto;
import com.object_mappers.DataTableOptionsToPageSettingsMapper;
import com.object_mappers.DataTableOptionsToSpecialityFiltersMapper;
import com.services.SpecialityService;
import com.services.classes.PageSettings;
import com.services.classes.SpecialityFilters;
import com.services.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/speciality")
public class SpecialitiesController extends BaseController{

    @Autowired
    private DataTableOptionsToPageSettingsMapper dataTableOptionsToPageSettingsMapper;

    @Autowired
    private DataTableOptionsToSpecialityFiltersMapper dataTableOptionsToSpecialityFiltersMapper;

    @Autowired
    private SpecialityService specialityService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("/getall")
    public ModelAndView getAllSpecialitiesPage(Principal principal) {
        ModelAndView allSpecialitiesModelAndView = new ModelAndView("specialities");
        allSpecialitiesModelAndView.addObject("isCanAddFile", false);

        this.addUserRightsInViewModel(allSpecialitiesModelAndView, principal);

        return allSpecialitiesModelAndView;
    }

    @PostMapping("/all")
    @ResponseBody
    public SpecialityDataTableResult getAllFilesPost(@RequestBody DataTableRequest dataTableRequest) {
        PageSettings pageSettings = this.dataTableOptionsToPageSettingsMapper.distractPageSettings(dataTableRequest);
        SpecialityFilters specialityFilters = this.dataTableOptionsToSpecialityFiltersMapper.distractSpecialityFilters(dataTableRequest);

        SpecialityDataTableResult result = new SpecialityDataTableResult();
        result.setData(this.specialityService.getSpecialitiesForFile(pageSettings, specialityFilters));
        result.setTotalRecords(this.specialityService.getSpecialitiesCount(specialityFilters));

        return result;
    }

    @PostMapping("/update")
    @ResponseBody
    public LoadAjaxQueriesResult updateSpecialityMark(@RequestParam("specialityMarkId") long specialityMarkId, @RequestParam("mark") double mark, Principal principal){
        HrDto hrDto = this.specialityService.updateSpecialityMark(specialityMarkId, mark, principal);
        LoadAjaxQueriesResult res = new LoadAjaxQueriesResult();
        res.setRes("OK");

        if(hrDto.getFullName() != null){
            res.setHrFullName(hrDto.getFullName());
        }

        res.setHrId(hrDto.getId());
        return res;
    }

    @PostMapping("/list")
    @ResponseBody
    public List<Speciality> getAllSpecialities(){
        return this.specialityService.getAllSpecialities();
    }

    @PostMapping("/add")
    @ResponseBody
    public SpecialityDto addSpeciality(@RequestBody SpecialityDto specialityDto){
        return this.specialityService.addSpeciality(specialityDto);
    }

    @PostMapping("/update_speciality")
    @ResponseBody
    public LoadAjaxQueriesResult updateSpeciality(@RequestBody SpecialityDto specialityDto){
      this.specialityService.updateSpeciality(specialityDto);

        LoadAjaxQueriesResult res = new LoadAjaxQueriesResult();
        res.setRes("OK");
        return res;
    }

    @PostMapping("/all_for_editing")
    @ResponseBody
    public SpecialityDataTableResult getAllSpecialitiesForEditing(@RequestBody DataTableRequest dataTableRequest){
        PageSettings pageSettings = this.dataTableOptionsToPageSettingsMapper.distractPageSettings(dataTableRequest);
        SpecialityFilters specialityFilters = this.dataTableOptionsToSpecialityFiltersMapper.distractSpecialityFilters(dataTableRequest);

        SpecialityDataTableResult result = new SpecialityDataTableResult();
        result.setData(this.specialityService.getSpecialities(pageSettings, specialityFilters));
        result.setTotalRecords(this.specialityService.getSpecialitiesCountForEditing(specialityFilters));

        return result;
    }

    @PostMapping("/remove")
    @ResponseBody
    public LoadAjaxQueriesResult removeSpeciality(@RequestParam("specialityId") long specialityId){
        this.specialityService.removeSpeciality(specialityId);

        LoadAjaxQueriesResult res = new LoadAjaxQueriesResult();
        res.setRes("OK");
        return res;
    }

    @Override
    public UserDetailsServiceImpl getUserDetailsService() {
        return this.userDetailsService;
    }
}
