package com.services;

import com.data_base.entities.Speciality;
import com.dto.HrDto;
import com.dto.SpecialityDto;
import com.services.classes.PageSettings;
import com.services.classes.SpecialityFilters;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;

public interface SpecialityService {

    List<Speciality> getAllSpecialities();

    List<Speciality> getAllSpecialitiesOrdered();

    long getAllSpecialitiesCount();

    Speciality getSpecialityById(long specialityId);

    Speciality getSpecialityByName(String specialityName);

    @Transactional
    List<SpecialityDto> getSpecialities(PageSettings pageSettings, SpecialityFilters specialityFilters);

    @Transactional
    long getSpecialitiesCountForEditing(SpecialityFilters specialityFilters);

    @Transactional
    List<SpecialityDto> getSpecialitiesForFile(PageSettings pageSettings, SpecialityFilters specialityFilters);

    long getSpecialitiesCount(SpecialityFilters specialityFilters);

    HrDto updateSpecialityMark(long specialityMarkId, double mark, Principal principal);

    SpecialityDto addSpeciality(SpecialityDto specialityDto);

    void updateSpeciality(SpecialityDto specialityDto);

    void removeSpeciality(long specialityId);
}
