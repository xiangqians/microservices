package org.microservices.user.biz.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.microservices.common.core.o.Po;

import java.time.LocalDateTime;

/**
 * 权限表
 *
 * @author xiangqian
 * @date 01:06 2022/07/21
 */
@Data
@TableName("permission")
public class PermissionPo implements Po {

    private static final long serialVersionUID = 1L;

    /**
     * 权限id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 权限父节点id
     */
    private Integer parentId;

    /**
     * 权限名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 权限允许权限方法，GET、POST、PUT、DELETE
     */
    private String method;

    /**
     * 权限路径
     */
    @TableField("`path`")
    private String path;

    /**
     * 权限描述
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
