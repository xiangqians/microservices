package org.microservices.common.core.pagination;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 分页参数
 *
 * @author xiangqian
 * @date 14:41:59 2022/06/12
 */
@Data
@Schema(description = "分页参数信息")
public class PageRequest {

    @Max(value = 100, message = "页数不能超过10000")
    @Min(value = 1, message = "页数不能小于0")
    @NotNull(message = "当前页不能为空")
    @Schema(description = "当前页", example = "1")
    private Long current;

    @Max(value = 100, message = "页数量不能超过100")
    @Min(value = 1, message = "页数量不能小于0")
    @NotNull(message = "当前页不能为空")
    @Schema(description = "页数量", example = "10")
    private Long size;

    @Schema(description = "排序属性")
    private List<String> sortProperty;

    @Schema(description = "排序命令")
    private SortOrder sortOrder;

    public PageRequest() {
        this(1L, 10L);
    }

    public PageRequest(long current, long size) {
        this.current = current;
        this.size = size;
    }

}
