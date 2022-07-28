/*    */ package com.sun.tools.doclets.internal.toolkit.util;
/*    */ 
/*    */ import com.sun.tools.doclets.internal.toolkit.Configuration;
/*    */ import java.util.Map;
/*    */ import java.util.WeakHashMap;
/*    */ import javax.tools.JavaFileManager;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class DocFileFactory
/*    */ {
/* 49 */   private static final Map<Configuration, DocFileFactory> factories = new WeakHashMap<>();
/*    */ 
/*    */   
/*    */   protected Configuration configuration;
/*    */ 
/*    */ 
/*    */   
/*    */   static synchronized DocFileFactory getFactory(Configuration paramConfiguration) {
/* 57 */     DocFileFactory docFileFactory = factories.get(paramConfiguration);
/* 58 */     if (docFileFactory == null) {
/* 59 */       JavaFileManager javaFileManager = paramConfiguration.getFileManager();
/* 60 */       if (javaFileManager instanceof javax.tools.StandardJavaFileManager) {
/* 61 */         docFileFactory = new StandardDocFileFactory(paramConfiguration);
/*    */       } else {
/*    */         
/*    */         try {
/* 65 */           Class<?> clazz = Class.forName("com.sun.tools.javac.nio.PathFileManager");
/* 66 */           if (clazz.isAssignableFrom(javaFileManager.getClass()))
/* 67 */             docFileFactory = new PathDocFileFactory(paramConfiguration); 
/* 68 */         } catch (Throwable throwable) {
/* 69 */           throw new IllegalStateException(throwable);
/*    */         } 
/*    */       } 
/* 72 */       factories.put(paramConfiguration, docFileFactory);
/*    */     } 
/* 74 */     return docFileFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected DocFileFactory(Configuration paramConfiguration) {
/* 80 */     this.configuration = paramConfiguration;
/*    */   }
/*    */   
/*    */   abstract DocFile createFileForDirectory(String paramString);
/*    */   
/*    */   abstract DocFile createFileForInput(String paramString);
/*    */   
/*    */   abstract DocFile createFileForOutput(DocPath paramDocPath);
/*    */   
/*    */   abstract Iterable<DocFile> list(JavaFileManager.Location paramLocation, DocPath paramDocPath);
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\internal\toolki\\util\DocFileFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */