package com.services.impl;

import com.data_base.entities.User;
import com.data_base.repositories.UserRepository;
import com.dto.HrDto;
import com.dto.UserDto;
import com.object_mappers.UserToHrDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserToHrDtoMapper userToHrDtoMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        if(this.userRepository.existsByUsername(userName)){
            return this.userRepository.findOneByUsername(userName);
        }

        throw new UsernameNotFoundException("Пользователь с такими учетными данными не найден");
    }

    @Transactional
    public boolean isCanLoadFiles(Principal principal){
        return this.userRepository.findOneByUsername(principal.getName())
                .getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN") || authority.getAuthority().equals("HR_MANAGER"));
    }

    @Transactional
    public boolean isCanSeeMenu(Principal principal){
        return this.userRepository.findOneByUsername(principal.getName())
                .getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN") || authority.getAuthority().equals("DATA_SPECIALIST"));
    }

    @Transactional
    public boolean isCanEditSettings(Principal principal){
        return this.userRepository.findOneByUsername(principal.getName())
                .getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));
    }

    public HrDto loadHrInfoByUserId(long userId){
        return this.userToHrDtoMapper.userToHrDto(this.userRepository.getOne(userId));
    }

    @Transactional
    public String getUserAvatar(Principal principal){
        return Optional.ofNullable(this.userRepository.findOneByUsername(principal.getName()).getAvatar()).orElse("");
    }

    @Transactional
    public String getUserFullName(Principal principal){
        return Optional.ofNullable(this.userRepository.findOneByUsername(principal.getName()).getFullName()).orElse("");
    }

    @Transactional
    public long getUserId(Principal principal){
        return Optional.ofNullable(this.userRepository.findOneByUsername(principal.getName()).getId()).orElse(0l);
    }

    @Transactional
    public String getUserEmail(Principal principal){
        return Optional.ofNullable(this.userRepository.findOneByUsername(principal.getName()).getEmail()).orElse("");
    }

    @Transactional
    public String getUserPhone(Principal principal){
        return Optional.ofNullable(this.userRepository.findOneByUsername(principal.getName()).getPhone()).orElse("");
    }

    public void updateCurrentUser(UserDto userDto, Principal principal){
        User user = this.userRepository.findOneByUsername(principal.getName());
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        this.userRepository.saveAndFlush(user);
    }
}
