package org.xiangqian.microservices.order.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.xiangqian.microservices.common.model.Vo;
import org.xiangqian.microservices.order.model.OrderCode;

/**
 * @author xiangqian
 * @date 21:21 2023/10/16
 */
@Data
@Schema(description = "新增订单信息")
public class OrderAddVo implements Vo {

    @Length(max = 15, message = "金额不能超过15个字符")
    @NotBlank(message = OrderCode.AMOUNT_NOT_EMPTY)
    @Schema(description = "金额")
    private String amount;

    @Length(max = 250, message = "备注不能超过250个字符")
    @Schema(description = "备注")
    private String rem;

    public void setAmount(String amount) {
        this.amount = StringUtils.trim(amount);
    }

    public void setRem(String rem) {
        this.rem = StringUtils.trim(rem);
    }

    @Override
    public void validate(Class<?>... groups) throws Exception {
    }

}
