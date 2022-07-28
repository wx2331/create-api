package io.github.create.api.factory;

import com.alibaba.fastjson.JSON;
import io.github.create.api.factory.service.CreateApi;
import io.github.create.api.factory.utils.ClassDocUtils;
import io.github.create.api.factory.utils.JavaFileUtils;
import io.github.create.api.factory.utils.StringUtils;
import io.github.create.api.vo.ApiUrlVo;
import io.github.create.api.vo.UriParamVo;
import io.github.create.api.vo.doc.*;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

public class CreateApiFactory {

    /**
     * 创建api文档
     * @return
     */
    public Collection<ApiDoc> createApi(){
        List<ApiUrlVo> allUrl = new CreateApi().getAllUrlVoList();
        Map<String, ApiDoc> apiDocMap = new HashMap<>();
        for (ApiUrlVo apiUrlVo : allUrl) {
            Iterator<String> apiIterator = apiUrlVo.getUrl().iterator();
            while (apiIterator.hasNext()){
                //api地址
                String api = apiIterator.next();
                //本地绝对路径
                String javaPath = JavaFileUtils.getInstance().getJavaPath(apiUrlVo.getClassName());
                //对应类注释信息
                ClassDocVo classDocVo = ClassDocUtils.getInstance().getClassDocVo(javaPath);
                //对应注解信息
                ApiDoc apiDoc = apiDocMap.get(apiUrlVo.getClassName());
                if(StringUtils.isNull(apiDoc)){
                    apiDoc = new ApiDoc();
                    apiDoc.setClassName(apiUrlVo.getClassName());
                    apiDoc.setComment(classDocVo.getClassDoc());
                }
                //当前uri对应方法注释信息
                MethodDocVo methodDocVo = ClassDocUtils.getInstance().findMethodDocVo(apiUrlVo, classDocVo);
                MappingDoc mappingDoc = new MappingDoc();
                //api地址
                mappingDoc.setApi(api);
                Iterator<String> apiTypeIterator = apiUrlVo.getType().iterator();
                if(apiTypeIterator.hasNext()){
                    //api类型
                    mappingDoc.setApiType(apiTypeIterator.next());
                }
                //api注释
                mappingDoc.setComment(methodDocVo.getMethodDoc());
                mappingDoc.setReturnClassName(apiUrlVo.getReturnClass().getTypeName());
                //api参数
                mappingDoc.setMappingParamDocs(createUriParamVoList(apiUrlVo, methodDocVo));
//                mappingDoc.setReturnParamDocs(getAllField(apiUrlVo.getClazz()));
                mappingDoc.setReturnParamDocs(createReturnParamVoList(apiUrlVo, methodDocVo));
                apiDoc.getMappingDocs().add(mappingDoc);
                apiDocMap.put(apiUrlVo.getClassName(), apiDoc);
            }
        }
        return apiDocMap.values();
    }


    /**
     * 创建返回参数信息集合
     * @param apiUrlVo
     * @param methodDocVo
     * @return
     */
    private List<MappingParamDoc> createReturnParamVoList(ApiUrlVo apiUrlVo, MethodDocVo methodDocVo){
        Method method = ClassDocUtils.getInstance().findMethod(apiUrlVo);
        if(null == method){
            return new ArrayList<>();
        }
        return getAllField(method.getGenericReturnType());
    }

    /**
     * 获取一个type的成员信息
     * @param type
     * @return
     */
    private List<MappingParamDoc> getAllField(Type type){
        return getAllField(type, new ArrayList<>(), null);
    }

    /**
     * 获取类泛型对应的actualTypeArguments下标
     * @param rawType
     * @return
     */
    private Map<String, Integer> getTypeParametersIndex(Class<?> rawType){
        TypeVariable<? extends Class<?>>[] typeParameters = rawType.getTypeParameters();
        Map<String, Integer> map = new HashMap<>();
        int index = 0;
        for (TypeVariable<? extends Class<?>> typeParameter : typeParameters) {
            map.put(typeParameter.getName(), index ++);
        }
        return map;
    }

    /**
     * 获取一个type的成员信息
     * @param type
     * @param classNameList
     * @param parentParamDoc
     * @return
     */
    private List<MappingParamDoc> getAllField(Type type, List<String> classNameList, MappingParamDoc parentParamDoc){
        return getAllField(type, classNameList, parentParamDoc, new HashMap<>(), new Type[0]);
    }

