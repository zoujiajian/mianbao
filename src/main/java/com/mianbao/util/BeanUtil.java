package com.mianbao.util;

import com.google.common.collect.Maps;
import com.mianbao.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by zoujiajian on 2017-4-26.
 * 对象赋值辅助类
 */
public class BeanUtil {

    private BeanUtil(){

    }

    private static <T> T beanInitValue(Class<T> clazz, Map<String, String> paramsMap) throws Exception{
        if(paramsMap == null || paramsMap.size() ==0){
            throw new IllegalArgumentException();
        }
        T obj = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        Map<String,Field> fieldMap = Maps.newHashMap();
        for(Field field: fields){
            if(!fieldMap.containsKey(field.getName())){
                fieldMap.put(field.getName(),field);
            }
        }
        for(String fieldName : paramsMap.keySet()){
            if(fieldMap.containsKey(fieldName)){
                // 将属性的首字符大写，方便构造get，set方法
                String name = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method getMethod = clazz.getMethod("get" + name);

                Field field = fieldMap.get(fieldName);
                Object value = paramsMap.get(fieldName);
                String type = field.getGenericType().toString();

                if(type.equals("class java.lang.String")){
                    String filedValue = (String) getMethod.invoke(obj);
                    if(StringUtils.isEmpty(filedValue)){
                        Method setMethod = clazz.getMethod("set" + name,String.class);
                        setMethod.invoke(obj,value);
                    }
                }

                //封装类型和基本类型兼顾
                if(type.equals("class java.lang.Integer") || type.equals("int")){
                    Integer filedValue = (Integer) getMethod.invoke(obj);
                    if(filedValue == null || filedValue == 0){
                        Method setMethod = clazz.getMethod("set" + name,int.class);
                        setMethod.invoke(obj,Integer.parseInt(value.toString()));
                    }
                }

                fieldSetValue(clazz,value,type,obj);
            }
        }
        return obj;
    }

    private static <T> T beanInitValueWithArray(Class<T> clazz, Map<String,String[]> parameterMap) throws Exception {
        if(clazz == null || parameterMap == null || parameterMap.size() == 0){
            return null;
        }
        Map<String, String> tempMap = Maps.newHashMap();
        for(String key : parameterMap.keySet()){
            //如果值是数组 转换为，号拼接 类似"xx,xxx,xxxx"
            if(parameterMap.get(key).length > 1){
                StringBuilder builder = new StringBuilder();
                for(String k : parameterMap.get(key)){
                    builder.append(",").append(k);
                }
                String temp = null;
                if(StringUtils.isNotEmpty(builder)){
                    temp = builder.toString().replaceFirst(",","");
                }
                tempMap.put(key,temp);
            }else{
                tempMap.put(key,parameterMap.get(key)[0]);
            }

        }
        return beanInitValue(clazz,tempMap);
    }

    //用于扩展
    protected static <T> void fieldSetValue(Class<T> clazz, Object value, String type, Object obj) throws Exception{

    }

    //request解析为对象
    public static <T> T requestParse(HttpServletRequest request, Class<T> clazz){
        Map<String, String[]> parameterMap = request.getParameterMap();
        try {
            return beanInitValueWithArray(clazz,parameterMap);
        } catch (Exception e) {
            throw new BusinessException("request解析异常");
        }
    }
}
