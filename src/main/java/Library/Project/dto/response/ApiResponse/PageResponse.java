package Library.Project.dto.response.ApiResponse;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageResponse<T> {
    private int pageNo;
    private int pageSize;
    private long totalPage;
    private T items;
}
