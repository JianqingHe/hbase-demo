package hbasedemo.hbasedemo.entity;

import com.google.gson.Gson;
import hbasedemo.hbasedemo.annotation.Column;
import hbasedemo.hbasedemo.annotation.HBaseTable;
import hbasedemo.hbasedemo.annotation.RowKey;
import lombok.Data;

/**
 * 用户
 *
 * @author hejq
 * @date 2019/5/28 10:52
 */
@Data
@HBaseTable(tableName = "hb_test_1", family = "f1")
public class UserEntity {

    /**
     * id
     */
    @RowKey
    private Integer id;

    /**
     * 姓名
     */
    @Column
    private String userName;

    /**
     * 性别
     */
    @Column
    private Integer age;


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
