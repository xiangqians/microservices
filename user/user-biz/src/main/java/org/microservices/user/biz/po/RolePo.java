package org.microservices.user.biz.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.microservices.common.core.o.Po;

import java.time.LocalDateTime;

/**
 * 角色表
 *
 * @author xiangqian
 * @date 01:06 2022/07/21
 */
@Data
@TableName("`role`")
public class RolePo implements Po {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 角色码
     */
    @TableField("`code`")
    private String code;

    /**
     * 角色描述
     */
    @TableField("`desc`")
    private String desc;

    /**
     * 附加信息
     */
    private String additionalInfo;

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

}
