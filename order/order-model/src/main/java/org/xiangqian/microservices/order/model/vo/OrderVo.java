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

    @JsonIgnore // 不回显del字段
    @Override
    public String getDel() {
        return super.getDel();
    }

    @Override
    public void validate(Class<?>... groups) throws Exception {
    }

}
