package Library.Project.controller;

import Library.Project.configuration.Translator;
import Library.Project.service.implement.AuthenticationService;
import Library.Project.dto.Request.AuthenticationRequest;
import Library.Project.dto.Request.IntrospectRequest;
import Library.Project.dto.Request.LogoutRequest;
import Library.Project.dto.Response.AuthenticationResponse;
import Library.Project.dto.Response.IntrospectResponse;
import Library.Project.dto.Response.ResponseData;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@SecurityRequirements
@RequestMapping("/auth")
@Tag(name = "Authentication Controller")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "Log in by username and password", description = "Return a token that need to save")
    @PostMapping("/login")
    public ResponseData<AuthenticationResponse> logIn(@RequestBody AuthenticationRequest request){
        return new ResponseData<>(1000, Translator.toLocale("authentication.done"), authenticationService.authenticate(request));
    }

    @Operation(summary = "Verify token")
    @PostMapping("/introspect")
    public ResponseData<IntrospectResponse> introspectToken(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        return new ResponseData<>(1000, Translator.toLocale("introspect.done"), authenticationService.introspectToken(request));
    }

    @Operation(summary = "Log out")
    @PostMapping("/logout")
    public ResponseData<String> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return new ResponseData<>(1000, Translator.toLocale("authentication.logout.success"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Clean data of token db")
    @DeleteMapping("/remove-invalid-token")
    public ResponseData<String> refreshDb(){
        authenticationService.removeOverDateToken();
        return new ResponseData<>(1000, Translator.toLocale("authentication.remove.token"));
    }
}
