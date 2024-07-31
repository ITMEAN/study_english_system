package org.minh.identityservice.service.user;

import org.minh.identityservice.entity.Users;
import org.minh.identityservice.exception.AlreadyExistException;
import org.minh.identityservice.exception.BadCredentialsException;
import org.minh.identityservice.exception.DataNotFoundException;
import org.minh.identityservice.model.request.CreateUserRequest;
import org.minh.identityservice.model.request.UpdateUserRequest;
import org.minh.identityservice.model.response.UserResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UserService {
    Users createUser(CreateUserRequest user) throws AlreadyExistException, IllegalArgumentException,DataNotFoundException;
    Users updateUser(UpdateUserRequest user) ;
    Users deleteUser(String email) ;
    Users getUserById(String email)  throws DataNotFoundException;
    List<Users> getAllUsers() ;
    boolean isUserExist(String email) ;

    UserResponseDTO getUserByEmail(String email, String token) throws BadCredentialsException;
}
