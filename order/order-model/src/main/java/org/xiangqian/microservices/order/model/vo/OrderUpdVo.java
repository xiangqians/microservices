package org.xiangqian.microservices.order.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author xiangqian
 * @date 21:21 2023/10/16
 */
@Data
@Schema(description = "修改订单信息")
public class OrderUpdVo extends OrderAddVo {

    @Schema(description = "主键")
    @NotNull(message = "主键id不能为空")
    @Min(value = 1, message = "主键id必须大于0")
    private Long id;

}
