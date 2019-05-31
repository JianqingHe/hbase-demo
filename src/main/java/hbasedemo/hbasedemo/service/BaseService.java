package hbasedemo.hbasedemo.service;

import java.io.IOException;
import java.util.List;

/**
 * HBaseTable 操作相关
 * @author hejq
 * @date 2019/5/31 15:04
 */
public interface BaseService {

    /**
     * 创建表
     *
     * @param clazz 实体类
     * @param <T> 泛型
     * @return 创建结果 成功 失败
     * @throws IOException IO异常
     */
    <T> boolean createTable(Class<T> clazz) throws IOException;

    /**
     * 删除表
     *
     * @param clazz 实体类
     * @param <T> 泛型
     * @return 创建结果 成功 失败
     * @throws IOException IO异常
     */
    <T> boolean deleteTable(Class<T> clazz) throws IOException ;

    /**
     * 查询表是否存在
     *
     * @param clazz 实体类
     * @param <T> 泛型
     * @return 创建结果 成功 失败
     * @throws IOException IO异常
     */
    <T> boolean hasTable(Class<T> clazz) throws IOException ;

    /**
     * 保存数据
     *
     * @param t 实体类
     * @return 创建结果 成功 失败
     * @throws IOException IO异常
     */
    <T> boolean put(T t) throws IOException ;

    /**
     * 批量保存数据
     *
     * @param clazz 实体类
     * @param list 数据集合
     * @return 创建结果 成功 失败
     * @throws IOException IO异常
     */
    <T> boolean putBatch(Class<T> clazz, List<T> list) throws IOException;

    /**
     * 通过rowKey查询数据
     *
     * @param rowKey 关键字
     * @param clazz 实体类型
     * @param <T> 泛型
     * @return 查询结果
     * @throws IOException IO异常
     */
    <T> T get(Object rowKey, Class<T> clazz)  throws IOException ;

    /**
     * 通过rowKey和column查询数据
     *
     * @param rowKey 关键字
     * @param clazz 实体类型
     * @param columns 字段
     * @param <T> 泛型
     * @return 查询结果
     * @throws IOException IO异常
     */
    <T> T get(Object rowKey, Class<T> clazz, List<String> columns) throws IOException ;

    /**
     * 扫描表指定范围
     *
     * @param startRowKey 主键开始
     * @param stopRowKey 主键结束
     * @param clazz 实体类型
     * @param <T> 泛型
     * @return 查询结果
     * @throws IOException IO异常
     */
    <T> List<T> scan(Object startRowKey, Object stopRowKey, Class<T> clazz) throws IOException;

    /**
     * 扫描表指定范围
     *
     * @param startRowKey 主键开始
     * @param stopRowKey 主键结束
     * @param clazz 实体类型
     * @param columns 字段
     * @param <T> 泛型
     * @return 查询结果
     * @throws IOException IO异常
     */
    <T> List<T> scan(Object startRowKey, Object stopRowKey, Class<T> clazz, List<String> columns) throws IOException;

    /**
     * 删除数据
     *
     * @param clazz 实体类
     * @param <T> 泛型
     * @return 操作结果 成功 失败
     * @throws IOException IO异常
     */
    <T> boolean delete(Object rowKey, Class<T> clazz) throws IOException ;

    /**
     * 批量删除数据
     *
     * @param clazz 实体类
     * @param <T> 泛型
     * @return 操作结果 成功 失败
     * @throws IOException IO异常
     */
    <T> boolean deleteBatch(Class<T> clazz, List<T> list) throws IOException;

    /**
     * 查询该条数据是否存在
     *
     * @param rowKey 主键
     * @param clazz 实体类型
     * @param <T> 泛型
     * @return 存在 true false
     * @throws IOException
     */
    <T> boolean hasRow(Object rowKey, Class<T> clazz) throws IOException ;
}
