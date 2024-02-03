package org.xiangqian.microservices.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * @author xiangqian
 * @date 19:36 2024/02/01
 */
@Schema(description = "分页请求信息")
public class PageRequest extends Page implements Vo {

    // 不回显字段
    @JsonIgnore
    @Schema(hidden = true)
    @Override
    public long getTotal() {
        return super.getTotal();
    }

    // 不回显字段
    @JsonIgnore
    @Schema(hidden = true)
    @Override
    public List getData() {
        return super.getData();
    }

}
