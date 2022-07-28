package io.github.create.api.vo;

/**
 * url的参数信息
 */
public class UriParamVo {
    private String paramClassName;
    private Class<?> clazz;
    private boolean isArray;
    private boolean isObject;

    public UriParamVo(String paramClassName) {
        this.paramClassName = paramClassName;
        this.isArray = false;
    }

    public UriParamVo(String paramClassName, boolean isArray) {
        this.paramClassName = paramClassName;
        this.isArray = isArray;
    }

    public UriParamVo(String paramClassName, boolean isArray, boolean isObject) {
        this.paramClassName = paramClassName;
        this.isArray = isArray;
        this.isObject = isObject;
    }

    public UriParamVo(String paramClassName, Class<?> clazz, boolean isArray, boolean isObject) {
        this.paramClassName = paramClassName;
        this.clazz = clazz;
        this.isArray = isArray;
        this.isObject = isObject;
    }

    public UriParamVo() {
    }

    public String getParamClassName() {
        return paramClassName;
    }

    public void setParamClassName(String paramClassName) {
        this.paramClassName = paramClassName;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean array) {
        isArray = array;
    }

    public boolean isObject() {
        return isObject;
    }

    public void setObject(boolean object) {
        isObject = object;
    }
}
