package Library.Project.service.implement;

import Library.Project.configuration.Translator;
import Library.Project.constant.RolePermissions;
import Library.Project.entity.InvalidatedToken;
import Library.Project.entity.Role;
import Library.Project.entity.User;
import Library.Project.constant.enums.ErrorCodeFail;
import Library.Project.exception.AppException;
import Library.Project.repository.InvalidatedTokenRepository;
import Library.Project.service.interfaces.IAuthenticationService;
import Library.Project.dto.request.Authentication.AuthenticationRequest;
import Library.Project.dto.request.Authentication.IntrospectRequest;
import Library.Project.dto.request.Authentication.LogoutRequest;
import Library.Project.dto.response.AuthenticationResponse.AuthenticationResponse;
import Library.Project.dto.response.AuthenticationResponse.IntrospectResponse;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService implements IAuthenticationService {
    private final UserService userService;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final RoleService roleService;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userService.findUserByUserName(request.getUsername());
        if(user == null)
            throw new AppException(ErrorCodeFail.USERNAME_PASSWORD_INCORRECT);

        PasswordEncoder encoder = new BCryptPasswordEncoder(10);

        boolean auth = encoder.matches(request.getPassword(), user.getPassword());

        if (!auth){
            throw new RuntimeException(Translator.toLocale("username.password.incorrect"));
        }

        String token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    @Override
    public IntrospectResponse introspectToken(IntrospectRequest request) throws JOSEException, ParseException {
        String token = request.getToken();
        boolean status = true;

        try{
            verifier(token);
        } catch (RuntimeException e){
            status = false;
        }

        return IntrospectResponse.builder()
                .valid(status)
                .build();
    }

    @Override
    public Objects logout(LogoutRequest request) throws ParseException, JOSEException {

        SignedJWT signToken = verifier(request.getToken());

        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder()
                        .id(jit)
                        .expiryTime(expiryTime)
                        .build();

        invalidatedTokenRepository.save(invalidatedToken);

        return null;
    }

    @Override
    public Objects removeOverDateToken(){
        List<InvalidatedToken> invalidatedTokens = invalidatedTokenRepository.findAll();

        invalidatedTokens.forEach(a -> {
            if(a.getExpiryTime().after(new Date()))
                invalidatedTokens.remove(a);
        });

        return null;
    }


    private String generateToken(User user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("library.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(Translator.toLocale("authentication.token.fail"));
        }
    }

    private SignedJWT verifier(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verified = signedJWT.verify(verifier);

        if(!expiryTime.after(new Date()))
            throw new RuntimeException(Translator.toLocale("authentication.verify.fail"));

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new RuntimeException(Translator.toLocale("authentication.logout.already"));

        return signedJWT;
    }

    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(user.getRole() != null){
            stringJoiner.add("ROLE_" + user.getRole());
            if(Objects.equals(user.getRole(), "ADMIN")) {
                Arrays.stream(RolePermissions.admin_permission).forEach(stringJoiner::add);
            } else if(Objects.equals(user.getRole(), "LIBRARIAN")){
                Arrays.stream(RolePermissions.librarian_permission).forEach(stringJoiner::add);
            } else if (Objects.equals(user.getRole(), "MANAGER")) {
                Arrays.stream(RolePermissions.manager_permission).forEach(stringJoiner::add);
            } else if (Objects.equals(user.getRole(), "USER")) {
                Arrays.stream(RolePermissions.user_permission).forEach(stringJoiner::add);
            } else {
                if(roleService.checkExits(user.getRole()))
                    throw new AppException(ErrorCodeFail.NOT_FOUND);
                else{
                    Role role = roleService.findByName(user.getRole());
                    role.getPermissions().forEach(a -> stringJoiner.add(a.getName()));
                }
            }
        }
        else throw new AppException(ErrorCodeFail.NOT_FOUND);

        return stringJoiner.toString();
    }
}
