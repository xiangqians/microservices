package org.xiangqian.microservices.user.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.xiangqian.microservices.common.model.Vo;
import org.xiangqian.microservices.user.model.entity.UserEntity;

/**
 * @author xiangqian
 * @date 21:11 2023/08/18
 */
@Data
@ToString(callSuper = true)
@Schema(description = "用户信息")
public class UserVo extends UserEntity implements Vo {

    // 不回显字段
    @JsonIgnore
    @Schema(hidden = true)
    @Override
    public String getPasswd() {
        return super.getPasswd();
    }

}
