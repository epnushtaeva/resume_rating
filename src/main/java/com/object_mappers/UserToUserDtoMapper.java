package com.object_mappers;

import com.data_base.entities.User;
import com.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserToUserDtoMapper {
    @Mappings({
            @Mapping(target="post", source="user.post.name"),
            @Mapping(target="role", source="user.role.russianName"),
            @Mapping(target="lastLogin", source="user.lastLogin", dateFormat = "dd.MM.yyyy"),
            @Mapping(target="postId", source="user.post.id"),
            @Mapping(target="roleId", source="user.role.id"),
            @Mapping(target = "avatar", source = "user.avatar")
    })
    UserDto userToUserDto(User user);

    List<UserDto> usersToUsersDtos(Page<User> users);
}
