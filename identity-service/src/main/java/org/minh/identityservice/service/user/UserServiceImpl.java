package org.minh.identityservice.service.user;

import lombok.RequiredArgsConstructor;
import org.minh.identityservice.entity.Role;
import org.minh.identityservice.entity.Users;
import org.minh.identityservice.exception.AlreadyExistException;
import org.minh.identityservice.exception.BadCredentialsException;
import org.minh.identityservice.exception.DataNotFoundException;
import org.minh.identityservice.httpclient.UserClient;
import org.minh.identityservice.jwt.JwtUtil;
import org.minh.identityservice.mapper.UserMapper;
import org.minh.identityservice.model.request.CreateUserRequest;
import org.minh.identityservice.model.request.UpdateUserRequest;
import org.minh.identityservice.model.response.UserResponseDTO;
import org.minh.identityservice.repository.RoleRepository;
import org.minh.identityservice.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private final UsersRepository usersRepository;
    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    private final UserMapper userMapper;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final RestTemplate restTemplate;
    @Autowired
    private final UserClient userClient;
    @Autowired
    private final JwtUtil jwtUtil;

    @Override
    public Users createUser(CreateUserRequest request) throws AlreadyExistException, IllegalArgumentException, DataNotFoundException {
        Optional<Users> users = usersRepository.findByEmail(request.getEmail());
        //check exist and active
        if (users.isPresent()) {
            if (users.get().isActive()) {
                throw new AlreadyExistException("User already exists");
            } else {
                usersRepository.deleteById(users.get().getEmail());
            }
        }
        if (!request.getRePassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        Role role = roleRepository.findById(2).orElseThrow(() -> new DataNotFoundException("Role not found"));
        Users user = userMapper.toUser(request);
        user.setFullName(request.getFullName());
        user.setActive(false);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return usersRepository.save(user);
    }


    @Override
    public Users updateUser(UpdateUserRequest user) throws DataNotFoundException {
        Users users = usersRepository.findById(user.getEmail()).orElseThrow(() -> new DataNotFoundException("Email not found"));
        users.setFullName(user.getFullName());
        users.setPhone(user.getPhone());
        users.setLob(user.getLob());
        return usersRepository.save(users);
    }

    @Override
    public Users deleteUser(String email) throws DataNotFoundException {
        Users users = usersRepository.findById(email).orElseThrow(() -> new DataNotFoundException("Email not found"));
        users.setActive(false);
        return usersRepository.save(users);
    }

    @Override
    public Users getUserById(String id) throws DataNotFoundException {
        return usersRepository.findByEmail(id).orElseThrow(() -> new DataNotFoundException("Email not found"));
    }

    @Override
    public List<Users> getAllUsers() {
        return userClient.getAllUsers();
    }

    @Override
    public boolean isUserExist(String email) {
        return usersRepository.existsById(email);
    }


    @Override
    public UserResponseDTO getUserByEmail(String email, String authorization) throws BadCredentialsException, DataNotFoundException {
        System.err.println("email = " + email);
       try{

           String  token = authorization.replace("Bearer ", "");
           String emailSubject = jwtUtil.getSubject(token);
           if (!emailSubject.equals(email)) {
               throw new BadCredentialsException("Token is invalid");
           }
           Users users = usersRepository.findById(email).orElseThrow(() -> new DataNotFoundException("Email not found"));
           return userMapper.toUserResponseDTO(users);
       }catch (Exception e){
           System.err.println(e.getMessage());
           throw new BadCredentialsException("Token is invalid");
       }

    }


}
