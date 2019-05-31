package hbasedemo.hbasedemo.dao;

import com.google.gson.Gson;
import hbasedemo.hbasedemo.constant.TableInfo;
import hbasedemo.hbasedemo.core.PageInfo;
import hbasedemo.hbasedemo.entity.UserEntity;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author hejq
 * @date 2019/5/31 9:55
 */
@Component
public class UserDao {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    public void saveUser(UserEntity user) {
        hbaseTemplate.put(TableInfo.TABLE_NAME, TableInfo.ROW_NAME, TableInfo.FAMILY_NAME, user.getId().toString(), Bytes.toBytes(user.toString()));
    }

    public UserEntity findByName(String name) {
        return queryForBeanByRowKey(TableInfo.TABLE_NAME, TableInfo.ROW_NAME, UserEntity.class);
    }

    public void updateUser(UserEntity user) {
    }

    public void deleteById(Long userId) {
    }

    public PageInfo<UserEntity> findByPageInfo(PageInfo page) {
        return null;
    }

    public void batchInsert(List<UserEntity> userList) {
    }

    public <T> T queryForBeanByRowKey(String tableName, String rowKey, final Class<T> beanType) {

        return hbaseTemplate.get(tableName, rowKey, (result, rowNum) -> {
            Map<byte[], byte[]> map = result.getFamilyMap(Bytes.toBytes(TableInfo.FAMILY_NAME));
           /* BeanWrapper beanWrapper = new BeanWrapperImpl(t);
            for (Map.Entry<byte[], byte[]> entry : map.entrySet()) {
                beanWrapper.setPropertyValue(Bytes.toString(entry.getKey()),Bytes.toString(entry.getValue()));
            }*/
            return new Gson().fromJson(new Gson().toJson(map), beanType);
        });
    }
}
