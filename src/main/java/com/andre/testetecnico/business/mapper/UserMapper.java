package com.andre.testetecnico.business.mapper;

import com.andre.testetecnico.business.userDtos.UserRequestDTO;
import com.andre.testetecnico.business.userDtos.UserResponseDTO;
import com.andre.testetecnico.user.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity requestToEntity (UserRequestDTO dto);

    UserResponseDTO entityToResponse (UserEntity entity);
}
