package Library.Project.service.interfaces;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface ClientTemplate {
    <R> ResponseEntity<R> get(String uri, HttpHeaders headers, ParameterizedTypeReference<R> response);

    <R> ResponseEntity<R> post(String uri, HttpHeaders headers, ParameterizedTypeReference<R> response);
}
