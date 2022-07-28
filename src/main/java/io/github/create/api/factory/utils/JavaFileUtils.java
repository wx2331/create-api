package io.github.create.api.factory.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JavaFileUtils {
    private static final Logger log = LoggerFactory.getLogger(JavaFileUtils.class);
    final private static JavaFileUtils javaFileUtils = new JavaFileUtils();
    private JavaFileUtils() {
        init();
    }

    public static JavaFileUtils getInstance() {
        return javaFileUtils;
    }

    final private Set<String> javaFiles = new HashSet<>();
    //类对应的绝对路径  key:class.name  value:磁盘绝对路径
    final private Map<String, String> path = new HashMap<>();


    public void init(){
        getAllJavaFile(System.getProperty("user.dir"));
    }

    /**
     * 获取当前工程所有的.java文件
     * @param path
     */
    private void getAllJavaFile(String path){
        File file = new File(path);
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(StringUtils.isNotNull(files)){
                for (File listFile : files) {
                    getAllJavaFile(listFile.getPath());
                }
            }
        }else{
            String filePath = file.getPath();
            if(filePath.endsWith(".java")){
                javaFiles.add(file.getPath());
            }
        }
    }

    /**
     * 获取指定类的java源文件的绝对路径
     * @param className 指定类的全路径
     * @return 若未找到对应路径则返回空
     */
    public String getJavaPath(String className) {
        if (path.containsKey(className)) {
            return path.get(className);
        }
        String pathName = className.replace(".", File.separator) + ".java";
        for (String filePath : javaFiles) {
            if (filePath.endsWith(pathName)) {
                path.put(className, filePath);
                return filePath;
            }
        }
        path.put(className, null);
        log.error("{}not found, pathName:{}", className, pathName);
        return "";
    }

    /**
     * 判断一个类路径是不是object
     * @param className
     * @return
     */
    public boolean isObject(String className){
        return StringUtils.isNotEmpty(getJavaPath(className));
    }
}
