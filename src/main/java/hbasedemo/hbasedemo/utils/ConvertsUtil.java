package hbasedemo.hbasedemo.utils;

import org.apache.hadoop.hbase.util.Bytes;

import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 赋值工具类
 *
 * @author hejq
 * @date 2019/5/31 15:13
 */
public class ConvertsUtil {

    public ConvertsUtil() {
    }

    /**
     * 字段类型转换赋值
     *
     * @param type 字段类
     * @param value 值
     * @return 转换结果
     */
    public static Object convert(Class type, String value) {
        if (type.equals(String.class)) {
            return value;
        } else if (!type.equals(Integer.TYPE) && !type.equals(Integer.class)) {
            if (!type.equals(Short.class) && !type.equals(Short.TYPE)) {
                if (!type.equals(Integer.class) && !type.equals(Integer.TYPE)) {
                    if (!type.equals(Float.class) && !type.equals(Float.TYPE)) {
                        if (!type.equals(Double.class) && !type.equals(Double.TYPE)) {
                            if (!type.equals(Long.class) && !type.equals(Long.TYPE)) {
                                if (!type.equals(Boolean.class) && !type.equals(Boolean.TYPE)) {
                                    return null;
                                } else {
                                    return Boolean.parseBoolean(value);
                                }
                            } else {
                                return value == null ? 0L : Long.parseLong(value);
                            }
                        } else {
                            return value == null ? 0.0D : Double.parseDouble(value);
                        }
                    } else {
                        return value == null ? 0.0F : Float.parseFloat(value);
                    }
                } else {
                    return value == null ? 0 : Integer.parseInt(value);
                }
            } else {
                return value == null ? 0 : Short.parseShort(value);
            }
        } else {
            return Integer.parseInt(value);
        }
    }

    /**
     * 空值转换
     *
     * @param type 字段类
     * @param value 值
     * @return 转换结果
     */
    public static Object convertWithNull(Class type, String value) {
        if (type.equals(String.class)) {
            return value;
        } else if (!type.equals(Integer.TYPE) && !type.equals(Integer.class)) {
            if (!type.equals(Short.class) && !type.equals(Short.TYPE)) {
                if (!type.equals(Integer.class) && !type.equals(Integer.TYPE)) {
                    if (!type.equals(Float.class) && !type.equals(Float.TYPE)) {
                        if (!type.equals(Double.class) && !type.equals(Double.TYPE)) {
                            if (!type.equals(Long.class) && !type.equals(Long.TYPE)) {
                                if (!type.equals(Boolean.class) && !type.equals(Boolean.TYPE)) {
                                    if (type.equals(Date.class)) {
                                        return value == null ? null : value.toString();
                                    } else if (type.equals(java.util.Date.class)) {
                                        return value == null ? null : java.util.Date.parse(value);
                                    } else if (type.equals(Timestamp.class)) {
                                        return value == null ? null : Timestamp.valueOf(value) + "";
                                    } else {
                                        return null;
                                    }
                                } else {
                                    return value == null ? null : Boolean.parseBoolean(value);
                                }
                            } else {
                                return value == null ? null : Long.parseLong(value);
                            }
                        } else {
                            return value == null ? null : Double.parseDouble(value);
                        }
                    } else {
                        return value == null ? null : Float.parseFloat(value);
                    }
                } else {
                    return value == null ? null : Integer.parseInt(value);
                }
            } else {
                return value == null ? null : Short.parseShort(value);
            }
        } else {
            return Integer.parseInt(value);
        }
    }

    /**
     * 将byte转换成对应的值
     *
     * @param type 类型
     * @param bts byte类型值
     * @return 转换结果
     */
    public static Object bytesToValue(Type type, byte[] bts) {
        if (bts == null && type.equals(String.class)) {
            return null;
        } else if (bts == null && !type.equals(String.class)) {
            return null;
        } else if (type.equals(String.class)) {
            return Bytes.toString(bts);
        } else if (!type.equals(Integer.TYPE) && !type.equals(Integer.class)) {
            if (!type.equals(Short.class) && !type.equals(Short.TYPE)) {
                if (!type.equals(Float.class) && !type.equals(Float.TYPE)) {
                    if (!type.equals(Double.class) && !type.equals(Double.TYPE)) {
                        if (!type.equals(Long.class) && !type.equals(Long.TYPE)) {
                            return !type.equals(Boolean.class) && !type.equals(Boolean.TYPE) ? null : Bytes.toBoolean(bts);
                        } else {
                            return Bytes.toLong(bts);
                        }
                    } else {
                        return Bytes.toDouble(bts);
                    }
                } else {
                    return Bytes.toFloat(bts);
                }
            } else {
                return Bytes.toShort(bts);
            }
        } else {
            return Bytes.toInt(bts);
        }
    }

    /**
     * 值转换成byte
     *
     * @param obj 需要转换的值
     * @return 转换结果
     */
    public static byte[] convertToBytes(Object obj) {
        if (obj.getClass().equals(String.class)) {
            return Bytes.toBytes(obj.toString());
        } else if (!obj.getClass().equals(Integer.TYPE) && !obj.getClass().equals(Integer.class)) {
            if (!obj.getClass().equals(Short.class) && !obj.getClass().equals(Short.TYPE)) {
                if (!obj.getClass().equals(Integer.class) && !obj.getClass().equals(Integer.TYPE)) {
                    if (!obj.getClass().equals(Float.class) && !obj.getClass().equals(Float.TYPE)) {
                        if (!obj.getClass().equals(Double.class) && !obj.getClass().equals(Double.TYPE)) {
                            if (!obj.getClass().equals(Long.class) && !obj.getClass().equals(Long.TYPE)) {
                                if (!obj.getClass().equals(Boolean.class) && !obj.getClass().equals(Boolean.TYPE)) {
                                    return null;
                                } else {
                                    return Bytes.toBytes(Boolean.parseBoolean(obj.toString()));
                                }
                            } else {
                                return obj.toString() == null ? Bytes.toBytes(0L) : Bytes.toBytes(Long.parseLong(obj.toString()));
                            }
                        } else {
                            return Bytes.toBytes(Double.parseDouble(obj.toString()));
                        }
                    } else {
                        return Bytes.toBytes(Float.parseFloat(obj.toString()));
                    }
                } else {
                    return Bytes.toBytes(Integer.parseInt(obj.toString()));
                }
            } else {
                return Bytes.toBytes(Short.parseShort(obj.toString()));
            }
        } else {
            return Bytes.toBytes(Integer.parseInt(obj.toString()));
        }
    }
}
