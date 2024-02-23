package io.ylab.backend.mapper;

import io.ylab.backend.entity.User;
import org.mapstruct.Mapper;
import io.ylab.backend.dto.response.UserDto;
import io.ylab.backend.model.UserModel;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "token", ignore = true)
    UserDto toUserDto(User user);

    User toUser(UserModel userModel);
}
