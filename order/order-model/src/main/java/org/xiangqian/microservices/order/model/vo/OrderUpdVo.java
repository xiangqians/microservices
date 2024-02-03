package org.xiangqian.microservices.order.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.xiangqian.microservices.common.model.Vo;
import org.xiangqian.microservices.order.model.entity.OrderEntity;

import java.time.LocalDateTime;

/**
 * @author xiangqian
 * @date 21:21 2023/10/16
 */
@Data
@Schema(description = "修改订单信息")
public class OrderUpdVo extends OrderEntity implements Vo {

    // 不回显字段
    @JsonIgnore
    @Schema(hidden = true)
    @Override
    public Long getUserId() {
        return super.getUserId();
    }

    // 不回显字段
    @JsonIgnore
    @Schema(hidden = true)
    @Override
    public String getDel() {
        return super.getDel();
    }

    // 不回显字段
    @JsonIgnore
    @Schema(hidden = true)
    @Override
    public LocalDateTime getAddTime() {
        return super.getAddTime();
    }

    // 不回显字段
    @JsonIgnore
    @Schema(hidden = true)
    @Override
    public LocalDateTime getUpdTime() {
        return super.getUpdTime();
    }

}
