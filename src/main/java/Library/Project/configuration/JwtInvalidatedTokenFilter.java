package Library.Project.configuration;

import Library.Project.dto.response.ApiResponse.ResponseError;
import Library.Project.constant.enums.ErrorCodeFail;
import Library.Project.exception.AppException;
import Library.Project.repository.InvalidatedTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;

@Component
@RequiredArgsConstructor
public class JwtInvalidatedTokenFilter extends OncePerRequestFilter {

    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authRequest = request.getHeader("Authorization");

        if (authRequest != null && authRequest.startsWith("Bearer ")) {
            try {
                SignedJWT signedJWT = SignedJWT.parse(authRequest.substring(7));
                if (!invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
                    filterChain.doFilter(request, response);
                } else {

                    ErrorCodeFail errorCodeFail = ErrorCodeFail.UNAUTHENTICATED;
                    response.setStatus(errorCodeFail.getStatusCode().value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    ObjectMapper objectMapper = new ObjectMapper();
                    response.getWriter().write(objectMapper.writeValueAsString(
                            new ResponseError(errorCodeFail.getCode(), errorCodeFail.getMessage())));
                    response.flushBuffer();
                }
            } catch (ParseException e) {
                throw new AppException(ErrorCodeFail.TOKEN_INVALID);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }



}
