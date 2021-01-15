package com.data_base.repositories;

import com.data_base.entities.User;
import com.services.classes.PageSettings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    boolean existsByUsername(String userName);

    User findOneByUsername(String userName);

    Page<User> findAllByOrderByIdDesc(Pageable pageSettings);

    Page<User> findAllByUsernameContainingIgnoreCaseAndFullNameContainingIgnoreCaseAndPostIdAndRoleIdOrderByIdDesc(Pageable pageSettings, String userName, String fullName, long postId, long roleId);

    Page<User> findAllByUsernameContainingIgnoreCaseAndPostIdAndRoleIdOrderByIdDesc(Pageable pageSettings, String userName, long postId, long roleId);

    Page<User> findAllByFullNameContainingIgnoreCaseAndPostIdAndRoleIdOrderByIdDesc(Pageable pageSettings, String fullName, long postId, long roleId);

    Page<User> findAllByUsernameContainingIgnoreCaseAndFullNameContainingIgnoreCaseAndPostIdOrderByIdDesc(Pageable pageSettings, String userName, String fullName, long postId);

    Page<User> findAllByUsernameContainingIgnoreCaseAndFullNameContainingIgnoreCaseAndRoleIdOrderByIdDesc(Pageable pageSettings, String userName, String fullName, long roleId);

    Page<User> findAllByUsernameContainingIgnoreCaseAndFullNameContainingIgnoreCaseOrderByIdDesc(Pageable pageSettings, String userName, String fullName);

    Page<User> findAllByUsernameContainingIgnoreCaseAndRoleIdOrderByIdDesc(Pageable pageSettings, String userName, long roleId);

    Page<User> findAllByUsernameContainingIgnoreCaseAndPostIdOrderByIdDesc(Pageable pageSettings, String userName, long postId);

    Page<User> findAllByFullNameContainingIgnoreCaseAndRoleIdOrderByIdDesc(Pageable pageSettings, String fullName, long roleId);

    Page<User> findAllByFullNameContainingIgnoreCaseAndPostIdOrderByIdDesc(Pageable pageSettings, String fullName, long postId);

    Page<User> findAllByRoleIdAndPostIdOrderByIdDesc(Pageable pageSettings, long roleId, long postId);

    Page<User> findAllByPostIdOrderByIdDesc(Pageable pageSettings, long postId);

    Page<User> findAllByRoleIdOrderByIdDesc(Pageable pageSettings, long roleId);

    Page<User> findAllByFullNameContainingIgnoreCaseOrderByIdDesc(Pageable pageSettings, String fullName);

    Page<User> findAllByUsernameContainingIgnoreCaseOrderByIdDesc(Pageable pageSettings, String userName);

    long countByUsernameContainingIgnoreCaseAndFullNameContainingIgnoreCaseAndPostIdAndRoleId(String userName, String fullName, long postId, long roleId);

    long countByUsernameContainingIgnoreCaseAndPostIdAndRoleId(String userName, long postId, long roleId);

    long countByFullNameContainingIgnoreCaseAndPostIdAndRoleId(String fullName, long postId, long roleId);

    long countByUsernameContainingIgnoreCaseAndFullNameContainingIgnoreCaseAndPostId(String userName, String fullName, long postId);

    long countByUsernameContainingIgnoreCaseAndFullNameContainingIgnoreCaseAndRoleId(String userName, String fullName, long roleId);

    long countByUsernameContainingIgnoreCaseAndFullNameContainingIgnoreCase(String userName, String fullName);

    long countByUsernameContainingIgnoreCaseAndRoleId(String userName, long roleId);

    long countByUsernameContainingIgnoreCaseAndPostId(String userName, long postId);

    long countByFullNameContainingIgnoreCaseAndRoleId(String fullName, long roleId);

    long countByFullNameContainingIgnoreCaseAndPostId(String fullName, long postId);

    long countByRoleIdAndPostId(long roleId, long postId);

    long countByPostId(long postId);

    long countByRoleId(long roleId);

    long countByFullNameContainingIgnoreCase(String fullName);

    long countByUsernameContainingIgnoreCase(String userName);
}
