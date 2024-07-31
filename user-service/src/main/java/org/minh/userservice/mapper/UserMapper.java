package org.minh.userservice.mapper;

import org.mapstruct.Mapper;
import org.minh.userservice.entity.Users;
import org.minh.userservice.model.request.CreateUserRequest;


@Mapper(componentModel = "spring")
public interface UserMapper {
    Users toUser (CreateUserRequest request);
}
