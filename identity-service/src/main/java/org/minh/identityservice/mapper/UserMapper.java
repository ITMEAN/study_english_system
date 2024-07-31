package org.minh.identityservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.minh.identityservice.entity.Users;
import org.minh.identityservice.model.request.CreateUserRequest;
import org.minh.identityservice.model.request.UpdateUserRequest;
import org.minh.identityservice.model.response.UserResponseDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Users toUser (CreateUserRequest request);
    @Mapping(target = "role", source = "role.name")
    UserResponseDTO toUserResponseDTO (Users user);
}
