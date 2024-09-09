package hanu.devteria.service;

import hanu.devteria.dto.request.UserCreationRequest;
import hanu.devteria.dto.request.UserUpdateRequest;
import hanu.devteria.dto.response.UserResponse;
import hanu.devteria.exception.AppException;
import hanu.devteria.exception.ErrorCode;
import hanu.devteria.mapper.UserMapper;
import hanu.devteria.model.User;
import hanu.devteria.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;

    private UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository,UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public User createUser(UserCreationRequest request) {

        if(userRepository.existsByName(request.getName())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return user;

    }

    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    public UserResponse findUserById(int id) {
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new RuntimeException("User with id " + id + " not found")));
    }


    public UserResponse updateUserById(int id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
        userMapper.updateUser(user,request);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }
}
