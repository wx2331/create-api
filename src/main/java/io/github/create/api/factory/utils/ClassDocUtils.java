package io.github.create.api.factory.utils;

import com.sun.javadoc.*;
import io.github.create.api.vo.ApiUrlVo;
import io.github.create.api.vo.UriParamVo;
import io.github.create.api.vo.doc.ClassDocVo;
import io.github.create.api.vo.doc.FieldDocVo;
import io.github.create.api.vo.doc.MethodDocVo;
import io.github.create.api.vo.doc.MethodParamDocVo;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ClassDocUtils {
    final private static ClassDocUtils classDocUtils = new ClassDocUtils();
    private ClassDocUtils() {
    }

    public static ClassDocUtils getInstance() {
        return classDocUtils;
    }

    //java源文件的注释信息  key:java源文件绝对地址  value:注释信息
    private Map<String, ClassDocVo> classDocVoMap = new HashMap<>();

    public ClassDocVo getClassDocVo(Class<?> clazz){
        if(null == clazz){
            return new ClassDocVo();
        }
        String javaPath = JavaFileUtils.getInstance().getJavaPath(clazz.getName());
        return getClassDocVo(javaPath);
    }
    /**
     * 获取指定java源文件的注释信息
     * @param javaPath 源文件地址
     * @return
     */
    public ClassDocVo getClassDocVo(String javaPath){
        if(null == javaPath || "".equals(javaPath)){
            return new ClassDocVo();
        }
        if(javaPath.contains("java.lang.")){
            int a = 1;
        }
        if(classDocVoMap.containsKey(javaPath)){
            return classDocVoMap.get(javaPath);
        }
        ClassDocVo classDocVo = getClassDoc(javaPath);
        classDocVoMap.put(javaPath, classDocVo);
        return classDocVo;
    }

    private static RootDoc rootDoc;

    public static boolean start(RootDoc root) {
        rootDoc = root;
        return true;
    }

    private ClassDocVo getClassDoc(String javaBeanFilePath) {
        ClassDocVo classDocVo = new ClassDocVo();
        try{
            com.sun.tools.javadoc.Main.execute(new String[]{"-doclet", ClassDocUtils.class.getName(), "-docletpath",
                    ClassDocUtils.class.getResource("/").getPath(), "-encoding", "utf-8", javaBeanFilePath});
            ClassDoc[] classes = rootDoc.classes();

            if (classes == null) {// || classes.length == 0
                return classDocVo;
            }
            if (classes.length == 0) {// || classes.length == 0
                return classDocVo;
            }
            ClassDoc classDoc = classes[0];
            // 获取类的全路径
            classDocVo.setClassPath(classDoc.qualifiedTypeName());
            // 获取类的名称
            classDocVo.setClassName(classDoc.name());
            // 获取类的注释
            classDocVo.setClassDoc(getComment(classDoc.getRawCommentText()));
            //获取方法的注释
            MethodDoc[] methods = classes[0].methods();
            for (MethodDoc methodDoc : methods) {
                MethodDocVo methodDocVo = new MethodDocVo();
                methodDocVo.setMethodName(methodDoc.name());
//            System.out.printf("%s方法注释:%s\n", methodDoc.name(), getComment(methodDoc.getRawCommentText()));
                methodDocVo.setMethodDoc(getComment(methodDoc.getRawCommentText()));
                //方法参数
                Parameter[] parameters = methodDoc.parameters();
                //方法参数注释
                ParamTag[] paramTags = methodDoc.paramTags();
                for (Parameter parameter : parameters) {
                    //参数名称
                    String paramName = parameter.name();
                    //参数类型
                    String typeClassName = parameter.type().qualifiedTypeName();
                    methodDocVo.getMethodParameterComment().add(new MethodParamDocVo(paramName, typeClassName, getParamComment(paramTags, paramName)));
                }
                methodDocVo.setMethodParameterNum(parameters.length);
                methodDocVo.setReturnType(methodDoc.returnType());
                classDocVo.getMethodDocVoList().add(methodDocVo);
            }
            // 获取属性名称和注释
            FieldDoc[] fields = classDoc.fields(false);
            for (FieldDoc field : fields) {
                classDocVo.getFieldDocVoList().add(new FieldDocVo(field.name(), field.type().qualifiedTypeName(), field.commentText()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return classDocVo;
    }

    /**
     * 获取参数注解
     * @param paramTags
     * @param paramName
     * @return
     */
    public String getParamComment(ParamTag[] paramTags, String paramName){
        for (ParamTag paramTag : paramTags) {
            if(Objects.equals(paramName, paramTag.parameterName())){
                return paramTag.parameterComment();
            }
        }
        return "";
    }

    /**
     * 获取注释
     * @param comment
     * @return
     */
    private String getComment(String comment){
        if(StringUtils.isEmpty(comment)){
            return "";
        }
        String spitStr = "\n";
        for (String msg : comment.split(spitStr)) {
            if (!msg.trim().startsWith("@") && msg.trim().length() > 0) {
                return msg;
            }
        }
        return "";
    }


    /**
     * 从类注释中寻找当前方法注释信息
     * @param apiUrlVo
     * @param classDocVo
     * @return
     */
    public MethodDocVo findMethodDocVo(ApiUrlVo apiUrlVo, ClassDocVo classDocVo){
        for (MethodDocVo methodDocVo : classDocVo.getMethodDocVoList()) {
            //方法名和参数长度一致
            if(methodDocVo.getMethodName().equals(apiUrlVo.getMethodName()) && methodDocVo.getMethodParameterNum().equals(apiUrlVo.getMethodParameterNum())){
                //源码中方法的参数注释信息列表
                List<MethodParamDocVo> methodParameterCommentList = methodDocVo.getMethodParameterComment();
                //uri的方法的参数信息列表
                List<UriParamVo> uriMethodParamVos = apiUrlVo.getMethodParameterType();
                //是否当前方法的标志
                boolean isThisMethod = true;
                for (int i = 0; i < methodParameterCommentList.size(); i++) {
                    //源码中的方法参数
                    MethodParamDocVo methodParamDocVo = methodParameterCommentList.get(i);
                    //uri中的方法参数
                    UriParamVo uriParam = uriMethodParamVos.get(i);
                    try{
                        if(!methodParamDocVo.getClassName().equals(uriParam.getParamClassName())){
                            isThisMethod = false;
                            break;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if(isThisMethod){
                    return methodDocVo;
                }
            }
        }
        return new MethodDocVo();
    }


    /**
     * 从类注释中寻找当前方法注释信息
     * @param apiUrlVo
     * @return
     */
    public Method findMethod(ApiUrlVo apiUrlVo){
        Method[] methods = apiUrlVo.getClazz().getMethods();
        for (Method method : methods) {
            //方法名和参数长度一致
            if(method.getName().equals(apiUrlVo.getMethodName()) && method.getParameterCount() == apiUrlVo.getMethodParameterNum()){
                Class<?>[] parameterTypes = method.getParameterTypes();
                //uri的方法的参数信息列表
                List<UriParamVo> uriMethodParamVos = apiUrlVo.getMethodParameterType();
                //是否当前方法的标志
                boolean isThisMethod = true;
                for (int i = 0; i < uriMethodParamVos.size(); i++) {
                    //源码中的方法参数
                    Class<?> parameterType = parameterTypes[i];
                    //uri中的方法参数
                    UriParamVo uriParam = uriMethodParamVos.get(i);
                    try{
                        if(!parameterType.getName().equals(uriParam.getParamClassName())){
                            isThisMethod = false;
                            break;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if(isThisMethod){
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * 查询属性注释
     * @param classDocVo
     * @param fieldName
     * @return
     */
    public static String findFieldComment(ClassDocVo classDocVo, String fieldName){
        for (FieldDocVo fieldDocVo : classDocVo.getFieldDocVoList()) {
            if(fieldDocVo.getFieldName().equals(fieldName)){
                return fieldDocVo.getComment();
            }
        }
        return "";
    }

    /**
     * 查询方法注释
     * @param classDocVo
     * @param methodName
     * @return
     */
    public static String findMethodComment(ClassDocVo classDocVo, String methodName){
        for (MethodDocVo methodDocVo : classDocVo.getMethodDocVoList()) {
            if(methodDocVo.getMethodDoc().equals(methodName)){
                return methodDocVo.getMethodDoc();
            }
        }
        return "";
    }
}
