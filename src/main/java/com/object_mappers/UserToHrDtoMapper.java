package com.object_mappers;

import com.data_base.entities.User;
import com.dto.HrDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserToHrDtoMapper {
    @Mappings({
            @Mapping(target="fullName", source="user.fullName"),
            @Mapping(target="post", source="user.post.name"),
            @Mapping(target="email", source="user.email"),
            @Mapping(target="phone", source="user.phone")
    })
    HrDto userToHrDto(User user);
}
