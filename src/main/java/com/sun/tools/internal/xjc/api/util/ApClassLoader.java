/*     */ package com.sun.tools.internal.xjc.api.util;
/*     */ 
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ApClassLoader
/*     */   extends URLClassLoader
/*     */ {
/*     */   private final String[] packagePrefixes;
/*     */   
/*     */   public ApClassLoader(@Nullable ClassLoader parent, String[] packagePrefixes) throws ToolsJarNotFoundException {
/*  60 */     super(getToolsJar(parent), parent);
/*  61 */     if ((getURLs()).length == 0) {
/*     */ 
/*     */       
/*  64 */       this.packagePrefixes = new String[0];
/*     */     } else {
/*  66 */       this.packagePrefixes = packagePrefixes;
/*     */     } 
/*     */   }
/*     */   public Class loadClass(String className) throws ClassNotFoundException {
/*  70 */     for (String prefix : this.packagePrefixes) {
/*  71 */       if (className.startsWith(prefix))
/*     */       {
/*     */         
/*  74 */         return findClass(className);
/*     */       }
/*     */     } 
/*     */     
/*  78 */     return super.loadClass(className);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class findClass(String name) throws ClassNotFoundException {
/*  84 */     StringBuilder sb = new StringBuilder(name.length() + 6);
/*  85 */     sb.append(name.replace('.', '/')).append(".class");
/*     */     
/*  87 */     InputStream is = getResourceAsStream(sb.toString());
/*  88 */     if (is == null) {
/*  89 */       throw new ClassNotFoundException("Class not found" + sb);
/*     */     }
/*     */     try {
/*  92 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*  93 */       byte[] buf = new byte[1024];
/*     */       int len;
/*  95 */       while ((len = is.read(buf)) >= 0) {
/*  96 */         baos.write(buf, 0, len);
/*     */       }
/*  98 */       buf = baos.toByteArray();
/*     */ 
/*     */       
/* 101 */       int i = name.lastIndexOf('.');
/* 102 */       if (i != -1) {
/* 103 */         String pkgname = name.substring(0, i);
/* 104 */         Package pkg = getPackage(pkgname);
/* 105 */         if (pkg == null) {
/* 106 */           definePackage(pkgname, null, null, null, null, null, null, null);
/*     */         }
/*     */       } 
/* 109 */       return defineClass(name, buf, 0, buf.length);
/* 110 */     } catch (IOException e) {
/* 111 */       throw new ClassNotFoundException(name, e);
/*     */     } finally {
/*     */       try {
/* 114 */         is.close();
/* 115 */       } catch (IOException iOException) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static URL[] getToolsJar(@Nullable ClassLoader parent) throws ToolsJarNotFoundException {
/*     */     try {
/* 128 */       Class.forName("com.sun.tools.javac.Main", false, parent);
/* 129 */       return new URL[0];
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 134 */     catch (ClassNotFoundException classNotFoundException) {
/*     */ 
/*     */ 
/*     */       
/* 138 */       File jreHome = new File(System.getProperty("java.home"));
/* 139 */       File toolsJar = new File(jreHome.getParent(), "lib/tools.jar");
/*     */       
/* 141 */       if (!toolsJar.exists()) {
/* 142 */         throw new ToolsJarNotFoundException(toolsJar);
/*     */       }
/*     */       
/*     */       try {
/* 146 */         return new URL[] { toolsJar.toURL() };
/* 147 */       } catch (MalformedURLException e) {
/*     */         
/* 149 */         throw new AssertionError(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\ap\\util\ApClassLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */