package org.xiangqian.microservices.common.cache;

import java.time.Duration;

/**
 * redis客户端操作
 *
 * @author xiangqian
 * @date 21:26 2024/02/28
 */
public interface Cache {

    /**
     * 设置缓存
     *
     * @param key   键
     * @param value 值
     */
    Boolean set(Object key, Object value);

    /**
     * 设置缓存，如果该键已存在，则不进行任何操作
     *
     * @param key   键
     * @param value 值
     */
    Boolean setIfAbsent(Object key, Object value);

    /**
     * 设置缓存过期时间
     *
     * @param key     键
     * @param timeout 过期时间
     * @return
     */
    Boolean expire(Object key, Duration timeout);

    /**
     * 获取缓存值
     *
     * @param key 键
     * @param <V>
     * @return
     */
    <V> V get(Object key);

    /**
     * 删除缓存
     *
     * @param key
     */
    Boolean delete(Object key);

    /**
     * 哈希
     *
     * @param key 键
     * @return
     */
    Hash hash(Object key);

    /**
     * 哈希
     */
    interface Hash {
        /**
         * 设置哈希值
         *
         * @param hashKey   哈希键
         * @param hashValue 哈希值
         * @return
         */
        Boolean set(Object hashKey, Object hashValue);

        /**
         * 获取哈希值
         *
         * @param hashKey 哈希键
         * @param <HV>
         * @return
         */
        <HV> HV get(Object hashKey);

        /**
         * 删除哈希值
         *
         * @param hashKey 哈希键
         * @return
         */
        Boolean delete(Object hashKey);

        /**
         * 是否有哈希键
         *
         * @param hashKey
         * @return
         */
        Boolean hasKey(Object hashKey);
    }

    /**
     * 列表
     *
     * @param key 键
     * @return
     */
    List list(Object key);

    interface List {
        /**
         * 新增元素值
         *
         * @param value 元素值
         * @return
         */
        Boolean add(Object value);

        /**
         * 获取列表指定索引位置的元素
         *
         * @param index
         * @param <V>
         * @return
         */
        <V> V get(int index);

        /**
         * 修改列表中指定索引位置的元素
         *
         * @param index 索引值，从0开始
         * @param value
         * @return
         */
        Boolean set(int index, Object value);

        /**
         * 删除列表中指定索引位置的元素
         *
         * @param index 索引值，从0开始
         * @return
         */
        Boolean delete(int index);

        /**
         * 获取列表大小
         *
         * @return
         */
        Long size();
    }

}
