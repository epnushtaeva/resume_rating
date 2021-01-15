package com.services;

import com.data_base.entities.Settings;

public interface SettingsService {
    Settings getSettings();

    void updateEmail(String email);
}
