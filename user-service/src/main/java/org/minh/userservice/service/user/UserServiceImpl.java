package org.minh.userservice.service.user;

import lombok.RequiredArgsConstructor;
import org.minh.userservice.entity.Users;
import org.minh.userservice.exception.DataNotFoundException;
import org.minh.userservice.mapper.UserMapper;
import org.minh.userservice.model.request.CreateUserRequest;
import org.minh.userservice.model.request.UpdateUserRequest;
import org.minh.userservice.repository.RoleRepository;
import org.minh.userservice.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private final UsersRepository usersRepository;
    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    private final UserMapper userMapper;

    @Override
    public Users updateUser(UpdateUserRequest user) throws DataNotFoundException {
        Users users = usersRepository.findById(user.getEmail()).orElseThrow(()->new DataNotFoundException("Email not found"));
        users.setFirstName(user.getFirstName());
        users.setLastName(user.getLastName());
        users.setPhone(user.getPhone());
        users.setLob(user.getLob());
        return usersRepository.save(users);
    }

    @Override
    public Users deleteUser(String email) throws DataNotFoundException {
        Users users = usersRepository.findById(email).orElseThrow(()->new DataNotFoundException("Email not found"));
        users.setActive(false);
        return usersRepository.save(users);
    }

    @Override
    public Users getUserById(String id)  throws DataNotFoundException {
        return usersRepository.findByEmail(id).orElseThrow(()->new DataNotFoundException("Email not found"));
    }

    @Override
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }



}
