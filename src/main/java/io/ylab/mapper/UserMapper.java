package io.ylab.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import io.ylab.entity.User;
import io.ylab.dto.response.UserDTO;
import io.ylab.model.UserModel;

@Mapper
public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    UserDTO toUserDTO(User user);

    User toUser(UserModel userModel);
}
