package org.xiangqian.microservices.order.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.xiangqian.microservices.common.model.Entity;
import org.xiangqian.microservices.common.model.validation.group.Add;
import org.xiangqian.microservices.common.model.validation.group.Page;
import org.xiangqian.microservices.common.model.validation.group.Upd;
import org.xiangqian.microservices.order.model.OrderCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单信息表
 *
 * @author xiangqian
 * @date 21:00 2023/09/11
 */
@Data
@TableName("`order`")
@Schema(description = "订单信息表")
public class OrderEntity implements Entity {

    private static final long serialVersionUID = 1L;

    @Max(value = Long.MAX_VALUE, message = OrderCode.ORDER_ID_MAX, groups = {Upd.class, Page.class})
    @Min(value = 1, message = OrderCode.ORDER_ID_MIN, groups = {Upd.class, Page.class})
    @NotNull(message = OrderCode.ORDER_ID_NOT_NULL, groups = {Upd.class})
    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Max(value = Long.MAX_VALUE, message = OrderCode.ORDER_USER_ID_MAX)
    @NotNull(message = OrderCode.ORDER_USER_ID_NOT_NULL)
    @Schema(description = "用户id")
    private Long userId;

    @Digits(integer = 6, fraction = 4, message = OrderCode.ORDER_AMOUNT_DIGITS, groups = {Add.class, Upd.class, Page.class})
    @Schema(description = "金额")
    private BigDecimal amount;

    @Size(max = 255, message = OrderCode.ORDER_REM_SIZE, groups = {Add.class, Upd.class, Page.class})
    @Schema(description = "备注")
    private String rem;

    @Schema(description = "删除标识，0-正常，1-已删除")
    @TableLogic
    private String del;

    @Schema(description = "创建时间")
    private LocalDateTime addTime;

    @Schema(description = "修改时间")
    private LocalDateTime updTime;

}