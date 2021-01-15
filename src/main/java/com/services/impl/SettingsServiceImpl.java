package com.services.impl;

import com.data_base.entities.Settings;
import com.data_base.repositories.SettingsRepository;
import com.services.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingsServiceImpl implements SettingsService {
    @Autowired
    private SettingsRepository settingsRepository;

    @Override
    public Settings getSettings() {
        List<Settings> settingsList = this.settingsRepository.findAll();

        if(settingsList.size() > 0){
            return settingsList.get(0);
        }

        Settings settings = new Settings();
        settings.setEmail("");
        return settings;
    }

    @Override
    public void updateEmail(String email) {
        List<Settings> settingsList = this.settingsRepository.findAll();
        Settings settings = null;

        if(settingsList.size() > 0){
            settings = settingsList.get(0);
        } else {
            settings = new Settings();
        }

        settings.setEmail(email);
        this.settingsRepository.save(settings);
    }
}
