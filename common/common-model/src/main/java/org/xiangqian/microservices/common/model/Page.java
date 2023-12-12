package org.xiangqian.microservices.common.model;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xiangqian
 * @date 00:53 2022/06/12
 */
@Schema(description = "分页信息")
public class Page<T> implements IPage<T> {

    @Schema(description = "当前页", example = "1", required = true)
    private long current;

    @Schema(description = "页数量", example = "10", required = true)
    private long size;

    @Schema(description = "总数")
    private long total;

    @Getter
    @Setter
    @Schema(description = "数据")
    private List<T> data;

    public Page() {
        this(1L, 10L);
    }

    public Page(long current, long size) {
        this.current = current;
        this.size = size;
        this.total = 0L;
        this.data = Collections.emptyList();
    }

    /**
     * page类型转换（T -> R）
     *
     * @param func
     * @param <R>
     * @return
     */
    public <R> Page<R> conv(Function<T, R> func) {
        Page<R> page = new Page<>();
        page.setCurrent(getCurrent());
        page.setSize(getSize());
        page.setTotal(getTotal());
        page.setData(Optional.ofNullable(getData())
                .filter(CollectionUtils::isNotEmpty)
                .map(data -> data.stream().map(func::apply).collect(Collectors.toList()))
                .orElse(Collections.emptyList()));
        return page;
    }

    @JsonIgnore
    @Override
    public List<OrderItem> orders() {
        return Collections.emptyList();
    }

    @JsonIgnore
    @Override
    public boolean optimizeCountSql() {
        return IPage.super.optimizeCountSql();
    }

    @JsonIgnore
    @Override
    public boolean optimizeJoinOfCountSql() {
        return IPage.super.optimizeJoinOfCountSql();
    }

    @JsonIgnore
    @Override
    public boolean searchCount() {
        return IPage.super.searchCount();
    }

    @JsonIgnore
    @Override
    public long offset() {
        return IPage.super.offset();
    }

    @JsonIgnore
    @Override
    public Long maxLimit() {
        return IPage.super.maxLimit();
    }

    @JsonIgnore
    @Override
    public long getPages() {
        return IPage.super.getPages();
    }

    @JsonIgnore
    @Override
    public IPage<T> setPages(long pages) {
        return IPage.super.setPages(pages);
    }

    @JsonIgnore
    @Override
    public List<T> getRecords() {
        return null;
    }

    @JsonIgnore
    @Override
    public IPage<T> setRecords(List<T> records) {
        this.data = records;
        return this;
    }

    @Override
    public long getTotal() {
        return total;
    }

    @Override
    public IPage<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public IPage<T> setSize(long size) {
        this.size = size;
        return this;
    }

    @Override
    public long getCurrent() {
        return current;
    }

    @Override
    public IPage<T> setCurrent(long current) {
        this.current = current;
        return this;
    }

    @JsonIgnore
    @Override
    public String countId() {
        return IPage.super.countId();
    }

}
