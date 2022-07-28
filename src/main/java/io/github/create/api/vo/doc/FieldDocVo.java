package io.github.create.api.vo.doc;

public class FieldDocVo {
    private String fieldName;
    private String className;
    private String comment;

    public FieldDocVo(String fieldName, String className, String comment) {
        this.fieldName = fieldName;
        this.className = className;
        this.comment = comment;
    }

    public FieldDocVo() {
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
