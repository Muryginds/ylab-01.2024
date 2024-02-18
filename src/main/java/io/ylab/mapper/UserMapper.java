package io.ylab.mapper;

import org.mapstruct.Mapper;
import io.ylab.entity.User;
import io.ylab.dto.response.UserDto;
import io.ylab.model.UserModel;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "token", ignore = true)
    UserDto toUserDto(User user);

    User toUser(UserModel userModel);
}
