package hbasedemo.hbasedemo;

import hbasedemo.hbasedemo.entity.UserEntity;
import hbasedemo.hbasedemo.service.BaseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HbaseDemoApplicationTests {

    @Autowired
    private BaseService baseService;

    @Test
    public void contextLoads() {
    }

    /**
     * 建表测试
     */
    @Test
    public void testCreateTable() {
        try {
            baseService.createTable(UserEntity.class);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 测试单条数据保存
     *
     * @throws IOException
     */
    @Test
    public void testPut() throws IOException {
        UserEntity userEntity = new UserEntity();
        userEntity.setAge(28);
        userEntity.setUserName("hejq");
        userEntity.setId(1);
        baseService.put(userEntity);
    }

    /**
     * 测试单条查询
     *
     * @throws IOException
     */
    @Test
    public void testGet() throws IOException {
        UserEntity userEntity = baseService.get(1, UserEntity.class);
        System.out.println(userEntity);
    }
}
