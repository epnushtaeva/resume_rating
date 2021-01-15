package com.controllers;

import com.classes.FileDataTableResult;
import com.classes.DataTableRequest;
import com.classes.LoadAjaxQueriesResult;
import com.dto.FileHiredDto;
import com.dto.FileMarkDto;
import com.dto.HrDto;
import com.object_mappers.DataTableOptionsToFileFiltersMapper;
import com.object_mappers.DataTableOptionsToPageSettingsMapper;
import com.services.FileService;
import com.services.NeuralNetworkService;
import com.services.classes.FileFilters;
import com.services.classes.PageSettings;
import com.services.impl.UserDetailsServiceImpl;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/file")
public class FileController extends BaseController{
    @Autowired
    private NeuralNetworkService neuralNetworkService;

    @Autowired
    private DataTableOptionsToFileFiltersMapper dataTableOptionsToFileFiltersMapper;

    @Autowired
    private DataTableOptionsToPageSettingsMapper dataTableOptionsToPageSettingsMapper;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Autowired
    private FileService fileService;

    @GetMapping("/load")
    public ModelAndView loadFile(Principal principal) {
        ModelAndView allFilesModelAndView = new ModelAndView("load_file");

        this.addUserRightsInViewModel(allFilesModelAndView, principal);

        return allFilesModelAndView;
    }

    @GetMapping("/all")
    public ModelAndView getAllFiles(Principal principal) {
        ModelAndView allFilesModelAndView = new ModelAndView("all_files");
        allFilesModelAndView.addObject("isCanAddFile", false);
        allFilesModelAndView.addObject("ajax_url", "/file/all");

        this.addUserRightsInViewModel(allFilesModelAndView, principal);

        return allFilesModelAndView;
    }

    @PostMapping("/all")
    @ResponseBody
    public FileDataTableResult getAllFilesPost(@RequestBody DataTableRequest dataTableRequest) {
        PageSettings pageSettings = this.dataTableOptionsToPageSettingsMapper.distractPageSettings(dataTableRequest);
        FileFilters fileFilters = this.dataTableOptionsToFileFiltersMapper.distractFileFilters(dataTableRequest);

        FileDataTableResult result = new FileDataTableResult();
        result.setData(this.fileService.getFiles(pageSettings, fileFilters));
        result.setTotalRecords(this.fileService.getFilesCount(fileFilters));

        return result;
    }

    @PostMapping("/coast")
    @ResponseBody
    public List<FileMarkDto> coastFile(@RequestParam("file") MultipartFile file, Principal principal){
        return neuralNetworkService.costAndSaveFile(file,file.getOriginalFilename(), principal);
    }

    @PostMapping("/remove")
    @ResponseBody
    public LoadAjaxQueriesResult removeFile(@RequestParam("fileId") long fileId){
        this.fileService.removeFile(fileId);

        LoadAjaxQueriesResult res = new LoadAjaxQueriesResult();
        res.setRes("OK");
        return res;
    }

    @PostMapping("/update_hired")
    @ResponseBody
    public LoadAjaxQueriesResult removeFile(@RequestBody FileHiredDto fileHiredDto, Principal principal){
        HrDto hrDto = this.fileService.updateFileHired(fileHiredDto, principal);
        LoadAjaxQueriesResult res = new LoadAjaxQueriesResult();
        res.setRes("OK");

        if(hrDto.getFullName() != null){
            res.setHrFullName(hrDto.getFullName());
        }

        res.setHrId(hrDto.getId());
        return res;
    }

    @PostMapping("/make_learn_example")
    @ResponseBody
    public LoadAjaxQueriesResult setLearnExampleFromFile(@RequestParam("file_id") long fileId){
        LoadAjaxQueriesResult res = new LoadAjaxQueriesResult();
        res.setHrId(this.fileService.makeLearnExampleFromRealFile(fileId));
        res.setRes("OK");
        return res;
    }

    @PostMapping("/load_neural_network")
    @ResponseBody
    public LoadAjaxQueriesResult loadNeuralNetworkToDataBase(@RequestParam("speciality_id") long specialityId, @RequestParam("file") MultipartFile neuralNetworkFile){
       this.neuralNetworkService.loadNeuralNetworkEntitiesToDataBase(specialityId, neuralNetworkFile);
        LoadAjaxQueriesResult res = new LoadAjaxQueriesResult();
        res.setRes("OK");
        return res;
    }

    @PostMapping("/load_neural_network_to_file")
    @ResponseBody
    public LoadAjaxQueriesResult loadNeuralNetworkToFromDataBase(@RequestParam("speciality_id") long specialityId){
        String jsonFilePath = this.neuralNetworkService.loadNeuralNetworkEntitiesFromDataBase(specialityId);
        LoadAjaxQueriesResult res = new LoadAjaxQueriesResult();
        res.setHrFullName(jsonFilePath);
        res.setRes("OK");
        return res;
    }

    @Override
    public UserDetailsServiceImpl getUserDetailsService() {
        return this.userDetailsService;
    }
}
