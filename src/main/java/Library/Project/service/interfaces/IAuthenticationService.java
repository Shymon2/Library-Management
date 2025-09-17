package Library.Project.service.interfaces;

import Library.Project.dto.Request.Authentication.AuthenticationRequest;
import Library.Project.dto.Request.Authentication.IntrospectRequest;
import Library.Project.dto.Request.Authentication.LogoutRequest;
import Library.Project.dto.Response.AuthenticationResponse.AuthenticationResponse;
import Library.Project.dto.Response.AuthenticationResponse.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface IAuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    IntrospectResponse introspectToken(IntrospectRequest request) throws JOSEException, ParseException;

    Object logout(LogoutRequest token) throws ParseException, JOSEException;

    Object removeOverDateToken();

}
