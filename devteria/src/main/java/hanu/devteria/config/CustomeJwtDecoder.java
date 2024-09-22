package hanu.devteria.config;

import com.nimbusds.jose.JOSEException;
import hanu.devteria.dto.request.IntrospecRequest;
import hanu.devteria.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomeJwtDecoder implements JwtDecoder {

    @Autowired
    private AuthenticationService authenticationService;

    @Value("${signer.key}")
    private String signerKey;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    //chịu trách nhiệm veridy token có hợp lệ hay không
    @Override
    public Jwt decode(String token) throws JwtException {
        // check token co con hieu luc hay ko
        try {
          var response=  authenticationService.introspect(IntrospecRequest.builder()
                    .token(token)
                    .build());
          // sao lại thêm  if, không thêm thì token logout vẫn sd dc
          if(!response.isValid()){
              throw  new JwtException("Token invalid");
          }
        } catch (ParseException | JOSEException e) {
            throw new JwtException(e.getMessage());
        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        return nimbusJwtDecoder.decode(token);
    }
}
