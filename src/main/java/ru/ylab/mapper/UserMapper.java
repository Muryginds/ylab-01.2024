package ru.ylab.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.ylab.entity.User;
import ru.ylab.dto.response.UserDTO;
import ru.ylab.model.UserModel;

@Mapper
public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    UserDTO toUserDTO(User user);

    User toUser(UserModel userModel);
}
