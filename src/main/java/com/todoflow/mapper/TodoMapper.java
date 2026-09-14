package com.todoflow.mapper;

import com.todoflow.dto.todo.TodoCreateRequestDto;
import com.todoflow.dto.todo.TodoResponseDto;
import com.todoflow.dto.todo.TodoUpdateRequestDto;
import com.todoflow.entity.Todo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TodoMapper {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.username", target = "ownerUsername")
    TodoResponseDto toResponseDto(Todo todo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "completed", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "shares", ignore = true)
    Todo toEntity(TodoCreateRequestDto request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "shares", ignore = true)
    void updateEntity(
            TodoUpdateRequestDto request,
            @MappingTarget Todo todo
    );
}