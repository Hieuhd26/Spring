package hanu.devteria.service;

import hanu.devteria.dto.request.UserCreationRequest;
import hanu.devteria.dto.request.UserUpdateRequest;
import hanu.devteria.dto.response.UserResponse;
import hanu.devteria.enums.Role;
import hanu.devteria.exception.AppException;
import hanu.devteria.exception.ErrorCode;
import hanu.devteria.mapper.UserMapper;
import hanu.devteria.model.User;
import hanu.devteria.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
// làm như trên trên thì nó tự tạo construct và tiêm vào
public class UserService {
    private UserRepository userRepository;

    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(UserCreationRequest request) {

        if (userRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
 //       user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return user;

    }

    @PreAuthorize("hasRole('ADMIN')") // tao aop boc ben ngoai
    // kiem tra role roi moi kiem tra
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    @PostAuthorize("returnObject.name==authentication.name")
    // thuc hien xong moi kiem tra role
    public UserResponse findUserById(int id) {
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new RuntimeException("User with id " + id + " not found")));
    }


    public UserResponse updateUserById(int id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
        userMapper.updateUser(user, request);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }

    public UserResponse getInfor() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByName(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        return userMapper.toUserResponse(user);

    }
}
