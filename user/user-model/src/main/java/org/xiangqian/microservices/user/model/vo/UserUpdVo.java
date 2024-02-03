package org.xiangqian.microservices.user.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.xiangqian.microservices.common.model.Vo;
import org.xiangqian.microservices.user.model.entity.UserEntity;

import java.time.LocalDateTime;

/**
 * @author xiangqian
 * @date 21:45 2023/10/11
 */
@Data
@Schema(description = "更新用户信息")
public class UserUpdVo extends UserEntity implements Vo {

    // 不回显字段
    @JsonIgnore
    @Schema(hidden = true)
    @Override
    public String getLock() {
        return super.getLock();
    }

    // 不回显字段
    @JsonIgnore
    @Schema(hidden = true)
    @Override
    public String getDel() {
        return super.getDel();
    }

    // 不回显字段
    @JsonIgnore
    @Schema(hidden = true)
    @Override
    public LocalDateTime getAddTime() {
        return super.getAddTime();
    }

    // 不回显字段
    @JsonIgnore
    @Schema(hidden = true)
    @Override
    public LocalDateTime getUpdTime() {
        return super.getUpdTime();
    }

}
