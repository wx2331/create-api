package io.github.create.api.vo.doc;

import io.github.create.api.factory.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 类信息
 */
public class ApiDoc {
    //类名
    private String className;
    //注释
    private String comment;
    //请求信息
    private List<MappingDoc> mappingDocs;

    public List<MappingDoc> getMappingDocs() {
        if(StringUtils.isNull(mappingDocs)){
            mappingDocs = new ArrayList<>();
        }
        return mappingDocs;
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

    public void setMappingDocs(List<MappingDoc> mappingDocs) {
        this.mappingDocs = mappingDocs;
    }
}
