package io.github.create.api.factory.service;

import io.github.create.api.factory.utils.JavaFileUtils;
import io.github.create.api.factory.utils.SpringUtils;
import io.github.create.api.factory.utils.StringUtils;
import io.github.create.api.vo.ApiUrlVo;
import io.github.create.api.vo.UriParamVo;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

/**
 * 创建所有的api信息
 */
public class CreateApi {
    /**
     * 创建所有的api信息
     */
    public List<ApiUrlVo> getAllUrlVoList(){
        RequestMappingHandlerMapping mapping = SpringUtils.getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        //返回的集合
        List<ApiUrlVo> list = new ArrayList<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            HandlerMethod method = m.getValue();
            ApiUrlVo apiUrlVo = new ApiUrlVo();
            RequestMappingInfo info = m.getKey();
            try {
                //获取返回参数的泛型classname
                apiUrlVo.setReturnClass(method.getReturnType().getGenericParameterType());
            }catch (Exception e){
                e.printStackTrace();
            }
            //api方法参数列表
            MethodParameter[] methodParameters = method.getMethodParameters();
            //获取方法对应的url
            apiUrlVo.getUrl().addAll(getMethodUrl(info));
            //封装UriParamVo参数信息
            apiUrlVo.getMethodParameterType().addAll(buildUriParamVo(methodParameters));
            //url参数长度
            apiUrlVo.setMethodParameterNum(methodParameters.length);
            //url所在类类名
            apiUrlVo.setClassName(method.getMethod().getDeclaringClass().getName());
            //url所在类
            apiUrlVo.setClazz(method.getMethod().getDeclaringClass());
            //url所属方法名
            apiUrlVo.setMethodName(method.getMethod().getName());
            MethodParameter returnType = method.getReturnType();
            //url请求方式
            RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
            for (RequestMethod requestMethod : methodsCondition.getMethods()) {
                apiUrlVo.getType().add(requestMethod.toString());
            }
            list.add(apiUrlVo);
        }
        return list;
    }

    /**
     * 获取url
     * @param info
     * @return
     */
    private Set<String> getMethodUrl(RequestMappingInfo info){
        Set<String> set = new HashSet<>();
        PatternsRequestCondition p = info.getPatternsCondition();
        if(StringUtils.isNotNull(p)){
            set.addAll(p.getPatterns());
        }
        return set;
    }

    private List<UriParamVo> buildUriParamVo(MethodParameter[] methodParameters){
        List<UriParamVo> uriParamVos = new ArrayList<>();
        if(StringUtils.isNotEmpty(methodParameters)){
            for (MethodParameter methodParameter : methodParameters) {
                Class<?> paramType = methodParameter.getParameter().getType();
                String className = paramType.getName();
                if(StringUtils.isNotNull(paramType.getComponentType())){
                    className = paramType.getComponentType().getName();
                }
                uriParamVos.add(new UriParamVo(className, paramType, paramType.isArray(), JavaFileUtils.getInstance().isObject(className)));
            }
        }
        return uriParamVos;
    }
}
