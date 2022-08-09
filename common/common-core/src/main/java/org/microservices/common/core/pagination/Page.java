package org.microservices.common.core.pagination;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.microservices.common.core.util.Optional;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xiangqian
 * @date 00:53 2022/06/12
 */
@Data
@Schema(description = "分页信息")
public class Page<T> {

    @Schema(description = "当前页")
    private Long current;

    @Schema(description = "当前页数量")
    private Long size;

    @Schema(description = "总页数")
    private Long pages;

    @Schema(description = "总数")
    private Long total;

    @Schema(description = "数据")
    private List<T> data;

    public Page() {
        this(1L, 10L);
    }

    public Page(long current, long size) {
        this.current = current;
        this.size = size;
        this.pages = 0L;
        this.total = 0L;
        this.data = Collections.emptyList();
    }

    public <R> Page<R> convert(Function<T, R> function) {
        Page<R> page = new Page<>();
        page.setCurrent(getCurrent());
        page.setSize(getSize());
        page.setPages(getPages());
        page.setTotal(getTotal());
        page.setData(Optional.ofNullable(getData()).map(data -> data.stream().map(function::apply).collect(Collectors.toList())).orElse(Collections.emptyList()));
        return page;
    }

}
