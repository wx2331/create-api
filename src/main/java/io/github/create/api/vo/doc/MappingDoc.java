package io.github.create.api.vo.doc;

import io.github.create.api.factory.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口信息
 */
public class MappingDoc {
    //请求地址
    private String api;
    //注释
    private String comment;
    //请求类型
    private String apiType;
    /**
     * 返回参数类名
     */
    private String returnClassName;
    //参数信息
    private List<MappingParamDoc> mappingParamDocs;
    private List<MappingParamDoc> returnParamDocs;

    public List<MappingParamDoc> getMappingParamDocs() {
        if(StringUtils.isNull(mappingParamDocs)){
            mappingParamDocs = new ArrayList<>();
        }
        return mappingParamDocs;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getApiType() {
        return apiType;
    }

    public void setApiType(String apiType) {
        this.apiType = apiType;
    }

    public String getReturnClassName() {
        return returnClassName;
    }

    public void setReturnClassName(String returnClassName) {
        this.returnClassName = returnClassName;
    }

    public void setMappingParamDocs(List<MappingParamDoc> mappingParamDocs) {
        this.mappingParamDocs = mappingParamDocs;
    }

    public List<MappingParamDoc> getReturnParamDocs() {
        return returnParamDocs;
    }

    public void setReturnParamDocs(List<MappingParamDoc> returnParamDocs) {
        this.returnParamDocs = returnParamDocs;
    }
}
