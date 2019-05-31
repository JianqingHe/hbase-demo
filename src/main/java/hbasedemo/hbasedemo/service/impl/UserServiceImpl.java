package hbasedemo.hbasedemo.service.impl;

import hbasedemo.hbasedemo.core.PageInfo;
import hbasedemo.hbasedemo.dao.UserDao;
import hbasedemo.hbasedemo.entity.UserEntity;
import hbasedemo.hbasedemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户操作
 *
 * @author hejq
 * @date 2019/5/28 15:25
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 保存用户信息
     *
     * @param user 用户信息
     */
    @Override
    public void saveUser(UserEntity user) {
        userDao.saveUser(user);
    }

    /**
     * 通过姓名查询用户
     *
     * @param name 用户姓名
     * @return 符合条件的用户
     */
    @Override
    public UserEntity findByName(String name) {
        return userDao.findByName(name);
    }

    /**
     * 更新用户
     *
     * @param user 用户信息
     */
    @Override
    public void updateUser(UserEntity user) {
        userDao.updateUser(user);
    }

    /**
     * 通过用户id删除用户信息
     *
     * @param userId 用户id
     */
    @Override
    public void deleteById(Long userId) {
        userDao.deleteById(userId);
    }

    /**
     * 通过分页信息搜索用户数据
     *
     * @param page 分页信息
     * @return 分页返回用户数据
     */
    @Override
    public PageInfo<UserEntity> findByPageInfo(PageInfo page) {
        return userDao.findByPageInfo(page);
    }

    /**
     * 批量插入数据
     *
     * @param userList 用户数据
     */
    @Override
    public void batchInsert(List<UserEntity> userList) {
        userDao.batchInsert(userList);
    }
}
