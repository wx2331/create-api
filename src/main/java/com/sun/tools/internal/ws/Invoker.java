/*     */ package com.sun.tools.internal.ws;
/*     */ 
/*     */ import com.sun.istack.internal.tools.MaskingClassLoader;
/*     */ import com.sun.istack.internal.tools.ParallelWorldClassLoader;
/*     */ import com.sun.tools.internal.ws.resources.WscompileMessages;
/*     */ import com.sun.tools.internal.ws.wscompile.Options;
/*     */ import com.sun.tools.internal.xjc.api.util.ToolsJarNotFoundException;
/*     */ import com.sun.xml.internal.bind.util.Which;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.Service;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Invoker
/*     */ {
/*  64 */   static final String[] maskedPackages = new String[] { "com.sun.istack.internal.tools.", "com.sun.tools.internal.jxc.", "com.sun.tools.internal.xjc.", "com.sun.tools.internal.ws.", "com.sun.codemodel.internal.", "com.sun.relaxng.", "com.sun.xml.internal.xsom.", "com.sun.xml.internal.bind.", "com.ctc.wstx.", "org.codehaus.stax2.", "com.sun.xml.internal.messaging.saaj.", "com.sun.xml.internal.ws.", "com.oracle.webservices.internal.api." };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean noSystemProxies;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  87 */     boolean noSysProxiesProperty = false;
/*     */     try {
/*  89 */       noSysProxiesProperty = Boolean.getBoolean(Invoker.class.getName() + ".noSystemProxies");
/*  90 */     } catch (SecurityException securityException) {
/*     */     
/*     */     } finally {
/*  93 */       noSystemProxies = noSysProxiesProperty;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static int invoke(String mainClass, String[] args) throws Throwable {
/* 100 */     if (!noSystemProxies) {
/*     */       try {
/* 102 */         System.setProperty("java.net.useSystemProxies", "true");
/* 103 */       } catch (SecurityException securityException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 108 */     ClassLoader oldcc = Thread.currentThread().getContextClassLoader(); try {
/*     */       ParallelWorldClassLoader parallelWorldClassLoader;
/* 110 */       ClassLoader cl = Invoker.class.getClassLoader();
/* 111 */       if (Arrays.<String>asList(args).contains("-Xendorsed")) {
/* 112 */         cl = createClassLoader(cl);
/*     */       } else {
/* 114 */         Options.Target targetVersion; int targetArgIndex = Arrays.<String>asList(args).indexOf("-target");
/*     */         
/* 116 */         if (targetArgIndex != -1) {
/* 117 */           targetVersion = Options.Target.parse(args[targetArgIndex + 1]);
/*     */         } else {
/* 119 */           targetVersion = Options.Target.getDefault();
/*     */         } 
/* 121 */         Options.Target loadedVersion = Options.Target.getLoadedAPIVersion();
/*     */ 
/*     */         
/* 124 */         if (!loadedVersion.isLaterThan(targetVersion)) {
/* 125 */           if (Service.class.getClassLoader() == null) {
/* 126 */             System.err.println(WscompileMessages.INVOKER_NEED_ENDORSED(loadedVersion.getVersion(), targetVersion.getVersion()));
/*     */           } else {
/* 128 */             System.err.println(WscompileMessages.WRAPPER_TASK_LOADING_INCORRECT_API(loadedVersion.getVersion(), Which.which(Service.class), targetVersion.getVersion()));
/*     */           } 
/* 130 */           return -1;
/*     */         } 
/*     */ 
/*     */         
/* 134 */         List<URL> urls = new ArrayList<>();
/* 135 */         findToolsJar(cl, urls);
/*     */         
/* 137 */         if (urls.size() > 0) {
/* 138 */           List<String> mask = new ArrayList<>(Arrays.asList(maskedPackages));
/*     */ 
/*     */ 
/*     */           
/* 142 */           MaskingClassLoader maskingClassLoader = new MaskingClassLoader(cl, mask);
/*     */ 
/*     */           
/* 145 */           URLClassLoader uRLClassLoader = new URLClassLoader(urls.<URL>toArray(new URL[urls.size()]), (ClassLoader)maskingClassLoader);
/*     */ 
/*     */           
/* 148 */           parallelWorldClassLoader = new ParallelWorldClassLoader(uRLClassLoader, "");
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 153 */       Thread.currentThread().setContextClassLoader((ClassLoader)parallelWorldClassLoader);
/*     */       
/* 155 */       Class<?> compileTool = parallelWorldClassLoader.loadClass(mainClass);
/* 156 */       Constructor<?> ctor = compileTool.getConstructor(new Class[] { OutputStream.class });
/* 157 */       Object tool = ctor.newInstance(new Object[] { System.out });
/* 158 */       Method runMethod = compileTool.getMethod("run", new Class[] { String[].class });
/* 159 */       boolean r = ((Boolean)runMethod.invoke(tool, new Object[] { args })).booleanValue();
/* 160 */       return r ? 0 : 1;
/* 161 */     } catch (ToolsJarNotFoundException e) {
/* 162 */       System.err.println(e.getMessage());
/* 163 */     } catch (InvocationTargetException e) {
/* 164 */       throw e.getCause();
/* 165 */     } catch (ClassNotFoundException e) {
/* 166 */       throw e;
/*     */     } finally {
/* 168 */       Thread.currentThread().setContextClassLoader(oldcc);
/*     */     } 
/*     */     
/* 171 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean checkIfLoading21API() {
/*     */     
/* 179 */     try { Service.class.getMethod("getPort", new Class[] { Class.class, WebServiceFeature[].class });
/*     */       
/* 181 */       return true; }
/* 182 */     catch (NoSuchMethodException noSuchMethodException) {  }
/* 183 */     catch (LinkageError linkageError) {}
/*     */ 
/*     */     
/* 186 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean checkIfLoading22API() {
/*     */     
/* 194 */     try { Service.class.getMethod("create", new Class[] { URL.class, QName.class, WebServiceFeature[].class });
/*     */       
/* 196 */       return true; }
/* 197 */     catch (NoSuchMethodException noSuchMethodException) {  }
/* 198 */     catch (LinkageError linkageError) {}
/*     */ 
/*     */     
/* 201 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ClassLoader createClassLoader(ClassLoader cl) throws ClassNotFoundException, IOException, ToolsJarNotFoundException {
/* 211 */     URL[] urls = findIstack22APIs(cl);
/* 212 */     if (urls.length == 0) {
/* 213 */       return cl;
/*     */     }
/* 215 */     List<String> mask = new ArrayList<>(Arrays.asList(maskedPackages));
/* 216 */     if (urls.length > 1) {
/*     */       
/* 218 */       mask.add("javax.xml.bind.");
/* 219 */       mask.add("javax.xml.ws.");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 224 */     MaskingClassLoader maskingClassLoader = new MaskingClassLoader(cl, mask);
/*     */ 
/*     */     
/* 227 */     URLClassLoader uRLClassLoader = new URLClassLoader(urls, (ClassLoader)maskingClassLoader);
/*     */ 
/*     */     
/* 230 */     return (ClassLoader)new ParallelWorldClassLoader(uRLClassLoader, "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static URL[] findIstack22APIs(ClassLoader cl) throws ClassNotFoundException, IOException, ToolsJarNotFoundException {
/* 239 */     List<URL> urls = new ArrayList<>();
/*     */     
/* 241 */     if (Service.class.getClassLoader() == null) {
/*     */       
/* 243 */       URL res = cl.getResource("javax/xml/ws/EndpointContext.class");
/* 244 */       if (res == null)
/* 245 */         throw new ClassNotFoundException("There's no JAX-WS 2.2 API in the classpath"); 
/* 246 */       urls.add(ParallelWorldClassLoader.toJarUrl(res));
/* 247 */       res = cl.getResource("javax/xml/bind/JAXBPermission.class");
/* 248 */       if (res == null)
/* 249 */         throw new ClassNotFoundException("There's no JAXB 2.2 API in the classpath"); 
/* 250 */       urls.add(ParallelWorldClassLoader.toJarUrl(res));
/*     */     } 
/*     */     
/* 253 */     findToolsJar(cl, urls);
/*     */     
/* 255 */     return urls.<URL>toArray(new URL[urls.size()]);
/*     */   }
/*     */   
/*     */   private static void findToolsJar(ClassLoader cl, List<URL> urls) throws ToolsJarNotFoundException, MalformedURLException {
/*     */     try {
/* 260 */       Class.forName("com.sun.tools.javac.Main", false, cl);
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 265 */     catch (ClassNotFoundException e) {
/*     */       
/* 267 */       File jreHome = new File(System.getProperty("java.home"));
/* 268 */       File toolsJar = new File(jreHome.getParent(), "lib/tools.jar");
/*     */       
/* 270 */       if (!toolsJar.exists()) {
/* 271 */         throw new ToolsJarNotFoundException(toolsJar);
/*     */       }
/* 273 */       urls.add(toolsJar.toURL());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\Invoker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */