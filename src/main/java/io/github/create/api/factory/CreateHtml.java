package io.github.create.api.factory;


import io.github.create.api.factory.utils.StringUtils;
import io.github.create.api.vo.doc.ApiDoc;
import io.github.create.api.vo.doc.MappingDoc;
import io.github.create.api.vo.doc.MappingParamDoc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class CreateHtml {
    public static String create(Collection<ApiDoc> apiDocs){
        StringBuilder sb = new StringBuilder("");
        sb.append(getStart());
        for (ApiDoc apiDoc : apiDocs) {
            //controller信息
            String controllerId = UUID.randomUUID().toString();
            sb.append(" <div class=\"controller\" onclick=\"showApi('").append(controllerId).append("')\">\n")
                    .append("        <span class=\"controller-name\">").append(formatter(apiDoc.getClassName())).append("</span>\n")
                    .append("        <span class=\"comment\">").append(formatter(apiDoc.getComment())).append("</span>\n")
                    .append("</div>");

            //请求信息
            sb.append("<div class=\"controller-api-box\" id=\"").append(controllerId).append("\">");
            List<MappingDoc> mappingDocs = apiDoc.getMappingDocs();
            for (MappingDoc mappingDoc : mappingDocs) {
                String paramId = UUID.randomUUID().toString();
                sb.append("<div class=\"controller-api\" onclick=\"showApi('").append(paramId).append("')\">\n")
                        .append("            <span class=\"btn ").append(formatter(mappingDoc.getApiType()).toLowerCase()).append("\">").append(mappingDoc.getApiType()).append("</span>\n")
                        .append("            <span class=\"api-url\">").append(formatter(mappingDoc.getApi())).append("</span>\n")
                        .append("            <span class=\"comment\">").append(formatter(mappingDoc.getComment())).append("</span>\n")
                        .append("        </div>");

                //参数信息
                sb.append("<div class=\"api-comment hide\" id=\"").append(paramId).append("\">");

                //请求参数
                sb.append("<div class=\"title\">请求参数:</div>");
                sb.append("<div class=\"table\">\n" +
                        "                <div class=\"table-tr border-bottom\">\n" +
                        "                    <div class=\"table-header table-header-1\">参数名</div>\n" +
                        "                    <div class=\"table-header table-header-2\">参数类型</div>\n" +
                        "                    <div class=\"table-header table-header-3\">描述</div>\n" +
                        "                    <div class=\"table-header table-header-4\">是否是对象</div>\n" +
                        "                    <div class=\"table-header table-header-5\">是否是数组</div>\n" +
                        "                </div>");


                //参数子参数
                List<MappingParamDoc> mappingParamDocs = mappingDoc.getMappingParamDocs();
                if(StringUtils.isEmpty(mappingParamDocs)){
                    sb.append("                <div class=\"table-tr border-bottom\">\n").append("暂无参数").append("</div>");
                }else{
                    sb.append(createParam(mappingParamDocs));
                }
                //请求参数结尾
                sb.append("</div>");

                //返回结果
                sb.append("<div class=\"title\">返回结果:</div>");
                sb.append("<div class=\"table\">\n" +
                        "                <div class=\"table-tr border-bottom\">\n" +
                        "                    <div class=\"table-header table-header-1\">参数名</div>\n" +
                        "                    <div class=\"table-header table-header-2\">参数类型</div>\n" +
                        "                    <div class=\"table-header table-header-3\">描述</div>\n" +
                        "                    <div class=\"table-header table-header-4\">是否是对象</div>\n" +
                        "                    <div class=\"table-header table-header-5\">是否是数组</div>\n" +
                        "                </div>");


                //参数子参数
                List<MappingParamDoc> returnParamDocs = mappingDoc.getReturnParamDocs();
                if(StringUtils.isEmpty(returnParamDocs)){
                    sb.append("                <div class=\"table-tr border-bottom\">\n").append("暂无参数").append("</div>");
                }else{
                    sb.append(createParam(returnParamDocs));
                }
                //请求参数结尾
                sb.append("</div>");

                //参数信息结尾
                sb.append("</div>");
            }
            sb.append("</div>");
        }
        sb.append(getEnd());
        return sb.toString();
    }

    private static String formatter(String string){
        if(StringUtils.isEmpty(string)){
            return "";
        }
        return string;
    }

    private static String getStart(){
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "    <style>\n" +
                "        *{\n" +
                "            padding: 0;\n" +
                "            margin: 0;\n" +
                "        }\n" +
                "        .controller{\n" +
                "            padding: 20px 10px;\n" +
                "            /*background-color: #b73636;*/\n" +
                "            border-bottom: 6px solid #b73636;\n" +
                "            font-size: 18px;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "        .controller-name{\n" +
                "        }\n" +
                "        .comment{\n" +
                "            font-weight: normal;\n" +
                "            margin-left: 10px;\n" +
                "        }\n" +
                "        .controller-api{\n" +
                "            padding: 10px 10px 10px 30px;\n" +
                "            font-size: 14px;\n" +
                "            background-color: #eaeaea;\n" +
                "            border-bottom: 4px solid #535353;\n" +
                "        }\n" +
                "        .get{\n" +
                "            background-color: #000000 !important;\n" +
                "        }\n" +
                "        .post{\n" +
                "            background-color: #446E33 !important;\n" +
                "        }\n" +
                "        .put{\n" +
                "            background-color: #294886 !important;\n" +
                "        }\n" +
                "        .delete{\n" +
                "            background-color: #831c1c !important;\n" +
                "        }\n" +
                "        .btn{\n" +
                "            display: inline-block;\n" +
                "            padding: 10px;\n" +
                "            background-color: #000000;\n" +
                "            border-radius: 4px;\n" +
                "            color: aliceblue;\n" +
                "        }\n" +
                "        .controller-api-box{\n" +
                "            display: none;\n" +
                "        }\n" +
                "        .api-url{\n" +
                "            margin-left: 10px;\n" +
                "            display: inline-block;\n" +
                "        }\n" +
                "        .api-comment{\n" +
                "            background-color: #fff;\n" +
                "            font-size: 16px;\n" +
                "            padding: 10px;\n" +
                "        }\n" +
                "        .title{\n" +
                "            margin: 10px 0px;\n" +
                "        }\n" +
                "        .api-comment .title{\n" +
                "            font-weight: 700;\n" +
                "        }\n" +
                "         #result-content { }\n" +
                "        .string { color: green; }\n" +
                "        .number { color: darkorange; }\n" +
                "        .boolean { color: blue; }\n" +
                "        .null { color: magenta; }\n" +
                "        .key { color: red; }\n" +
                "        pre {\n" +
                "            white-space: pre-wrap;\n" +
                "            outline: 1px solid #ccc; padding: 5px; margin: 5px;\n" +
                "        }\n" +
                "\n" +
                "        table{\n" +
                "            width: 100%;\n" +
                "        }\n" +
                "        th{\n" +
                "            padding: 10px 0px;\n" +
                "        }\n" +
                "        td{\n" +
                "            padding: 10px 0px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .table{\n" +
                "            border: 1px solid #d3d3d3;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .table-tr{\n" +
                "            /*border-bottom: 1px solid #d3d3d3;*/\n" +
                "            display: flex;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .border-bottom{\n" +
                "            border-bottom: 1px solid #d3d3d3;\n" +
                "        }\n" +
                "        .table-header{\n" +
                "            display: inline-block;\n" +
                "            font-size: 16px;\n" +
                "            line-height: 30px;\n" +
                "            text-overflow: ellipsis;\n" +
                "            white-space: nowrap;\n" +
                "            overflow: hidden;\n" +
                "        }\n" +
                "        .table-header-1{\n" +
                "            width: 20%;\n" +
                "            border-right: 1px solid #d3d3d3;\n" +
                "        }\n" +
                "        .table-header-2{\n" +
                "            width: 30%;\n" +
                "            font-size: 12px;\n" +
                "            border-right: 1px solid #d3d3d3;\n" +
                "        }\n" +
                "        .table-header-3{\n" +
                "            width: 30%;\n" +
                "            font-size: 12px;\n" +
                "            border-right: 1px solid #d3d3d3;\n" +
                "        }\n" +
                "        .table-header-4{\n" +
                "            width: 10%;\n" +
                "            border-right: 1px solid #d3d3d3;\n" +
                "        }\n" +
                "        .table-header-5{\n" +
                "            width: 10%;\n" +
                "        }\n" +
                "        .last-tr{\n" +
                "            border-bottom: none;\n" +
                "        }\n" +
                "        .hide{\n" +
                "            display: none;\n" +
                "        }\n" +
                "        .div-flex{\n" +
                "            display: flex;\n" +
                "        }\n" +
                "        .pointer:hover{\n" +
                "            cursor: pointer;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>";
    }

    public static String getEnd(){
        return "</body>\n" +
                "<script src=\"https://code.jquery.com/jquery-3.5.1.min.js\"></script>\n" +
                "<script>\n" +
                "function showApi(id, node, showStr, hideStr) {\n" +
                "    console.log(id)\n" +
                "    const idNode = $(\"#\" + id);\n" +
                "    idNode.toggle();\n" +
                "    if(node){\n" +
                "        if(idNode.is(':visible')){\n" +
                "            $(node).text(hideStr);\n" +
                "        }else{\n" +
                "            $(node).text(showStr);\n" +
                "        }\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "$(function(){\n" +
                "    //复选框只读\n" +
                "    $(\"input[class='readonly']\").click(function () {\n" +
                "        this.checked = !this.checked;\n" +
                "        console.log(this.checked)\n" +
                "    })\n" +
                "})" +
                "</script>\n" +
                "</html>\n";
    }

    private static String createParam(List<MappingParamDoc> paramDocs){
        return createParam(paramDocs, null, 0);
    }
    private static String createParam(List<MappingParamDoc> paramDocs, String paramId, int index){
        if(StringUtils.isEmpty(paramDocs)){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if(StringUtils.isNotEmpty(paramId)){
            sb.append("<div class=\"hide\" id=\"").append(paramId).append("\">");
        }
        for (int i = 0; i < paramDocs.size(); i++) {
            MappingParamDoc paramDoc = paramDocs.get(i);
            String chiParamId = UUID.randomUUID().toString();
            //列开始
            sb.append("<div class=\"table-tr\">\n");

            //列内容 第一列
            sb.append("                    <div class=\"table-header table-header-1\">\n" +
                    "                        <div class=\"div-flex\">\n");


            //最后一个参数加底部下划线
            if(index > 0){
                for (int j = 0; j < index; j++) {
                    sb.append("<div style=\"display: inline-block;width: 8%;border-right: 1px solid #d3d3d3\">&nbsp;</div>");
                }
            }

            if(i == paramDocs.size() - 1){
                sb.append("<div class=\"border-bottom\" ");
            }else{
                sb.append("<div ");
            }
            if(StringUtils.isNotEmpty(paramDoc.getMappingParamDocs())){
                sb.append("style=\"display: inline-block;width: 8%;border-right: 1px solid #d3d3d3\">\n")
                        .append("                                <span class=\"pointer\" onclick=\"showApi('").append(chiParamId).append("', this, '+', '-')\">+</span>\n")
                        .append("                            </div>");

            }else{
                sb.append("style=\"display: inline-block;width: 8%;border-right: 1px solid #d3d3d3\">&nbsp;</div>");
            }
            sb.append("                            <div class=\"border-bottom\" style=\"display: inline-block;width: ").append(100 - (index + 1) * 8).append("%;\">").append(formatter(paramDoc.getParamName())).append("</div>");
            //第一列结束
            sb.append("</div></div>");

            sb.append("<div class=\"table-header border-bottom table-header-2\" title=\"").append(formatter(paramDoc.getClassName())).append("\">").append(formatter(paramDoc.getClassName())).append("</div>\n")
                    .append("                        <div class=\"table-header border-bottom table-header-3\" title=\"").append(formatter(paramDoc.getComment())).append("\">").append(formatter(paramDoc.getComment())).append("</div>\n")
                    .append("                        <div class=\"table-header border-bottom table-header-4\"><input type=\"checkbox\"").append(paramDoc.isObject() ? "checked" : "").append(" class=\"readonly\">").append(paramDoc.isObject()).append("</div>\n")
                    .append("                        <div class=\"table-header border-bottom table-header-5\"><input type=\"checkbox\"").append(paramDoc.isArray() ? "checked" : "").append(" class=\"readonly\">").append(paramDoc.isArray()).append("</div>");

            //列结束
            sb.append("</div>");

            if(StringUtils.isNotEmpty(paramDoc.getMappingParamDocs())){
                sb.append(createParam(paramDoc.getMappingParamDocs(), chiParamId, index + 1));

            }
        }
        if(StringUtils.isNotEmpty(paramId)){
            sb.append("</div>");
        }

        return sb.toString();
    }
}
