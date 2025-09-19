package Library.Project.service.interfaces;

import Library.Project.dto.request.Authentication.AuthenticationRequest;
import Library.Project.dto.request.Authentication.IntrospectRequest;
import Library.Project.dto.request.Authentication.LogoutRequest;
import Library.Project.dto.response.AuthenticationResponse.AuthenticationResponse;
import Library.Project.dto.response.AuthenticationResponse.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface IAuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    IntrospectResponse introspectToken(IntrospectRequest request) throws JOSEException, ParseException;

    Object logout(LogoutRequest token) throws ParseException, JOSEException;

    Object removeOverDateToken();

}
