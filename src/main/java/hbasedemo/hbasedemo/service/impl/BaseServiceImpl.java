package hbasedemo.hbasedemo.service.impl;

import hbasedemo.hbasedemo.annotation.Column;
import hbasedemo.hbasedemo.constant.Constant;
import hbasedemo.hbasedemo.service.BaseService;
import hbasedemo.hbasedemo.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 表操作相关
 *
 * @author hejq
 * @date 2019/5/31 15:04
 */
@Service("BaseService")
@Slf4j
public class BaseServiceImpl implements BaseService {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    /**
     * 创建表
     *
     * @param clazz 实体类
     * @return 创建结果 成功 失败
     * @throws IOException IO异常
     */
    @Override
    public <T> boolean createTable(Class<T> clazz) throws IOException {
        String tableNameStr = HBaseAnnotationUtil.getTableName(clazz);
        String family = HBaseAnnotationUtil.getFamilyName(clazz);
        HTableDescriptor table = new HTableDescriptor(TableName.valueOf(tableNameStr));
        if (hasTable(clazz)) {
            log.warn("该表已存在");
            return false;
        }
        table.addFamily(new HColumnDescriptor(family));
        getAdmin().createTable(table);
        log.info(tableNameStr + "已创建");
        return true;
    }

    /**
     * 删除表
     *
     * @param clazz 实体类
     * @return 创建结果 成功 失败
     * @throws IOException IO异常
     */
    @Override
    public <T> boolean deleteTable(Class<T> clazz) throws IOException {
        Admin admin = getAdmin();
        String tableNameStr = HBaseAnnotationUtil.getTableName(clazz);
        HTableDescriptor table = new HTableDescriptor(TableName.valueOf(tableNameStr));
        if (hasTable(clazz)) {
            admin.disableTable(table.getTableName());
            admin.deleteTable(table.getTableName());
            log.info(tableNameStr + "删除成功");
            return true;
        } else {
            log.warn(tableNameStr + "表不存在");
            return false;
        }
    }

    /**
     * 查询表是否存在
     *
     * @param clazz 实体类
     * @return 创建结果 成功 失败
     * @throws IOException IO异常
     */
    @Override
    public <T> boolean hasTable(Class<T> clazz) throws IOException {
        String tableNameStr = HBaseAnnotationUtil.getTableName(clazz);
        Admin admin = getAdmin();
        TableName tableName = TableName.valueOf(tableNameStr);
        return admin.tableExists(tableName);
    }

    /**
     * 保存数据
     *
     * @param t 实体类
     * @return 创建结果 成功 失败
     * @throws IOException IO异常
     */
    @Override
    public <T> boolean put(T t) throws IOException {
        String tableNameStr = HBaseAnnotationUtil.getTableName(t.getClass());
        Table table = getConnection().getTable(TableName.valueOf(tableNameStr));
        Object keyValue = HBaseAnnotationUtil.getRowKeyValue(t);
        Put put = new Put(Objects.requireNonNull(ConvertsUtil.convertToBytes(keyValue)));
        table.put(addCell(put, t));
        log.info(t + "已添加");
        return true;
    }

    /**
     * 添加列
     *
     * @param put Used to perform Put operations for a single row.
     * @param model 数据
     * @return 添加结果
     */
    private static Put addCell(Put put, Object model) {
        String family = HBaseAnnotationUtil.getFamilyName(model.getClass());
        // field字段为基本类型和集合类型的处理
        List<Field> fields = AnnotationUtil.getFieldsBySpecAnnotation(model.getClass(), Column.class);
        for (Field filed : fields) {
            Object value = JackBeanUtil.getProperty(model, filed.getName());
            if (value == null) {
                continue;
            }
            String type = filed.getGenericType().toString();
            //List转换
            if (type.startsWith("java.util.List")) {
                StringBuilder newValue = new StringBuilder();
                List list = (List) value;
                for (Object o : list) {
                    String oo = CommonUtil.filterSpecChar(o.toString());
                    newValue.append(oo).append(Constant.COLLECTION_SPLIT_CH);
                }
                if (CollectionUtils.isEmpty(list)) {
                    newValue = new StringBuilder();
                } else {
                    newValue = new StringBuilder(newValue.substring(0, newValue.length() - 1));
                }
                value = newValue.toString();
            }
            //Set转换
            if (type.startsWith("java.util.Set")) {
                StringBuilder newValue = new StringBuilder();
                Set set = (Set) value;
                for (Object o : set) {
                    String oo = CommonUtil.filterSpecChar(o.toString());
                    newValue.append(oo).append(Constant.COLLECTION_SPLIT_CH);
                }
                if (CollectionUtils.isEmpty(set)) {
                    newValue = new StringBuilder();
                } else {
                    newValue = new StringBuilder(newValue.substring(0, newValue.length() - 1));
                }
                value = newValue.toString();
            }
            //Map转换
            if (type.startsWith("java.util.Map")) {
                StringBuilder newValue = new StringBuilder();
                Map map = (Map) value;
                for (Object key:map.keySet()) {
                    String keyStr = CommonUtil.filterSpecChar(key.toString());
                    String valueStr = CommonUtil.filterSpecChar(map.get(key).toString());
                    newValue.append(keyStr).append(Constant.MAP_KV_CH).append(valueStr).append(Constant.COLLECTION_SPLIT_CH);
                }
                if (map.isEmpty()) {
                    newValue = new StringBuilder();
                } else{
                    newValue = new StringBuilder(newValue.substring(0, newValue.length() - 1));
                }
                value = newValue.toString();
            }
            put.addColumn(family.getBytes(), filed.getName().getBytes(), ConvertsUtil.convertToBytes(value));
        }
        return put;
    }

