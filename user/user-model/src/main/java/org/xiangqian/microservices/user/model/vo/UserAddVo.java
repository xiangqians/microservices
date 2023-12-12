package org.xiangqian.microservices.user.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.xiangqian.microservices.common.model.Vo;
import org.xiangqian.microservices.user.model.UserCode;

/**
 * @author xiangqian
 * @date 21:45 2023/10/11
 */
@Data
@Schema(description = "新增用户信息")
public class UserAddVo implements Vo {

    @Length(max = 60, message = "昵称不能超过60个字符")
    @Schema(description = "昵称")
    private String nickname;

    @Length(max = 60, message = "用户名不能超过60个字符")
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名", required = true)
    private String name;

    @Length(max = 60, message = "手机号不能超过60个字符")
    @NotBlank(message = UserCode.PHONE_NOT_EMPTY)
    @Schema(description = "手机号", required = true)
    private String phone;

    @Length(max = 60, message = "邮箱不能超过60个字符")
    @NotBlank(message = "邮箱不能为空")
    @Schema(description = "邮箱", required = true)
    private String email;

    @Length(max = 60, message = "密码不能超过60个字符")
    @Schema(description = "密码", required = true)
    private String passwd;

    @Length(max = 250, message = "备注不能超过250个字符")
    @Schema(description = "备注")
    private String rem;

    public void setNickname(String nickname) {
        this.nickname = StringUtils.trim(nickname);
    }

    public void setName(String name) {
        this.name = StringUtils.trim(name);
    }

    public void setPhone(String phone) {
        this.phone = StringUtils.trim(phone);
    }

    public void setEmail(String email) {
        this.email = StringUtils.trim(email);
    }

    public void setPasswd(String passwd) {
        this.passwd = StringUtils.trim(passwd);
    }

    public void setRem(String rem) {
        this.rem = StringUtils.trim(rem);
    }

    @Override
    public void validate(Class<?>... groups) throws Exception {
    }

}
