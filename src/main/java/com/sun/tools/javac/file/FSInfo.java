/*    */ package com.sun.tools.javac.file;
/*    */ 
/*    */ import com.sun.tools.javac.util.Context;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.StringTokenizer;
/*    */ import java.util.jar.Attributes;
/*    */ import java.util.jar.JarFile;
/*    */ import java.util.jar.Manifest;
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
/*    */ public class FSInfo
/*    */ {
/*    */   public static FSInfo instance(Context paramContext) {
/* 32 */     FSInfo fSInfo = (FSInfo)paramContext.get(FSInfo.class);
/* 33 */     if (fSInfo == null)
/* 34 */       fSInfo = new FSInfo(); 
/* 35 */     return fSInfo;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FSInfo() {}
/*    */   
/*    */   protected FSInfo(Context paramContext) {
/* 42 */     paramContext.put(FSInfo.class, this);
/*    */   }
/*    */   
/*    */   public File getCanonicalFile(File paramFile) {
/*    */     try {
/* 47 */       return paramFile.getCanonicalFile();
/* 48 */     } catch (IOException iOException) {
/* 49 */       return paramFile.getAbsoluteFile();
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean exists(File paramFile) {
/* 54 */     return paramFile.exists();
/*    */   }
/*    */   
/*    */   public boolean isDirectory(File paramFile) {
/* 58 */     return paramFile.isDirectory();
/*    */   }
/*    */   
/*    */   public boolean isFile(File paramFile) {
/* 62 */     return paramFile.isFile();
/*    */   }
/*    */   
/*    */   public List<File> getJarClassPath(File paramFile) throws IOException {
/* 66 */     String str = paramFile.getParent();
/* 67 */     JarFile jarFile = new JarFile(paramFile);
/*    */     try {
/* 69 */       Manifest manifest = jarFile.getManifest();
/* 70 */       if (manifest == null) {
/* 71 */         return (List)Collections.emptyList();
/*    */       }
/* 73 */       Attributes attributes = manifest.getMainAttributes();
/* 74 */       if (attributes == null) {
/* 75 */         return (List)Collections.emptyList();
/*    */       }
/* 77 */       String str1 = attributes.getValue(Attributes.Name.CLASS_PATH);
/* 78 */       if (str1 == null) {
/* 79 */         return (List)Collections.emptyList();
/*    */       }
/* 81 */       ArrayList<File> arrayList = new ArrayList();
/*    */       
/* 83 */       for (StringTokenizer stringTokenizer = new StringTokenizer(str1); stringTokenizer.hasMoreTokens(); ) {
/* 84 */         String str2 = stringTokenizer.nextToken();
/* 85 */         File file = (str == null) ? new File(str2) : new File(str, str2);
/* 86 */         arrayList.add(file);
/*    */       } 
/*    */       
/* 89 */       return arrayList;
/*    */     } finally {
/* 91 */       jarFile.close();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\file\FSInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */