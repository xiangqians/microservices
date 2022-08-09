package org.microservices.user.biz.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;
import org.microservices.common.core.o.Po;
import org.microservices.common.core.o.Vo;
import org.microservices.user.common.vo.user.UserVo;

import java.time.LocalDateTime;

/**
 * 用户表
 *
 * @author xiangqian
 * @date 01:06 2022/07/21
 */
@Data
@TableName("`user`")
public class UserPo implements Po {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    @TableField("`password`")
    private String password;

    /**
     * 描述
     */
    @TableField("`desc`")
    private String desc;

    /**
     * 附加信息
     */
    private String additionalInfo;

    /**
     * 锁定标记，0-正常，1-锁定
     */
    private String lockFlag;

    /**
     * 删除标识，0-正常，1-删除
     */
    @TableLogic
    private String delFlag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    @Override
    public <T extends Vo> T convertToVo(Class<T> type) {

        if (type == UserVo.class) {
            UserVo vo = new UserVo();
            vo.setId(getId());
            vo.setNickname(getNickname());
            vo.setUsername(getUsername());
            vo.setDesc(getDesc());
            vo.setLockFlag(NumberUtils.toInt(getLockFlag()));
            vo.setCreateTime(getCreateTime());
            vo.setUpdateTime(getUpdateTime());
            return (T) vo;
        }

        return Po.super.convertToVo(type);
    }

}
