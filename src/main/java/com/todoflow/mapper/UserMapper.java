package com.todoflow.mapper;

import com.todoflow.dto.user.UserCreateRequestDto;
import com.todoflow.dto.user.UserResponseDto;
import com.todoflow.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toResponseDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "todos", ignore = true)
    @Mapping(target = "todoShares", ignore = true)
    User toEntity(UserCreateRequestDto request);
}