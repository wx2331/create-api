package io.github.create.api.factory;

import io.github.create.api.factory.utils.IdUtils;
import io.github.create.api.factory.utils.StringUtils;
import io.github.create.api.vo.doc.ApiDoc;
import io.github.create.api.vo.doc.MappingDoc;
import io.github.create.api.vo.doc.MappingParamDoc;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CreateHtmlByVm {
    final private static String arrow_png_base64 = "data:image/png;base64,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";
    public static byte[] create(Collection<ApiDoc> apiDocs) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        buildZip(apiDocs, zip);
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * 创建压缩文件
     * @param apiDocs
     * @param zip
     * @throws Exception
     */
    private static void buildZip(Collection<ApiDoc> apiDocs, ZipOutputStream zip) throws Exception {
        StringBuilder sb = new StringBuilder("");
        for (ApiDoc apiDoc : apiDocs) {
            //controller信息 渲染index
            sb.append("\n" +
                    "\t\t\t<li>\n" +
                    "\t\t\t\t<div class=\"menu-title-main\" id=\"" + IdUtils.simpleUUID() + "\" onclick=\"toggleRequest(this)\">\n" +
                    "\t\t\t\t\t<div class=\"menu-icon\">\n" +
                    "\t\t\t\t\t\t<img src=\"" + arrow_png_base64 + "\">\n" +
                    "\t\t\t\t\t</div>\n" +
                    "\t\t\t\t\t<div class=\"menu-title\" title=\"" + apiDoc.getComment() + "\">" + apiDoc.getComment() + "</div>\n" +
                    "\t\t\t\t</div>\n" +
                    "\t\t\t\t<div class=\"menu-request-main\">\n" +
                    "\t\t\t\t\t<ul>\n");

            List<MappingDoc> mappingDocs = apiDoc.getMappingDocs();
            for (MappingDoc mappingDoc : mappingDocs) {
                String paramId = UUID.randomUUID().toString();
                sb.append("\t\t\t\t\t\t<li class=\"method-request-item\" id=\"" + paramId + "\" onclick=\"showPage(this, './detail/" + paramId + ".html')\">\n" +
                                "\t\t\t\t\t\t\t<span class=\"method-type method-" + formatter(mappingDoc.getApiType()).toLowerCase() + "\">" + formatter(mappingDoc.getApiType()).toLowerCase() + "</span>\n" +
                                "\t\t\t\t\t\t\t<span class=\"request-title\" title=\"" + formatter(mappingDoc.getComment()) + "\">" + formatter(mappingDoc.getComment()) + "</span>\n" +
                                "\t\t\t\t\t\t</li>\n");
                //创建请求信息文件
                createRequestFile(apiDoc, mappingDoc, paramId, zip);
            }
            sb.append("\t\t\t\t\t</ul>\n" +
                            "\t\t\t\t</div>\n" +
                            "\t\t\t</li>");
        }
        String content = readFile("vm/index.html.vm")
                .replace("{menu-content}", sb.toString());
        // 添加到zip
        zip.putNextEntry(new ZipEntry("index.html"));
        IOUtils.write(content, zip, "UTF-8");
        zip.flush();
        zip.closeEntry();
    }

    private static void createRequestFile(ApiDoc apiDocs, MappingDoc mappingDoc, String fileName, ZipOutputStream zip) throws Exception{
        String content = readFile("vm/demo_request.html.vm")
                .replace("{uriName}", formatter(mappingDoc.getApi()))
                .replace("{methodType}", formatter(mappingDoc.getApiType()))
                .replace("{methodTypeLower}", formatter(mappingDoc.getApiType()).toLowerCase())
                .replace("{uri}", formatter(mappingDoc.getApi()))
                .replace("{param}", getParamTableStr(mappingDoc.getMappingParamDocs()))
                .replace("{result}", getParamTableStr(mappingDoc.getReturnParamDocs()));
        // 添加到zip
        zip.putNextEntry(new ZipEntry("/detail/" + fileName + ".html"));
        IOUtils.write(content, zip, "UTF-8");
        zip.flush();
        zip.closeEntry();
    }

    /**
     * 参数table
     * @param mappingParamDocs
     * @return
     */
    private static String getParamTableStr(List<MappingParamDoc> mappingParamDocs) {
        if(StringUtils.isEmpty(mappingParamDocs)){
            return "                <div class=\"table-tr border-bottom\">\n" + "暂无参数" + "</div>";
        }else{
            return createParam(mappingParamDocs);
        }
    }


    public static String readFile(String filePath) {
        try {
            ClassPathResource file = new ClassPathResource(filePath);
            if (file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        file.getInputStream(), StandardCharsets.UTF_8);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                StringBuilder sb = new StringBuilder();
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    sb.append(lineTxt).append("\n");
                }
                read.close();
                return sb.toString();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception
                e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return "";
    }

    public static String readFileBak(String filePath) {
        try {
            File file = new File(CreateHtmlByVm.class.getClassLoader().getResource("").getFile() + filePath);
            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), StandardCharsets.UTF_8);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                StringBuilder sb = new StringBuilder();
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    sb.append(lineTxt).append("\n");
                }
                read.close();
                return sb.toString();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception
                e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return "";
    }

    private static String formatter(String string){
        if(StringUtils.isEmpty(string)){
            return "";
        }
        return string;
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
