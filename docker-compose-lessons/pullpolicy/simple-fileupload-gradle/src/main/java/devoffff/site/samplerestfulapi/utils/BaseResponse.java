package devoffff.site.samplerestfulapi.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    private T payload;

    @Builder.Default
    private String localDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    private String message;
    private int status;

    // Pagination information
    private Long totalElements;
    private Integer totalPages;
    private Integer currentPage;
    private Integer pageSize;

    // Custom builder method for pagination
//    public static class BaseResponseBuilder<T> {
//        public BaseResponseBuilder<T> page(Page<?> page) {
//            if (page != null) {
//                this.totalElements = page.getTotalElements();
//                this.totalPages = page.getTotalPages();
//                this.currentPage = page.getNumber();
//                this.pageSize = page.getSize();
//            }
//            return this;
//        }
//    }
}

