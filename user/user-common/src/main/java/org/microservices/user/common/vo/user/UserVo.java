package org.microservices.user.common.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.microservices.common.core.o.Vo;

import java.time.LocalDateTime;

/**
 * @author xiangqian
 * @date 10:42:33 2022/04/03
 */
@Data
@Schema(description = "用户信息")
public class UserVo implements Vo {

    @Schema(description = "用户id")
    private Long id;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "描述")
    private String desc;

    @Schema(description = "锁定标记，0-正常，1-锁定")
    private Integer lockFlag;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

}
