package io.github.create.api.vo;

import io.github.create.api.factory.utils.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * api地址信息
 */
public class ApiUrlVo {
    /**
     * 方法名
     */
    private String methodName;

    /**
     * 方法参数数量
     */
    private Integer methodParameterNum;

    /**
     * 方法参数信息
     */
    private List<UriParamVo> methodParameterType;

    /**
     * 类名
     */
    private String className;
    private Class<?> clazz;

    /**
     * 返回参数
     */
    private Type returnClass;

    /**
     * 请求方式
     */
    private Set<String> type;
    /**
     * 请求地址
     */
    private Set<String> url;

    public Set<String> getUrl() {
        if(StringUtils.isNull(url)){
            url = new HashSet<>();
        }
        return url;
    }

    public Set<String> getType() {
        if(StringUtils.isNull(type)){
            type = new HashSet<>();
        }
        return type;
    }

    public List<UriParamVo> getMethodParameterType() {
        if(StringUtils.isNull(methodParameterType)){
            methodParameterType = new ArrayList<>();
        }
        return methodParameterType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Integer getMethodParameterNum() {
        return methodParameterNum;
    }

    public void setMethodParameterNum(Integer methodParameterNum) {
        this.methodParameterNum = methodParameterNum;
    }

    public void setMethodParameterType(List<UriParamVo> methodParameterType) {
        this.methodParameterType = methodParameterType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Type getReturnClass() {
        return returnClass;
    }

    public void setReturnClass(Type returnClass) {
        this.returnClass = returnClass;
    }

    public void setType(Set<String> type) {
        this.type = type;
    }

    public void setUrl(Set<String> url) {
        this.url = url;
    }
}
