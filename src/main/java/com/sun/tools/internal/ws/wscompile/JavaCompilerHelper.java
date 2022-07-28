/*    */ package com.sun.tools.internal.ws.wscompile;
/*    */ 
/*    */ import com.sun.istack.internal.tools.ParallelWorldClassLoader;
/*    */ import com.sun.tools.internal.ws.resources.JavacompilerMessages;
/*    */ import java.io.File;
/*    */ import java.io.OutputStream;
/*    */ import java.io.PrintWriter;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URISyntaxException;
/*    */ import java.net.URL;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class JavaCompilerHelper
/*    */ {
/*    */   static File getJarFile(Class clazz) {
/* 47 */     URL url = null;
/*    */     try {
/* 49 */       url = ParallelWorldClassLoader.toJarUrl(clazz.getResource('/' + clazz.getName().replace('.', '/') + ".class"));
/* 50 */       return new File(url.toURI());
/* 51 */     } catch (ClassNotFoundException e) {
/*    */       
/* 53 */       throw new Error(e);
/* 54 */     } catch (MalformedURLException e) {
/*    */       
/* 56 */       throw new Error(e);
/* 57 */     } catch (URISyntaxException e) {
/*    */       
/* 59 */       return new File(url.getPath());
/*    */     } 
/*    */   }
/*    */   
/*    */   static boolean compile(String[] args, OutputStream out, ErrorReceiver receiver) {
/* 64 */     ClassLoader cl = Thread.currentThread().getContextClassLoader();
/*    */ 
/*    */     
/*    */     try {
/* 68 */       Class<?> comSunToolsJavacMainClass = cl.loadClass("com.sun.tools.javac.Main");
/*    */       
/*    */       try {
/* 71 */         Method compileMethod = comSunToolsJavacMainClass.getMethod("compile", compileMethodSignature);
/*    */ 
/*    */ 
/*    */         
/* 75 */         Object result = compileMethod.invoke(null, new Object[] { args, new PrintWriter(out) });
/*    */         
/* 77 */         return (result instanceof Integer && ((Integer)result).intValue() == 0);
/* 78 */       } catch (NoSuchMethodException e2) {
/* 79 */         receiver.error(JavacompilerMessages.JAVACOMPILER_NOSUCHMETHOD_ERROR("getMethod(\"compile\", Class[])"), e2);
/* 80 */       } catch (IllegalAccessException e) {
/* 81 */         receiver.error(e);
/* 82 */       } catch (InvocationTargetException e) {
/* 83 */         receiver.error(e);
/*    */       } 
/* 85 */     } catch (ClassNotFoundException e) {
/* 86 */       receiver.error(JavacompilerMessages.JAVACOMPILER_CLASSPATH_ERROR("com.sun.tools.javac.Main"), e);
/* 87 */     } catch (SecurityException e) {
/* 88 */       receiver.error(e);
/*    */     } 
/* 90 */     return false;
/*    */   }
/*    */   
/* 93 */   private static final Class[] compileMethodSignature = new Class[] { String[].class, PrintWriter.class };
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wscompile\JavaCompilerHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */