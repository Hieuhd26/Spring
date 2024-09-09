package hanu.devteria.service;

import com.nimbusds.jose.JWSObject;
import hanu.devteria.dto.request.AuthenticationRequest;
import hanu.devteria.dto.response.AuthenticationResponse;
import hanu.devteria.exception.AppException;
import hanu.devteria.exception.ErrorCode;
import hanu.devteria.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    UserRepository userRepository;

    //   public boolean authenticate(AuthenticationRequest request) {
//            var user = userRepository.findByName(request.getUsername())
//                    .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
//        return passwordEncoder.matches(request.getPassword(), user.getPassword());
//    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByName(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticated){
            throw  new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    private String generateToken(String name){
        JWSObject
    }

}
