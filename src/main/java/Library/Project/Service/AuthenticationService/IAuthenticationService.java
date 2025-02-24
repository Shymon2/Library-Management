package Library.Project.Service.AuthenticationService;

import Library.Project.dto.Request.AuthenticationRequest;
import Library.Project.dto.Request.IntrospectRequest;
import Library.Project.dto.Request.LogoutRequest;
import Library.Project.dto.Response.AuthenticationResponse;
import Library.Project.dto.Response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface IAuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    IntrospectResponse introspectToken(IntrospectRequest request) throws JOSEException, ParseException;

    void logout(LogoutRequest token) throws ParseException, JOSEException;

    void removeOverDateToken();

}
