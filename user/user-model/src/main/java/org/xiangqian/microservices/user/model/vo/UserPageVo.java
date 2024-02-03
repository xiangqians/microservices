package org.xiangqian.microservices.user.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.xiangqian.microservices.common.model.Vo;
import org.xiangqian.microservices.user.model.entity.UserEntity;

/**
 * @author xiangqian
 * @date 10:32 2024/02/02
 */
@Data
@ToString(callSuper = true)
@Schema(description = "用户分页信息")
public class UserPageVo extends UserEntity implements Vo {

    // 不回显字段
    @JsonIgnore
    @Schema(hidden = true)
    @Override
    public String getPasswd() {
        return super.getPasswd();
    }

}
