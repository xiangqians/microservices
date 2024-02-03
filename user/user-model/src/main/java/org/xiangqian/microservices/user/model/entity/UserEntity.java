package org.xiangqian.microservices.user.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.xiangqian.microservices.common.model.Entity;

import java.time.LocalDateTime;

/**
 * 用户信息表
 *
 * @author xiangqian
 * @date 21:00 2023/09/05
 */
@Data
@TableName("user")
@Schema(description = "用户信息表")
public class UserEntity implements Entity {

    private static final long serialVersionUID = 1L;

    @Max(value = Long.MAX_VALUE, message = "【主键】不能大于" + Long.MAX_VALUE)
    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Size(max = 60, message = "【昵称】长度不能大于60个字符")
    @Schema(description = "昵称")
    private String nickname;

    @Size(max = 60, message = "【用户名】长度不能大于60个字符")
    @NotNull(message = "【用户名】不能为空")
    @Schema(description = "用户名")
    @TableField("`name`")
    private String name;

    @Size(max = 60, message = "【手机号】长度不能大于60个字符")
    @Schema(description = "手机号")
    private String phone;

    @Size(max = 60, message = "【邮箱】长度不能大于60个字符")
    @Schema(description = "邮箱")
    private String email;

    @Size(max = 128, message = "【密码】长度不能大于128个字符")
    @NotNull(message = "【密码】不能为空")
    @Schema(description = "密码")
    private String passwd;

    @Size(max = 255, message = "【备注】长度不能大于255个字符")
    @Schema(description = "备注")
    private String rem;

    @Schema(description = "锁定标记，0-正常，1-锁定")
    @TableField("`lock`")
    private String lock;

    @Schema(description = "删除标识，0-正常，1-已删除")
    @TableLogic
    private String del;

    @Schema(description = "创建时间")
    private LocalDateTime addTime;

    @Schema(description = "修改时间")
    private LocalDateTime updTime;

}
