package io.github.create.api.vo.doc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.javadoc.Type;
import io.github.create.api.factory.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 源码中的方法注释信息
 */
public class MethodDocVo {
    private String methodName;
    private String methodDoc;
    private List<MethodParamDocVo> methodParameterComment;
    private Integer methodParameterNum;
    @JsonIgnore
    private Type returnType;

    public List<MethodParamDocVo> getMethodParameterComment() {
        if(StringUtils.isNull(methodParameterComment)){
            methodParameterComment = new ArrayList<>();
        }
        return methodParameterComment;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodDoc() {
        return methodDoc;
    }

    public void setMethodDoc(String methodDoc) {
        this.methodDoc = methodDoc;
    }

    public void setMethodParameterComment(List<MethodParamDocVo> methodParameterComment) {
        this.methodParameterComment = methodParameterComment;
    }

    public Integer getMethodParameterNum() {
        return methodParameterNum;
    }

    public void setMethodParameterNum(Integer methodParameterNum) {
        this.methodParameterNum = methodParameterNum;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }
}
