package Library.Project.service.implement;

import Library.Project.dto.POJO.BookGetFromApi;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookGetFromApiService {
    private final RestTemplate restTemplate;

    public List<BookGetFromApi> getBook() {
        String uri = "https://www.googleapis.com/books/v1/volumes?q=java";
        ResponseEntity<List<BookGetFromApi>> response = restTemplate.exchange(uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BookGetFromApi>>() {});
        return response.getBody();
    }
}
