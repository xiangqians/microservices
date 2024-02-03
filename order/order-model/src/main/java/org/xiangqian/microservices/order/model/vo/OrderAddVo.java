package org.xiangqian.microservices.order.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xiangqian
 * @date 21:21 2023/10/16
 */
@Data
@Schema(description = "新增订单信息")
public class OrderAddVo extends OrderUpdVo {

    // 不回显字段
    @JsonIgnore
    @Schema(hidden = true)
    @Override
    public Long getId() {
        return super.getId();
    }

}