    /**
     * 获取一个type的成员信息
     * @param type
     * @param classNameList
     * @param parentParamDoc
     * @return
     */
    private List<MappingParamDoc> getAllField(Type type, List<String> classNameList, MappingParamDoc parentParamDoc, Map<String, Integer> typeParametersIndexParam, Type[] types){
        List<MappingParamDoc> mappingParamDocs = new ArrayList<>();
        if(classNameList.contains(type.getTypeName()) || type.getTypeName().startsWith("java.lang")){
            return new ArrayList<>();
        }
        if(type instanceof Class){
            Class<?> type1 = (Class<?>) type;
            //判断是否是基本数据类型
            if(null == type1.getClassLoader()){
                return new ArrayList<>();
            }
            String className = ((Class<?>) type).getName();
            classNameList.add(className);
            if(null != parentParamDoc){
                parentParamDoc.setArray(classIsArray(type1));
                parentParamDoc.setObject(classIsObject(type1));
                parentParamDoc.setClassName(className);
                //注释
                ClassDocVo classDocVo = ClassDocUtils.getInstance().getClassDocVo(type1);
                parentParamDoc.setComment(classDocVo.getClassDoc());
            }
            Method[] methods = ((Class<?>)type).getMethods();
            //注释
            ClassDocVo classDocVo = ClassDocUtils.getInstance().getClassDocVo((Class<?>)type);
            for (Method method : methods) {
                MappingParamDoc mappingParamDoc = new MappingParamDoc();
                String name = method.getName();
                int parameterCount = method.getParameterCount();
                if(parameterCount == 0 && (name.startsWith("get") || name.startsWith("is")) && !name.equals("getClass")){
                    //String substring = name.substring(3);
                    //String fieldName = String.valueOf(substring.charAt(0)).toLowerCase() + substring.substring(1);
                    //参数名称
                    String fieldName = getFieldNameByMethod(name);
                    if("createTime".equals(fieldName)){
                        int a = 1;
                    }
                    mappingParamDoc.setParamName(fieldName);
                    //属性注释
                    String methodComment = ClassDocUtils.findMethodComment(classDocVo, name);
                    if("".equals(methodComment)){
                        methodComment = ClassDocUtils.findFieldComment(classDocVo, fieldName);
                    }
                    mappingParamDoc.setComment(methodComment);
                    //返回类型
                    Type genericReturnType = method.getGenericReturnType();
                    mappingParamDoc.setClassName(genericReturnType.getTypeName());
                    //子参数
                    mappingParamDoc.setMappingParamDocs(getAllField(genericReturnType, classNameList, mappingParamDoc));;
                    mappingParamDocs.add(mappingParamDoc);
                }
            }
        }else if(type instanceof ParameterizedTypeImpl){
            ParameterizedTypeImpl paramType = (ParameterizedTypeImpl)type;
            Class<?> rawType = paramType.getRawType();
            boolean thisClassIsArray = classIsArray(rawType);
            if(null != parentParamDoc){
                parentParamDoc.setArray(thisClassIsArray);
                parentParamDoc.setObject(classIsObject(rawType));
                parentParamDoc.setClassName(rawType.getName()); //rawType.getName()
            }
            classNameList.add(rawType.getName());
            //获取类泛型对应的actualTypeArguments下标
            Map<String, Integer> typeParametersIndex = getTypeParametersIndex(rawType);
            //注释
            ClassDocVo classDocVo = ClassDocUtils.getInstance().getClassDocVo(rawType.getName());
            if(thisClassIsArray){
                return getAllField(paramType.getActualTypeArguments()[0], classNameList, null, typeParametersIndexParam, types);
            }else{
                Method[] methods = rawType.getMethods();
                for (Method method : methods) {
                    String name = method.getName();
                    int parameterCount = method.getParameterCount();
                    if(parameterCount == 0 && (name.startsWith("get") || name.startsWith("is")) && !name.equals("getClass")){
                        MappingParamDoc mappingParamDoc = new MappingParamDoc();
                        //参数名称
                        String fieldName = getFieldNameByMethod(name);
                        mappingParamDoc.setParamName(fieldName);
                        //参数类型
                        Type genericReturnType = method.getGenericReturnType();
                        if(genericReturnType instanceof TypeVariableImpl){
                            TypeVariableImpl typeVariable = (TypeVariableImpl) genericReturnType;
                            String typeName = typeVariable.getName();
                            if(typeParametersIndex.containsKey(typeName)){
                                int actualTypeArgumentsIndex = typeParametersIndex.get(typeName);
                                genericReturnType = paramType.getActualTypeArguments()[actualTypeArgumentsIndex];
                            }
                        }
                        //属性注释
                        String methodComment = ClassDocUtils.findMethodComment(classDocVo, name);
                        if("".equals(methodComment)){
                            methodComment = ClassDocUtils.findFieldComment(classDocVo, fieldName);
                        }
                        mappingParamDoc.setComment(methodComment);
                        mappingParamDoc.setClassName(genericReturnType.getTypeName());
                        //子参数
                        mappingParamDoc.setMappingParamDocs(getAllField(genericReturnType, classNameList, mappingParamDoc, typeParametersIndex, paramType.getActualTypeArguments()));;
                        mappingParamDocs.add(mappingParamDoc);
                    }
                }
            }
        }else if(type instanceof TypeVariableImpl){
            TypeVariableImpl typeVariable = (TypeVariableImpl) type;
            String typeName = typeVariable.getName();
            if(typeParametersIndexParam.containsKey(typeName)){
                int actualTypeArgumentsIndex = typeParametersIndexParam.get(typeName);
                type = types[actualTypeArgumentsIndex];
            }else{
                return new ArrayList<>();
            }
            Class<?> type1 = (Class<?>) type;
            //判断是否是基本数据类型
            if(null == type1.getClassLoader()){
                return new ArrayList<>();
            }
            String className = ((Class<?>) type).getName();
            classNameList.add(className);
            if(null != parentParamDoc){
                parentParamDoc.setArray(classIsArray(type1));
                parentParamDoc.setObject(classIsObject(type1));
                parentParamDoc.setClassName(className);
                //注释
                ClassDocVo classDocVo = ClassDocUtils.getInstance().getClassDocVo(type1);
                parentParamDoc.setComment(classDocVo.getClassDoc());
            }
            Method[] methods = ((Class<?>)type).getMethods();
            //注释
            ClassDocVo classDocVo = ClassDocUtils.getInstance().getClassDocVo((Class<?>)type);
            for (Method method : methods) {
                MappingParamDoc mappingParamDoc = new MappingParamDoc();
                String name = method.getName();
                int parameterCount = method.getParameterCount();
                if(parameterCount == 0 && (name.startsWith("get") || name.startsWith("is")) && !name.equals("getClass")){
                    //String substring = name.substring(3);
                    //String fieldName = String.valueOf(substring.charAt(0)).toLowerCase() + substring.substring(1);
                    //参数名称
                    String fieldName = getFieldNameByMethod(name);
                    if("createTime".equals(fieldName)){
                        int a = 1;
                    }
                    mappingParamDoc.setParamName(fieldName);
                    //属性注释
                    String methodComment = ClassDocUtils.findMethodComment(classDocVo, name);
                    if("".equals(methodComment)){
                        methodComment = ClassDocUtils.findFieldComment(classDocVo, fieldName);
                    }
                    mappingParamDoc.setComment(methodComment);
                    //返回类型
                    Type genericReturnType = method.getGenericReturnType();
                    mappingParamDoc.setClassName(genericReturnType.getTypeName());
                    //子参数
                    mappingParamDoc.setMappingParamDocs(getAllField(genericReturnType, classNameList, mappingParamDoc, new HashMap<>(), new Type[0]));;
                    mappingParamDocs.add(mappingParamDoc);
                }
            }
        }
        return mappingParamDocs;
    }

