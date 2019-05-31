package hbasedemo.hbasedemo.service;


import hbasedemo.hbasedemo.core.PageInfo;
import hbasedemo.hbasedemo.entity.UserEntity;

import java.util.List;

/**
 * 用户接口
 *
 * @author hejq
 * @date 2019/5/28 15:25
 */
public interface UserService {

    /**
     * 保存用户信息
     *
     * @param user 用户信息
     */
    void saveUser(UserEntity user);

    /**
     * 通过姓名查询用户
     *
     * @param name 用户姓名
     * @return 符合条件的用户
     */
    UserEntity findByName(String name);

    /**
     * 更新用户
     *
     * @param user 用户信息
     */
    void updateUser(UserEntity user);

    /**
     * 通过用户id删除用户信息
     *
     * @param userId 用户id
     */
    void deleteById(Long userId);

    /**
     * 通过分页信息搜索用户数据
     *
     * @param page 分页信息
     * @return 分页返回用户数据
     */
    PageInfo<UserEntity> findByPageInfo(PageInfo page);

    /**
     * 批量插入数据
     *
     * @param userList 用户数据
     */
    void batchInsert(List<UserEntity> userList);
}
