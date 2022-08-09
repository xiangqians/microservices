package org.microservices.user.common.vo.user.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.microservices.common.core.o.PoParam;
import org.microservices.common.core.o.VoParam;
import org.microservices.common.core.pagination.PageRequest;

/**
 * @author xiangqian
 * @date 22:59 2022/07/21
 */
@Data
@Schema(description = "用户分页参数信息")
public class UserPageVoParam extends PageRequest implements VoParam {

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "用户名")
    private String username;

    @Override
    public void post() {
        nickname = StringUtils.trimToNull(nickname);
        username = StringUtils.trimToNull(username);
    }

    @Override
    public <T extends PoParam> T convertToPoParam(Class<T> type) {


        return VoParam.super.convertToPoParam(type);
    }

}
