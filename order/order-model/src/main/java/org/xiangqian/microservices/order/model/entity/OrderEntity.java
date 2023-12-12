package org.xiangqian.microservices.order.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.xiangqian.microservices.common.model.Entity;

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

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键")
    private Integer id;

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "金额")
    private String amount;

    @Schema(description = "备注")
    private String rem;

    @TableLogic
    @Schema(description = "删除标识，0-正常，1-已删除")
    private String del;

    @Schema(description = "创建时间")
    private LocalDateTime addTime;

    @Schema(description = "修改时间")
    private LocalDateTime updTime;

}
