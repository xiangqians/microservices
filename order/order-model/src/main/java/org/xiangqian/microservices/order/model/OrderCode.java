package org.xiangqian.microservices.order.model;

import org.xiangqian.microservices.common.model.Code;

/**
 * @author xiangqian
 * @date 21:23 2023/09/11
 */
public interface OrderCode extends Code {

    @Description(zh = "【主键】不能大于" + Long.MAX_VALUE)
    String ORDER_ID_MAX = "order_id_max";

    @Description(zh = "【主键】不能小于0")
    String ORDER_ID_MIN = "order_id_min";

    @Description(zh = "【主键】不能为空")
    String ORDER_ID_NOT_NULL = "order_id_not_null";

    @Description(zh = "【用户id】不能大于" + Long.MAX_VALUE)
    String ORDER_USER_ID_MAX = "order_user_id_max";

    @Description(zh = "【用户id】不能为空")
    String ORDER_USER_ID_NOT_NULL = "order_user_id_not_null";

    @Description(zh = "整数部分长度不能大于6位，小数部分长度不能大于4位")
    String ORDER_AMOUNT_DIGITS = "order_amount_digits";

    @Description(zh = "【备注】长度不能大于255个字符")
    String ORDER_REM_SIZE = "order_rem_size";

}
