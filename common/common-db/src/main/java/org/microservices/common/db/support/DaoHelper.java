package org.microservices.common.db.support;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.microservices.common.core.pagination.Page;
import org.microservices.common.core.pagination.PageRequest;

import java.util.Collections;
import java.util.Optional;

/**
 * @author xiangqian
 * @date 01:14 2022/07/21
 */
public class DaoHelper {

    public static <T> Page<T> queryForPage(BaseMapper<T> baseMapper, PageRequest pageRequest, Wrapper<T> queryWrapper) {
        return convertMybatisPageToPage(baseMapper.selectPage(createMybatisPage(pageRequest), queryWrapper));
    }

    public static <T> Page<T> convertMybatisPageToPage(com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> page) {
        Page<T> tPage = new Page<>();
        tPage.setCurrent(page.getCurrent());
        tPage.setSize(page.getSize());
        tPage.setPages(page.getPages());
        tPage.setTotal(page.getTotal());
        tPage.setData(Optional.ofNullable(page.getRecords()).orElse(Collections.emptyList()));
        return tPage;
    }

    public static <T> com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> createMybatisPage(PageRequest pageRequest) {
        return new com.baomidou.mybatisplus.extension.plugins.pagination.Page(pageRequest.getCurrent(), pageRequest.getSize());
    }

}
