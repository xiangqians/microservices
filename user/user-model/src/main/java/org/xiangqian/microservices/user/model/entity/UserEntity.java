package org.xiangqian.microservices.user.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
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
@TableName("`user`")
@Schema(description = "用户信息表")
public class UserEntity implements Entity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键")
    private Integer id;

    @Schema(description = "昵称")
    private String nickname;

    @TableField("`name`")
    @Schema(description = "用户名")
    private String name;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "密码")
    private String passwd;

    @Schema(description = "备注")
    private String rem;

    @TableField("`lock`")
    @Schema(description = "锁定标记，0-正常，1-锁定")
    private String lock;

    @TableLogic
    @Schema(description = "删除标识，0-正常，1-已删除")
    private String del;

    @Schema(description = "创建时间")
    private LocalDateTime addTime;

    @Schema(description = "修改时间")
    private LocalDateTime updTime;

}
