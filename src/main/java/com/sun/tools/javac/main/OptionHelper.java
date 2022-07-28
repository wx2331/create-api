/*     */ package com.sun.tools.javac.main;
/*     */ 
/*     */ import com.sun.tools.javac.util.Log;
/*     */ import java.io.File;
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
/*     */ public abstract class OptionHelper
/*     */ {
/*     */   public abstract String get(Option paramOption);
/*     */   
/*     */   public abstract void put(String paramString1, String paramString2);
/*     */   
/*     */   public abstract void remove(String paramString);
/*     */   
/*     */   public abstract Log getLog();
/*     */   
/*     */   public abstract String getOwnName();
/*     */   
/*     */   abstract void error(String paramString, Object... paramVarArgs);
/*     */   
/*     */   abstract void addFile(File paramFile);
/*     */   
/*     */   abstract void addClassName(String paramString);
/*     */   
/*     */   public static class GrumpyHelper
/*     */     extends OptionHelper
/*     */   {
/*     */     private final Log log;
/*     */     
/*     */     public GrumpyHelper(Log param1Log) {
/*  73 */       this.log = param1Log;
/*     */     }
/*     */ 
/*     */     
/*     */     public Log getLog() {
/*  78 */       return this.log;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getOwnName() {
/*  83 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     
/*     */     public String get(Option param1Option) {
/*  88 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/*     */     
/*     */     public void put(String param1String1, String param1String2) {
/*  93 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove(String param1String) {
/*  98 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/*     */     
/*     */     void error(String param1String, Object... param1VarArgs) {
/* 103 */       throw new IllegalArgumentException(this.log.localize(Log.PrefixKind.JAVAC, param1String, param1VarArgs));
/*     */     }
/*     */ 
/*     */     
/*     */     public void addFile(File param1File) {
/* 108 */       throw new IllegalArgumentException(param1File.getPath());
/*     */     }
/*     */ 
/*     */     
/*     */     public void addClassName(String param1String) {
/* 113 */       throw new IllegalArgumentException(param1String);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\main\OptionHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */