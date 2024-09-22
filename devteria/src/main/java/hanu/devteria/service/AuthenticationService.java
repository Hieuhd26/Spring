package hanu.devteria.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import hanu.devteria.dto.request.AuthenticationRequest;
import hanu.devteria.dto.request.IntrospecRequest;
import hanu.devteria.dto.request.LogoutRequest;
import hanu.devteria.dto.request.RefreshRequest;
import hanu.devteria.dto.response.AuthenticationResponse;
import hanu.devteria.dto.response.IntrospecResponse;
import hanu.devteria.exception.AppException;
import hanu.devteria.exception.ErrorCode;
import hanu.devteria.model.InvalidateToken;
import hanu.devteria.model.User;
import hanu.devteria.repository.InvalidateTokenRepository;
import hanu.devteria.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    @NonFinal // ko inject vao contructor
    @Value("${signer.key}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${validDuration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${refreshableDuration}")
    protected long REFRESHABLE_DUARTION;

    UserRepository userRepository;
    InvalidateTokenRepository invalidateTokenRepository;

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
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {

        try {
            // đọc thông tin toke n lấy toke id, expery time
            // logout ng dung van co the dung token cu de refresh
            var signToken = verifyToken(request.getToken(), true);
            String jwtId = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();
            InvalidateToken invalidateToken = InvalidateToken.builder()
                    .id(jwtId)
                    .expiryTime(expiryTime)
                    .build();
            invalidateTokenRepository.save(invalidateToken);
        } catch (AppException e) {
            log.info("Token already expired");
        }


    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        // kiem tra hieu luc
        var signJWT = verifyToken(request.getToken(), true);

        //
        var TokenId = signJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();

        InvalidateToken invalidateToken = InvalidateToken.builder()
                .id(TokenId)
                .expiryTime(expiryTime)
                .build();
        invalidateTokenRepository.save(invalidateToken);

        var username = signJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByName(username).orElseThrow(
                () -> new AppException(ErrorCode.UNAUTHENTICATED)
        );
        // neu user co thay doi name, permission cung dươc cacp nhat luon
        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();


    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date exporyTime = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESHABLE_DUARTION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);
// nêu token bị sửa đổi hoặc hết hạn
        if (!(verified && exporyTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (invalidateTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }

    private String generateToken(User user) {
        // tạo header cần thuật toán
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // tạo body
        // data trong body là claims
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getName())// user dang nhap
                .issuer("hieu.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString()) //THÊM ID CHO TOKEN
                .claim("scope", buildScope(user)) // custom claim
                .build();
        // tạo từ jwtClaimsSet
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        // khi có header và payload thì tạo signature
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            // kí
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token");
            throw new RuntimeException(e);
        }
    }

    public IntrospecResponse introspect(IntrospecRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;
        // kt het han
        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospecResponse.builder()
                .valid(isValid)
                .build();

    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });
        }
        return stringJoiner.toString();
    }

}
