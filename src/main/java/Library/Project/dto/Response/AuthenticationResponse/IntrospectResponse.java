package Library.Project.dto.Response.AuthenticationResponse;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class IntrospectResponse implements Serializable {
    private boolean valid;
}
