package hbasedemo.hbasedemo.core;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 开放接口返回数据格式封装
 *
 * @author hejq
 * @date 2019/4/26 14:53
 */
@Data
public class ResultMap<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功编号
     */
    public static final int SUCCESS = 0;

    /**
     * 失败编号
     */
    public static final int FAIL = 1;

    /**
     * 返回信息
     */
    private String msg = "success";

    /**
     * 返回编码
     */
    private int code = SUCCESS;

    /**
     * 返回内容
     */
    private T data;

    /**
     * 无参构造
     */
    public ResultMap() {
        super();
    }

    /**
     * 返回内容
     *
     * @param data 内容
     */
    public ResultMap(T data) {
        super();
        this.data = data;
    }

    /**
     * 返回异常
     *
     * @param e 异常信息
     */
    public ResultMap(Throwable e) {
        super();
        this.msg = e.toString();
        this.code = FAIL;
    }

    /**
     * 成功
     *
     * @return 成功
     */
    public static ResultMap success() {
        return new ResultMap();
    }

    /**
     * 传入对象数据
     *
     * @param data 返回数据
     * @return
     */
    public static ResultMap success(Object data) {
        ResultMap<Object> resultMap = new ResultMap<>();
        resultMap.setData(data);
        return resultMap;
    }

    /**
     * 传入对象数据
     *
     * @param list 返回数据
     * @return
     */
    public static ResultMap<List> list(List list) {
        ResultMap<List> resultMap = new ResultMap<>();
        resultMap.setData(list);
        return resultMap;
    }
}
