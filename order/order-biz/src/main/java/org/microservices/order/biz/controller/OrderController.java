package org.microservices.order.biz.controller;

import org.microservices.common.core.pagination.PageDTO;
import org.microservices.common.core.pagination.PageVO;
import org.microservices.user.common.dato.SysUserDO;
import org.microservices.user.common.dto.SysUserDTO;
import org.microservices.user.common.vo.SysUserVO;
import org.microservices.user.api.feign.service.SysUserFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiangqian
 * @date 10:57 2022/04/10
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private SysUserFeignService sysUserFeignService;

    @GetMapping(value = "/user/page")
//    @HystrixCommand(fallbackMethod = "getErrorInfo")
    public PageVO<SysUserDO, SysUserDTO, SysUserVO> page(PageDTO pageDTO) {
        return sysUserFeignService.page(pageDTO).convertToVO();
    }

    public String getErrorInfo() {
        return "Network error, please hold on...";
    }

}
