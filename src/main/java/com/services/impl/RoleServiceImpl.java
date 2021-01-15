package com.services.impl;

import com.data_base.entities.Role;
import com.data_base.repositories.RoleRepository;
import com.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findRoleById(long id){
        return this.roleRepository.getOne(id);
    }

    @Override
    public List<Role> findAllRoles(){
        return this.roleRepository.findAll();
    }
}
