package Library.Project.controller;

import Library.Project.dto.GeneralPayload;
import Library.Project.service.RestfulResponseFactory;
import Library.Project.dto.request.Authentication.AuthenticationRequest;
import Library.Project.dto.request.Authentication.IntrospectRequest;
import Library.Project.dto.request.Authentication.LogoutRequest;
import Library.Project.dto.response.AuthenticationResponse.AuthenticationResponse;
import Library.Project.dto.response.AuthenticationResponse.IntrospectResponse;
import Library.Project.service.interfaces.IAuthenticationService;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@SecurityRequirements
@RequestMapping("/${base.app.context}/public/api/v1/auth")
@Tag(name = "Authentication Controller")
public class AuthenticationController {
    private final IAuthenticationService authenticationService;

    @Operation(summary = "Log in by username and password", description = "Return a token that need to save")
    @PostMapping("/login")
    public ResponseEntity<GeneralPayload<AuthenticationResponse>> logIn(@RequestBody AuthenticationRequest request){
        return RestfulResponseFactory.of(authenticationService.authenticate(request));
    }

    @Operation(summary = "Verify token")
    @PostMapping("/introspect")
    public ResponseEntity<GeneralPayload<IntrospectResponse>> introspectToken(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        return RestfulResponseFactory.of(authenticationService.introspectToken(request));
    }

    @Operation(summary = "Log out")
    @PostMapping("/logout")
    public ResponseEntity<GeneralPayload<Object>> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        return RestfulResponseFactory.of(authenticationService.logout(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Clean data of token db")
    @DeleteMapping("/remove-invalid-token")
    public ResponseEntity<GeneralPayload<Object>> refreshDb(){
        return RestfulResponseFactory.of(authenticationService.removeOverDateToken());
    }
}
