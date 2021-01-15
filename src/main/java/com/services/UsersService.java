package com.services;

import com.dto.UserDto;
import com.services.classes.PageSettings;
import com.services.classes.UsersFilters;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UsersService {
    List<UserDto> getAllUsers(PageSettings pageSettings, UsersFilters usersFilters);

    long getUsersCount(UsersFilters usersFilters);

    String addUser(UserDto userDto);

    void updateUser(UserDto userDto);

    void removeUser(long userId);

    String updateUserAvatar(long userId, MultipartFile multipartFile);
}