    /**
     * 批量保存数据
     *
     * @param clazz 实体类
     * @param list  数据集合
     * @return 创建结果 成功 失败
     * @throws IOException IO异常
     */
    @Override
    public <T> boolean putBatch(Class<T> clazz, List<T> list) throws IOException {
        String tableNameStr = HBaseAnnotationUtil.getTableName(clazz);
        Table table = getConnection().getTable(TableName.valueOf(tableNameStr));
        List<Row> batch = new LinkedList<>();
        for (T t : list) {
            Object keyValue = HBaseAnnotationUtil.getRowKeyValue(t);
            Put put = new Put(Objects.requireNonNull(ConvertsUtil.convertToBytes(keyValue)));
            put = addCell(put, t);
            batch.add(put);
        }
        //用于存放批量操作结果
        Object[] results = new Object[list.size()];
        try {
            table.batch(batch, results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 通过rowKey查询数据
     *
     * @param rowKey 关键字
     * @param clazz 实体类型
     * @param <T> 泛型
     * @return 查询结果
     * @throws IOException IO异常
     */
    @Override
    public <T> T get(Object rowKey, Class<T> clazz) throws IOException {
        //查询所有列
        List<String> columns = AnnotationUtil.getFieldsBySpecAnnotationWithString(clazz, Column.class);
        return get(rowKey, clazz, columns);
    }

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
    @Override
    public <T> T get(Object rowKey, Class<T> clazz, List<String> columns) throws IOException {
        String tableNameStr = HBaseAnnotationUtil.getTableName(clazz);
        String family = HBaseAnnotationUtil.getFamilyName(clazz);
        Table table = getConnection().getTable(TableName.valueOf(tableNameStr));
        Get get = new Get(Objects.requireNonNull(ConvertsUtil.convertToBytes(rowKey)));
        for (String column : columns) {
            get.addColumn(family.getBytes(), column.getBytes());
        }
        Result result = table.get(get);
        if (result.isEmpty()) {
            return null;
        }
        return resultToModel(result, clazz);
    }

    /**
     * 将查询结果转换成对应bean封装数据
     *
     * @param result 查询结果
     * @param clazz bean对象
     * @param <T> 泛型
     * @return 转换结果
     */
    private <T> T resultToModel(Result result, Class<T> clazz) {
        String family = HBaseAnnotationUtil.getFamilyName(clazz);
        T model = JackBeanUtil.getInstanceByClass(clazz);
        List<Field> fields = AnnotationUtil.getFieldsBySpecAnnotation(model.getClass(), Column.class);
        for (Field field : fields) {
            String fieldName = field.getName();
            byte[] b = result.getValue(family.getBytes(), fieldName.getBytes());
            Object value;
            Type type = field.getGenericType();
            if (type.toString().startsWith("java.util.List")) {
                value = ConvertsUtil.bytesToValue(String.class, b);
                if (value ==null) {
                    continue;
                }
                value = Arrays.asList(value.toString().split(Constant.COLLECTION_SPLIT_CH));
            } else if (type.toString().startsWith("java.util.Set")) {
                value = ConvertsUtil.bytesToValue(String.class, b);
                if (value ==null) {
                    continue;
                }
                Set<String> set = new HashSet<>();
                Collections.addAll(set, value.toString().split(Constant.COLLECTION_SPLIT_CH));
                value = set;
            } else if (type.toString().startsWith("java.util.Map")) {
                value = ConvertsUtil.bytesToValue(String.class, b);
                if (value ==null) {
                    continue;
                }
                Map<Object, Object> map = new HashMap<>();
                for(Object o:value.toString().split(Constant.COLLECTION_SPLIT_CH)) {
                    Object[] arr = o.toString().split(Constant.MAP_KV_CH);
                    map.put(arr[0], arr[1]);
                }
                value = map;
            } else {
                value = ConvertsUtil.bytesToValue(type, b);
            }
            JackBeanUtil.setProperty(model, fieldName, value);
        }
        return model;
    }

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
    @Override
    public <T> List<T> scan(Object startRowKey, Object stopRowKey, Class<T> clazz) throws IOException {
        //扫描行并获取所有列
        return scan(startRowKey, stopRowKey, clazz);
    }

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
    @Override
    public <T> List<T> scan(Object startRowKey, Object stopRowKey, Class<T> clazz, List<String> columns) throws IOException {
        String tableNameStr = HBaseAnnotationUtil.getTableName(clazz);
        Table table = getConnection().getTable(TableName.valueOf(tableNameStr));
        String family = HBaseAnnotationUtil.getFamilyName(clazz);
        Scan scan = new Scan();
        for (String column : columns) {
            scan.addColumn(family.getBytes(), column.getBytes());
        }
        ResultScanner scanner = table.getScanner(scan);
        List<T> list = new LinkedList<>();
        for (Result result : scanner) {
            T model = resultToModel(result, clazz);
            list.add(model);
        }
        return list;
    }

    /**
     * 删除数据
     *
     * @param clazz 实体类
     * @param <T> 泛型
     * @return 操作结果 成功 失败
     * @throws IOException IO异常
     */
    @Override
    public <T> boolean delete(Object rowKey, Class<T> clazz) throws IOException {
        if (!hasRow(rowKey, clazz)) {
            return false;
        }
        String tableNameStr = HBaseAnnotationUtil.getTableName(clazz);
        Table table = getConnection().getTable(TableName.valueOf(tableNameStr));
        Delete delete = new Delete(Objects.requireNonNull(ConvertsUtil.convertToBytes(rowKey)));
        table.delete(delete);
        return true;
    }

    /**
     * 批量删除数据
     *
     * @param clazz 实体类
     * @param <T> 泛型
     * @return 操作结果 成功 失败
     * @throws IOException IO异常
     */
    @Override
    public <T> boolean deleteBatch(Class<T> clazz, List<T> list) throws IOException {
        String tableNameStr = HBaseAnnotationUtil.getTableName(clazz);
        Table table = getConnection().getTable(TableName.valueOf(tableNameStr));
        List<Row> batch = new LinkedList<>();
        for (T t : list) {
            Object keyValue = HBaseAnnotationUtil.getRowKeyValue(t);
            if (!hasRow(keyValue, clazz)) {
                continue;
            }
            Delete delete = new Delete(Objects.requireNonNull(ConvertsUtil.convertToBytes(keyValue)));
            batch.add(delete);
        }
        Object[] results = new Object[list.size()];
        try {
            table.batch(batch, results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 查询该条数据是否存在
     *
     * @param rowKey 主键
     * @param clazz 实体类型
     * @param <T> 泛型
     * @return 存在 true false
     * @throws IOException
     */
    @Override
    public <T> boolean hasRow(Object rowKey, Class<T> clazz) throws IOException {
        String tableNameStr = HBaseAnnotationUtil.getTableName(clazz);
        Table table = getConnection().getTable(TableName.valueOf(tableNameStr));
        Get get = new Get(Objects.requireNonNull(ConvertsUtil.convertToBytes(rowKey)));
        Result result = table.get(get);
        return !result.isEmpty();
    }


    /**
     * 连接信息
     */
    private volatile Connection connection;

    /**
     * 获取连接信息
     *
     * @return 连接信息
     */
    private Connection getConnection() {
        if (null == this.connection) {
            synchronized (this) {
                if (null == this.connection) {
                    try {
                        /* 初始化线程 */
                        int initialSize = 10;
                        /* 默认最大的 */
                        int maximumPoolSize = 100;
                        /* 默认60s */
                        long keepAliveTime = 60L;
                        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(initialSize,
                                maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new
                                SynchronousQueue<>());
                        // init pool
                        poolExecutor.prestartCoreThread();
                        this.connection = ConnectionFactory.createConnection(hbaseTemplate.getConfiguration(),
                                poolExecutor);
                    } catch (IOException e) {
                        log.error("hbase connection pool failure!");
                    }
                }
            }
        }
        return this.connection;
    }

    /**
     * 获取连接属性
     *
     * @return 连接属性
     */
    private Admin getAdmin(){
        Admin admin = null;
        try {
            admin = getConnection().getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return admin;
    }
}
