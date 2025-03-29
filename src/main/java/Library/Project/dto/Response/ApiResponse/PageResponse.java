package Library.Project.dto.Response.ApiResponse;

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
