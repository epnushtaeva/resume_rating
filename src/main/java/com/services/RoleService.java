package com.services;

import com.data_base.entities.Role;

import java.util.List;

public interface RoleService {
    Role findRoleById(long id);

    List<Role> findAllRoles();
}
