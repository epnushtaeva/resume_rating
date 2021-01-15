package com.controllers;

import com.classes.LoadAjaxQueriesResult;
import com.classes.DataTableRequest;
import com.classes.FileDataTableResult;
import com.object_mappers.DataTableOptionsToFileFiltersMapper;
import com.object_mappers.DataTableOptionsToPageSettingsMapper;
import com.services.FileService;
import com.services.classes.FileFilters;
import com.services.classes.PageSettings;
import com.services.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
@RequestMapping("/learn_example")
public class LearnExamplesController extends BaseController {
    @Autowired
    private DataTableOptionsToFileFiltersMapper dataTableOptionsToFileFiltersMapper;

    @Autowired
    private DataTableOptionsToPageSettingsMapper dataTableOptionsToPageSettingsMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("/all")
    public ModelAndView getAllLearnExamples(Principal principal) {
        ModelAndView allFilesModelAndView = new ModelAndView("all_files");
        allFilesModelAndView.addObject("isCanAddFile", true);
        allFilesModelAndView.addObject("ajax_url", "/learn_example/all");

        this.addUserRightsInViewModel(allFilesModelAndView, principal);

        return allFilesModelAndView;
    }

    @PostMapping("/all")
    @ResponseBody
    public FileDataTableResult getAllLearnExamplesPost(@RequestBody DataTableRequest dataTableRequest) {
        PageSettings pageSettings = this.dataTableOptionsToPageSettingsMapper.distractPageSettings(dataTableRequest);
        FileFilters fileFilters = this.dataTableOptionsToFileFiltersMapper.distractFileFilters(dataTableRequest);
        fileFilters.setLearnExample(true);

        FileDataTableResult result = new FileDataTableResult();
        result.setData(this.fileService.getFiles(pageSettings, fileFilters));
        result.setTotalRecords(this.fileService.getFilesCount(fileFilters));

        return result;
    }

    @PostMapping("/load")
    @ResponseBody
    public LoadAjaxQueriesResult loadLearnExampleFile(@RequestParam("file") MultipartFile file){
        this.fileService.moveAndSaveLearnExample(file, file.getOriginalFilename());
        LoadAjaxQueriesResult res = new LoadAjaxQueriesResult();
        res.setRes("OK");
        return res;
    }

    @Override
    public UserDetailsServiceImpl getUserDetailsService() {
        return this.userDetailsService;
    }
}
