package Library.Project.Controller;

import Library.Project.Configuration.Translator;
import Library.Project.Service.AuthenticationService.AuthenticationService;
import Library.Project.dto.Request.AuthenticationRequest;
import Library.Project.dto.Request.IntrospectRequest;
import Library.Project.dto.Response.AuthenticationResponse;
import Library.Project.dto.Response.IntrospectResponse;
import Library.Project.dto.Response.ResponseData;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication Controller")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "Log in by username and password", description = "Return a token that need to save")
    @PostMapping("/logIn")
    public ResponseData<AuthenticationResponse> logIn(@RequestBody AuthenticationRequest request){
        return new ResponseData<>(1000, Translator.toLocale("authentication.done"), authenticationService.authenticate(request));
    }

    @Operation(summary = "Verify token")
    @PostMapping("/introspect")
    public ResponseData<IntrospectResponse> introspectToken(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        return new ResponseData<>(1000, Translator.toLocale("introspect.done"), authenticationService.introspectToken(request));
    }
}
