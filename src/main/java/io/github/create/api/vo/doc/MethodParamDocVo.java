package io.github.create.api.vo.doc;

/**
 * 源码中的方法参数注释信息
 */
public class MethodParamDocVo {
    private String paramName;
    private String className;
    private String paramComment;

    public MethodParamDocVo(String paramName, String className, String paramComment) {
        this.paramName = paramName;
        this.className = className;
        this.paramComment = paramComment;
    }

    public MethodParamDocVo() {
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getParamComment() {
        return paramComment;
    }

    public void setParamComment(String paramComment) {
        this.paramComment = paramComment;
    }
}
