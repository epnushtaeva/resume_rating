package com.services.impl;

import com.data_base.entities.User;
import com.data_base.repositories.UserRepository;
import com.dto.UserDto;
import com.object_mappers.UserToUserDtoMapper;
import com.services.FileService;
import com.services.PostService;
import com.services.RoleService;
import com.services.UsersService;
import com.services.classes.PageSettings;
import com.services.classes.UsersFilters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserToUserDtoMapper userToUserDtoMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private PostService postService;

    @Autowired
    private RoleService roleService;

    @Override
    @Transactional
    public List<UserDto> getAllUsers(PageSettings pageSettings, UsersFilters usersFilters) {
        Pageable dbQuerySettings = PageRequest.of(
                pageSettings.getPageNumber() - 1,
                pageSettings.getCountOfObjectsInOnePage(),
                Sort.Direction.DESC,
                "id"
        );
        List<UserDto> userDtos = this.userToUserDtoMapper.usersToUsersDtos(this.getUsersPage(dbQuerySettings, usersFilters));
        long userNumber = 1;

        for(UserDto userDto: userDtos){
            userDto.setRowNumber(userNumber);
            userNumber++;
        }

        return userDtos;
    }

    @Override
    @Transactional
    public long getUsersCount(UsersFilters usersFilters){
        return this.getUserCount(usersFilters);
    }

    @Override
    public String addUser(UserDto userDto){
        if(this.userRepository.existsByUsername(userDto.getUsername())){
            return "Пользователь с таким именем уже существует";
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getNewPassword()));
        user.setPost(this.postService.findPostById(userDto.getPostId()));
        user.setRole(this.roleService.findRoleById(userDto.getRoleId()));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        this.userRepository.saveAndFlush(user);

        userDto.setId(user.getId());
        return "";
    }

    @Override
    public void updateUser(UserDto userDto){
        User user = this.userRepository.getOne(userDto.getId());
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());

        if(!StringUtils.isEmpty(userDto.getNewPassword())) {
            user.setPassword(new BCryptPasswordEncoder().encode(userDto.getNewPassword()));
        }

        user.setPost(this.postService.findPostById(userDto.getPostId()));
        user.setRole(this.roleService.findRoleById(userDto.getRoleId()));
        this.userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public void removeUser(long userId){
          User user = this.userRepository.getOne(userId);

          if(!StringUtils.isEmpty(user.getAvatar())) {
            this.fileService.unlinkFileFromStorage(user.getAvatar());
          }

           this.fileService.clearFileHrManager(userId);
           this.userRepository.deleteById(userId);
    }

    @Override
    public String updateUserAvatar(long userId, MultipartFile multipartFile){
        String avatarPath = this.fileService.moveMultipartToStorage(multipartFile, multipartFile.getResource().getFilename());
        User user = this.userRepository.getOne(userId);

        if(!StringUtils.isEmpty(user.getAvatar())) {
            this.fileService.unlinkFileFromStorage(user.getAvatar());
        }

        user.setAvatar(avatarPath);
        this.userRepository.saveAndFlush(user);
        return  avatarPath;
    }

    private Page<User> getUsersPage(Pageable pageSettings, UsersFilters usersFilters) {
        if (!StringUtils.isEmpty(usersFilters.getFullName())) {
            return this.getPageWithUserFullName(pageSettings, usersFilters);
        } else {
            return this.getPageWithoutUserFullName(pageSettings, usersFilters);
        }
    }

    private long getUserCount(UsersFilters usersFilters){
        if (!StringUtils.isEmpty(usersFilters.getFullName())) {
            return this.getCountWithUserFullName(usersFilters);
        } else {
            return this.getCountWithoutUserFullName(usersFilters);
        }
    }

    private long getCountWithUserFullName(UsersFilters usersFilters){
        if (!StringUtils.isEmpty(usersFilters.getLogin())) {
            return this.getCountWithUserFullNameAndLogin(usersFilters);
        } else {
            return this.getCountWithUserFullNameAndWithoutLogin(usersFilters);
        }
    }

    private long getCountWithoutUserFullName(UsersFilters usersFilters){
        if (!StringUtils.isEmpty(usersFilters.getLogin())) {
            return this.getCountWithoutUserFullNameAndLogin(usersFilters);
        } else {
            return this.getCountWithoutUserFullNameAndWithoutLogin(usersFilters);
        }
    }

    private long getCountWithUserFullNameAndLogin(UsersFilters usersFilters){
        if (usersFilters.getPostId() != 0 && usersFilters.getRoleId() != 0) {
            return this.userRepository.countByUsernameContainingIgnoreCaseAndFullNameContainingIgnoreCaseAndPostIdAndRoleId(
                    usersFilters.getLogin(),
                    usersFilters.getFullName(),
                    usersFilters.getPostId(),
                    usersFilters.getRoleId());
        } else {
            if (usersFilters.getRoleId() != 0) {
                return this.userRepository.countByUsernameContainingIgnoreCaseAndFullNameContainingIgnoreCaseAndRoleId(
                        usersFilters.getLogin(),
                        usersFilters.getFullName(),
                        usersFilters.getRoleId()
                );
            }

            if (usersFilters.getPostId() != 0) {
                return this.userRepository.countByUsernameContainingIgnoreCaseAndFullNameContainingIgnoreCaseAndPostId(
                        usersFilters.getLogin(),
                        usersFilters.getFullName(),
                        usersFilters.getPostId()
                );
            }

            return this.userRepository.countByUsernameContainingIgnoreCaseAndFullNameContainingIgnoreCase(
                    usersFilters.getLogin(),
                    usersFilters.getFullName()
            );
        }
    }

    private long getCountWithUserFullNameAndWithoutLogin(UsersFilters usersFilters){
        if (usersFilters.getPostId() != 0 && usersFilters.getRoleId() != 0) {
            return this.userRepository.countByFullNameContainingIgnoreCaseAndPostIdAndRoleId(
                    usersFilters.getFullName(),
                    usersFilters.getPostId(),
                    usersFilters.getRoleId()
            );
        }  else {
            if (usersFilters.getRoleId() != 0) {
                return this.userRepository.countByFullNameContainingIgnoreCaseAndRoleId(
                        usersFilters.getFullName(),
                        usersFilters.getRoleId()
                );
            }

            if (usersFilters.getPostId() != 0) {
                return this.userRepository.countByFullNameContainingIgnoreCaseAndPostId(
                        usersFilters.getFullName(),
                        usersFilters.getPostId()
                );
            }

            return this.userRepository.countByFullNameContainingIgnoreCase(usersFilters.getFullName());
        }
    }

    private long getCountWithoutUserFullNameAndLogin(UsersFilters usersFilters){
        if (usersFilters.getPostId() != 0 && usersFilters.getRoleId() != 0) {
            return this.userRepository.countByUsernameContainingIgnoreCaseAndPostIdAndRoleId(
                    usersFilters.getLogin(),
                    usersFilters.getPostId(),
                    usersFilters.getRoleId()
            );
        }  else {
            if (usersFilters.getRoleId() != 0) {
                return this.userRepository.countByUsernameContainingIgnoreCaseAndRoleId(
                        usersFilters.getLogin(),
                        usersFilters.getRoleId()
                );
            }

            if (usersFilters.getPostId() != 0) {
                return this.userRepository.countByUsernameContainingIgnoreCaseAndPostId(
                        usersFilters.getLogin(),
                        usersFilters.getPostId()
                );
            }

            return this.userRepository.countByUsernameContainingIgnoreCase(usersFilters.getLogin());
        }
    }

    private long getCountWithoutUserFullNameAndWithoutLogin(UsersFilters usersFilters){
        if (usersFilters.getPostId() != 0 && usersFilters.getRoleId() != 0) {
            return this.userRepository.countByRoleIdAndPostId(usersFilters.getRoleId(), usersFilters.getPostId());
        } else {
            if (usersFilters.getRoleId() != 0) {
                return this.userRepository.countByRoleId(usersFilters.getRoleId());
            }

            if (usersFilters.getPostId() != 0) {
                return this.userRepository.countByPostId(usersFilters.getPostId());
            }

            return this.userRepository.count();
        }
    }

    private Page<User> getPageWithUserFullName(Pageable pageSettings, UsersFilters usersFilters) {
        if (!StringUtils.isEmpty(usersFilters.getLogin())) {
            return this.getPageWithUserFullNameAndLogin(pageSettings, usersFilters);
        } else {
            return this.getPageWithUserFullNameAndWithoutLogin(pageSettings, usersFilters);
        }
    }

    private Page<User> getPageWithoutUserFullName(Pageable pageSettings, UsersFilters usersFilters) {
        if (!StringUtils.isEmpty(usersFilters.getLogin())) {
            return this.getPageWithoutUserFullNameAndWithLogin(pageSettings, usersFilters);
        } else {
            return this.getPageWithoutUserFullNameAndWithoutLogin(pageSettings, usersFilters);
        }
    }

    private Page<User> getPageWithUserFullNameAndLogin(Pageable pageSettings, UsersFilters usersFilters) {
        if (usersFilters.getPostId() != 0 && usersFilters.getRoleId() != 0) {
            return this.userRepository.findAllByUsernameContainingIgnoreCaseAndFullNameContainingIgnoreCaseAndPostIdAndRoleIdOrderByIdDesc(
                    pageSettings,
                    usersFilters.getLogin(),
                    usersFilters.getFullName(),
                    usersFilters.getPostId(),
                    usersFilters.getRoleId());
        } else {
            if (usersFilters.getRoleId() != 0) {
                return this.userRepository.findAllByUsernameContainingIgnoreCaseAndFullNameContainingIgnoreCaseAndRoleIdOrderByIdDesc(
                        pageSettings,
                        usersFilters.getLogin(),
                        usersFilters.getFullName(),
                        usersFilters.getRoleId()
                );
            }

            if (usersFilters.getPostId() != 0) {
                return this.userRepository.findAllByUsernameContainingIgnoreCaseAndFullNameContainingIgnoreCaseAndPostIdOrderByIdDesc(
                        pageSettings,
                        usersFilters.getLogin(),
                        usersFilters.getFullName(),
                        usersFilters.getPostId()
                );
            }

            return this.userRepository.findAllByUsernameContainingIgnoreCaseAndFullNameContainingIgnoreCaseOrderByIdDesc(pageSettings, usersFilters.getLogin(),
                    usersFilters.getFullName());
        }
    }

    private Page<User> getPageWithUserFullNameAndWithoutLogin(Pageable pageSettings, UsersFilters usersFilters) {
        if (usersFilters.getPostId() != 0 && usersFilters.getRoleId() != 0) {
            return this.userRepository.findAllByFullNameContainingIgnoreCaseAndPostIdAndRoleIdOrderByIdDesc(
                    pageSettings,
                    usersFilters.getFullName(),
                    usersFilters.getPostId(),
                    usersFilters.getRoleId()
            );
        } else {
            if (usersFilters.getRoleId() != 0) {
                return this.userRepository.findAllByFullNameContainingIgnoreCaseAndRoleIdOrderByIdDesc(
                        pageSettings,
                        usersFilters.getFullName(),
                        usersFilters.getRoleId()
                );
            }

            if (usersFilters.getPostId() != 0) {
                return this.userRepository.findAllByFullNameContainingIgnoreCaseAndPostIdOrderByIdDesc(
                        pageSettings,
                        usersFilters.getFullName(),
                        usersFilters.getPostId()
                );
            }

            return this.userRepository.findAllByFullNameContainingIgnoreCaseOrderByIdDesc(pageSettings, usersFilters.getFullName());
        }
    }

    private Page<User> getPageWithoutUserFullNameAndWithLogin(Pageable pageSettings, UsersFilters usersFilters) {
        if (usersFilters.getPostId() != 0 && usersFilters.getRoleId() != 0) {
            return this.userRepository.findAllByUsernameContainingIgnoreCaseAndPostIdAndRoleIdOrderByIdDesc(
                    pageSettings,
                    usersFilters.getLogin(),
                    usersFilters.getPostId(),
                    usersFilters.getRoleId()
            );
        } else {
            if (usersFilters.getRoleId() != 0) {
                return this.userRepository.findAllByUsernameContainingIgnoreCaseAndRoleIdOrderByIdDesc(
                        pageSettings,
                        usersFilters.getLogin(),
                        usersFilters.getRoleId()
                );
            }

            if (usersFilters.getPostId() != 0) {
                return this.userRepository.findAllByUsernameContainingIgnoreCaseAndPostIdOrderByIdDesc(
                        pageSettings,
                        usersFilters.getLogin(),
                        usersFilters.getPostId()
                );
            }

            return this.userRepository.findAllByUsernameContainingIgnoreCaseOrderByIdDesc(pageSettings, usersFilters.getLogin());
        }
    }

    private Page<User> getPageWithoutUserFullNameAndWithoutLogin(Pageable pageSettings, UsersFilters usersFilters) {
        if (usersFilters.getPostId() != 0 && usersFilters.getRoleId() != 0) {
            return this.userRepository.findAllByRoleIdAndPostIdOrderByIdDesc(pageSettings, usersFilters.getRoleId(), usersFilters.getPostId());
        } else {
            if (usersFilters.getRoleId() != 0) {
                return this.userRepository.findAllByRoleIdOrderByIdDesc(pageSettings, usersFilters.getRoleId());
            }

            if (usersFilters.getPostId() != 0) {
                return this.userRepository.findAllByPostIdOrderByIdDesc(pageSettings, usersFilters.getPostId());
            }

            return this.userRepository.findAllByOrderByIdDesc(pageSettings);
        }
    }
}