    /**
     * 判断一个类是不是array
     * @param clazz
     * @return
     */
    private boolean classIsArray(Class<?> clazz){
        return clazz.isArray() || Collection.class.isAssignableFrom(clazz);
    }

    /**
     * 判断一个类是不是对象
     * @param clazz
     * @return
     */
    private boolean classIsObject(Class<?> clazz){
        return clazz.isArray() || Collection.class.isAssignableFrom(clazz);
    }

    /**
     * 从方法命中获取属性名
     * @param methodName
     * @return
     */
    private String getFieldNameByMethod(String methodName){
        String substring = methodName;
        if(methodName.startsWith("get")){
            substring = methodName.substring(3);
        }else if(methodName.startsWith("is")){
            substring = methodName.substring(2);
        }
        if(substring.length() == 0){
            return "";
        }
        return String.valueOf(substring.charAt(0)).toLowerCase() + substring.substring(1);
    }

    /**
     * 参数信息对象集合
     * @param apiUrlVo
     * @param methodDocVo
     * @return
     */
    private List<MappingParamDoc> createUriParamVoList(ApiUrlVo apiUrlVo, MethodDocVo methodDocVo){
        if(JSON.toJSONString(apiUrlVo.getUrl()).contains("/system/user")){
            int a = 1;
        }
        //源码中方法的参数注释信息列表
        //List<MethodParamDocVo> methodParameterCommentList = methodDocVo.getMethodParameterComment();
        //uri的方法的参数信息列表
        List<UriParamVo> uriMethodParamVos = apiUrlVo.getMethodParameterType();
        List<MappingParamDoc> res = new ArrayList<>();
        for (int i = 0; i < uriMethodParamVos.size(); i++) {
            //uri中的方法参数
            UriParamVo uriParam = uriMethodParamVos.get(i);
            //源码中的方法参数
            //MethodParamDocVo methodParamDocVo = findMethodParamDocVo(methodDocVo.getMethodParameterComment(), uriParam);
            MethodParamDocVo methodParamDocVo = new MethodParamDocVo();
            try{
                methodParamDocVo = methodDocVo.getMethodParameterComment().get(i);
            }catch (Exception e){
                e.printStackTrace();
            }
            //封装uri param对象
            MappingParamDoc mappingParamDoc = new MappingParamDoc();
            mappingParamDoc.setParamName(methodParamDocVo.getParamName());
            mappingParamDoc.setComment(methodParamDocVo.getParamComment());
            mappingParamDoc.setArray(uriParam.isArray());
            mappingParamDoc.setObject(uriParam.isObject());
            mappingParamDoc.setClassName(uriParam.getParamClassName());
            mappingParamDoc.setMappingParamDocs(getAllField(uriParam.getClazz(), new ArrayList<>(), mappingParamDoc));
            res.add(mappingParamDoc);
        }
        return res;
    }
}
