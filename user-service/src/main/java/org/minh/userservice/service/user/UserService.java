package org.minh.userservice.service.user;


import org.minh.userservice.entity.Users;
import org.minh.userservice.exception.AlreadyExistException;
import org.minh.userservice.exception.DataNotFoundException;
import org.minh.userservice.model.request.CreateUserRequest;
import org.minh.userservice.model.request.UpdateUserRequest;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UserService {

    Users updateUser(UpdateUserRequest user) ;
    Users deleteUser(String email) ;
    Users getUserById(String email)  throws DataNotFoundException;
    List<Users> getAllUsers() ;
}
