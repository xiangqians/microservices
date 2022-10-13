package org.microservices.common.core.pagination;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.microservices.common.core.enumeration.Enum;

import java.util.Arrays;
import java.util.Objects;

/**
 * 排序命令
 *
 * @author xiangqian
 * @date 23:15 2022/06/14
 */
@Getter
@Schema(description = "排序命令")
public enum SortOrder implements Enum<Integer> {
    UNSORTED(0, "未分类"),
    ASCENDING(1, "升序"),
    DESCENDING(2, "降序"),
    ;

    private final Integer value;
    private final String description;

    SortOrder(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public static SortOrder of(Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }

        return Arrays.stream(SortOrder.values())
                .filter(sortOrder -> sortOrder.value.equals(value))
                .findFirst()
                .orElse(null);
    }

}
