/*     */ package com.sun.tools.javac.processing;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
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
/*     */ class ServiceProxy
/*     */ {
/*     */   private static final String prefix = "META-INF/services/";
/*     */   
/*     */   static class ServiceConfigurationError
/*     */     extends Error
/*     */   {
/*     */     static final long serialVersionUID = 7732091036771098303L;
/*     */     
/*     */     ServiceConfigurationError(String param1String) {
/*  52 */       super(param1String);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void fail(Class<?> paramClass, String paramString) throws ServiceConfigurationError {
/*  60 */     throw new ServiceConfigurationError(paramClass.getName() + ": " + paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void fail(Class<?> paramClass, URL paramURL, int paramInt, String paramString) throws ServiceConfigurationError {
/*  65 */     fail(paramClass, paramURL + ":" + paramInt + ": " + paramString);
/*     */   }
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
/*     */   private static boolean parse(Class<?> paramClass, URL paramURL) throws ServiceConfigurationError {
/*  85 */     InputStream inputStream = null;
/*  86 */     BufferedReader bufferedReader = null;
/*     */     try {
/*  88 */       inputStream = paramURL.openStream();
/*  89 */       bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
/*  90 */       boolean bool = true;
/*     */       String str;
/*  92 */       while ((str = bufferedReader.readLine()) != null) {
/*  93 */         int i = str.indexOf('#');
/*  94 */         if (i >= 0) str = str.substring(0, i); 
/*  95 */         str = str.trim();
/*  96 */         int j = str.length();
/*  97 */         if (j != 0) {
/*  98 */           if (str.indexOf(' ') >= 0 || str.indexOf('\t') >= 0)
/*  99 */             fail(paramClass, paramURL, bool, "Illegal configuration-file syntax"); 
/* 100 */           int k = str.codePointAt(0);
/* 101 */           if (!Character.isJavaIdentifierStart(k))
/* 102 */             fail(paramClass, paramURL, bool, "Illegal provider-class name: " + str);  int m;
/* 103 */           for (m = Character.charCount(k); m < j; m += Character.charCount(k)) {
/* 104 */             k = str.codePointAt(m);
/* 105 */             if (!Character.isJavaIdentifierPart(k) && k != 46)
/* 106 */               fail(paramClass, paramURL, bool, "Illegal provider-class name: " + str); 
/*     */           } 
/* 108 */           m = 1; return m;
/*     */         } 
/*     */       } 
/* 111 */     } catch (FileNotFoundException fileNotFoundException) {
/* 112 */       return false;
/* 113 */     } catch (IOException iOException) {
/* 114 */       fail(paramClass, ": " + iOException);
/*     */     } finally {
/*     */       try {
/* 117 */         if (bufferedReader != null) bufferedReader.close(); 
/* 118 */       } catch (IOException iOException) {
/* 119 */         fail(paramClass, ": " + iOException);
/*     */       } 
/*     */       try {
/* 122 */         if (inputStream != null) inputStream.close(); 
/* 123 */       } catch (IOException iOException) {
/* 124 */         fail(paramClass, ": " + iOException);
/*     */       } 
/*     */     } 
/* 127 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean hasService(Class<?> paramClass, URL[] paramArrayOfURL) throws ServiceConfigurationError {
/* 136 */     for (URL uRL : paramArrayOfURL) {
/*     */       try {
/* 138 */         String str = "META-INF/services/" + paramClass.getName();
/* 139 */         URL uRL1 = new URL(uRL, str);
/* 140 */         boolean bool = parse(paramClass, uRL1);
/* 141 */         if (bool)
/* 142 */           return true; 
/* 143 */       } catch (MalformedURLException malformedURLException) {}
/*     */     } 
/*     */ 
/*     */     
/* 147 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\processing\ServiceProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */