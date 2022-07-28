/*     */ package com.sun.tools.hat.internal.server;
/*     */ 
/*     */ import com.sun.tools.hat.internal.model.JavaClass;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.LinkedList;
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
/*     */ 
/*     */ public class PlatformClasses
/*     */ {
/*  55 */   static String[] names = null;
/*     */ 
/*     */   
/*     */   public static synchronized String[] getNames() {
/*  59 */     if (names == null) {
/*  60 */       LinkedList<String> linkedList = new LinkedList();
/*     */ 
/*     */       
/*  63 */       InputStream inputStream = PlatformClasses.class.getResourceAsStream("/com/sun/tools/hat/resources/platform_names.txt");
/*  64 */       if (inputStream != null) {
/*     */         try {
/*  66 */           BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
/*     */           
/*     */           while (true) {
/*  69 */             String str = bufferedReader.readLine();
/*  70 */             if (str == null)
/*     */               break; 
/*  72 */             if (str.length() > 0) {
/*  73 */               linkedList.add(str);
/*     */             }
/*     */           } 
/*  76 */           bufferedReader.close();
/*  77 */           inputStream.close();
/*  78 */         } catch (IOException iOException) {
/*  79 */           iOException.printStackTrace();
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*  84 */       names = linkedList.<String>toArray(new String[linkedList.size()]);
/*     */     } 
/*  86 */     return names;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isPlatformClass(JavaClass paramJavaClass) {
/*  94 */     if (paramJavaClass.isBootstrap()) {
/*  95 */       return true;
/*     */     }
/*     */     
/*  98 */     String str = paramJavaClass.getName();
/*     */     
/* 100 */     if (str.startsWith("[")) {
/* 101 */       int i = str.lastIndexOf('[');
/* 102 */       if (i != -1) {
/* 103 */         if (str.charAt(i + 1) != 'L')
/*     */         {
/* 105 */           return true;
/*     */         }
/*     */         
/* 108 */         str = str.substring(i + 2);
/*     */       } 
/*     */     } 
/* 111 */     String[] arrayOfString = getNames();
/* 112 */     for (byte b = 0; b < arrayOfString.length; b++) {
/* 113 */       if (str.startsWith(arrayOfString[b])) {
/* 114 */         return true;
/*     */       }
/*     */     } 
/* 117 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\server\PlatformClasses.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */