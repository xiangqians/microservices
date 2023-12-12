package org.xiangqian.microservices.user.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author xiangqian
 * @date 21:45 2023/10/11
 */
@Data
@Schema(description = "更新用户信息")
public class UserUpdVo extends UserAddVo {

    @Schema(description = "主键")
    @NotNull(message = "主键id不能为空")
    @Min(value = 1, message = "主键id必须大于0")
    private Long id;

}
