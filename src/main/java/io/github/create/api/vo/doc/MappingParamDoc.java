package io.github.create.api.vo.doc;

import io.github.create.api.factory.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口参数信息
 */
public class MappingParamDoc {
    //参数名称
    private String paramName;
    //参数注释
    private String comment;
    //该参数是否是一个数组
    private boolean isArray;
    //该参数是否是一个对象
    private boolean isObject;
    //参数类型
    private String className;
    //子参数信息
    private List<MappingParamDoc> mappingParamDocs;

    public List<MappingParamDoc> getMappingParamDocs() {
        if(StringUtils.isNull(mappingParamDocs)){
            mappingParamDocs = new ArrayList<>();
        }
        return mappingParamDocs;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setMappingParamDocs(List<MappingParamDoc> mappingParamDocs) {
        this.mappingParamDocs = mappingParamDocs;
    }
}
