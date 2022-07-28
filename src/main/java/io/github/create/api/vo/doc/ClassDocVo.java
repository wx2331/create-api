package io.github.create.api.vo.doc;

import io.github.create.api.factory.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 源码中的类注释信息
 */
public class ClassDocVo {
    private String className = "";
    private String classPath = "";
    private String classDoc = "";
    private List<MethodDocVo> methodDocVoList;
    private List<FieldDocVo> fieldDocVoList;

    public List<MethodDocVo> getMethodDocVoList() {
        if(StringUtils.isNull(methodDocVoList)){
            methodDocVoList = new ArrayList<>();
        }
        return methodDocVoList;
    }

    public List<FieldDocVo> getFieldDocVoList() {
        if(StringUtils.isNull(fieldDocVoList)){
            fieldDocVoList = new ArrayList<>();
        }
        return fieldDocVoList;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public String getClassDoc() {
        return classDoc;
    }

    public void setClassDoc(String classDoc) {
        this.classDoc = classDoc;
    }

    public void setMethodDocVoList(List<MethodDocVo> methodDocVoList) {
        this.methodDocVoList = methodDocVoList;
    }

    public void setFieldDocVoList(List<FieldDocVo> fieldDocVoList) {
        this.fieldDocVoList = fieldDocVoList;
    }
}
