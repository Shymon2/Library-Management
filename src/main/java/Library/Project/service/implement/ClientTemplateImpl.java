package Library.Project.service.implement;

import Library.Project.service.interfaces.ClientTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ClientTemplateImpl implements ClientTemplate {
    private final RestTemplate restTemplate;

    public ClientTemplateImpl(@Qualifier("restConfig") RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public <R> ResponseEntity<R> get(String uri, HttpHeaders headers, ParameterizedTypeReference<R> response) {
        return exchangeParameterized(uri, HttpMethod.GET, new HttpEntity<>(headers), response);
    }

    @Override
    public <R> ResponseEntity<R> post(String uri, HttpHeaders headers, ParameterizedTypeReference<R> response) {
        return exchangeParameterized(uri, HttpMethod.POST, new HttpEntity<>(headers), response);
    }

    private <R> ResponseEntity<R> exchangeParameterized(String url, HttpMethod httpMethod, HttpEntity<Object> entity, ParameterizedTypeReference<R> responseClass) {
        return restTemplate.exchange(url, httpMethod, entity, responseClass);
    }
}
