package org.xiangqian.microservices.order.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.xiangqian.microservices.common.model.Vo;
import org.xiangqian.microservices.order.model.entity.OrderEntity;

/**
 * @author xiangqian
 * @date 21:00 2023/09/11
 */
@Data
@Schema(description = "订单信息")
public class OrderVo extends OrderEntity implements Vo {

    // 不回显字段
    @JsonIgnore
    @Schema(hidden = true)
    @Override
    public String getDel() {
        return super.getDel();
    }

}
