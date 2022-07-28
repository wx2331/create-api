/*     */ package com.sun.tools.internal.xjc;
/*     */ 
/*     */ import com.sun.istack.internal.tools.MaskingClassLoader;
/*     */ import com.sun.istack.internal.tools.ParallelWorldClassLoader;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.JAXBContext;
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
/*     */ 
/*     */ class ClassLoaderBuilder
/*     */ {
/*     */   protected static ClassLoader createProtectiveClassLoader(ClassLoader cl, String v) throws ClassNotFoundException, MalformedURLException {
/*     */     URLClassLoader uRLClassLoader;
/*     */     ParallelWorldClassLoader parallelWorldClassLoader;
/*  56 */     if (noHack) return cl;
/*     */     
/*  58 */     boolean mustang = false;
/*     */     
/*  60 */     if (SecureLoader.getClassClassLoader(JAXBContext.class) == null) {
/*     */       
/*  62 */       mustang = true;
/*     */       
/*  64 */       List<String> mask = new ArrayList<>(Arrays.asList(maskedPackages));
/*  65 */       mask.add("javax.xml.bind.");
/*     */       
/*  67 */       MaskingClassLoader maskingClassLoader = new MaskingClassLoader(cl, mask);
/*     */       
/*  69 */       URL apiUrl = maskingClassLoader.getResource("javax/xml/bind/JAXBPermission.class");
/*  70 */       if (apiUrl == null) {
/*  71 */         throw new ClassNotFoundException("There's no JAXB 2.2 API in the classpath");
/*     */       }
/*  73 */       uRLClassLoader = new URLClassLoader(new URL[] { ParallelWorldClassLoader.toJarUrl(apiUrl) }, (ClassLoader)maskingClassLoader);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  82 */     if ("1.0".equals(v)) {
/*  83 */       MaskingClassLoader maskingClassLoader; if (!mustang)
/*     */       {
/*  85 */         maskingClassLoader = new MaskingClassLoader(uRLClassLoader, toolPackages); } 
/*  86 */       parallelWorldClassLoader = new ParallelWorldClassLoader((ClassLoader)maskingClassLoader, "1.0/");
/*     */     }
/*  88 */     else if (mustang) {
/*     */       
/*  90 */       parallelWorldClassLoader = new ParallelWorldClassLoader((ClassLoader)parallelWorldClassLoader, "");
/*     */     } 
/*     */     
/*  93 */     return (ClassLoader)parallelWorldClassLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   private static String[] maskedPackages = new String[] { "com.sun.tools.", "com.sun.codemodel.internal.", "com.sun.relaxng.", "com.sun.xml.internal.xsom.", "com.sun.xml.internal.bind." };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   private static String[] toolPackages = new String[] { "com.sun.tools.", "com.sun.codemodel.internal.", "com.sun.relaxng.", "com.sun.xml.internal.xsom." };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   public static final boolean noHack = Boolean.getBoolean(XJCFacade.class.getName() + ".nohack");
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\ClassLoaderBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */