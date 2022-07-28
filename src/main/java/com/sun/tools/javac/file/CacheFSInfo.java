/*     */ package com.sun.tools.javac.file;
/*     */ 
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public class CacheFSInfo
/*     */   extends FSInfo
/*     */ {
/*     */   public static void preRegister(Context paramContext) {
/*  50 */     paramContext.put(FSInfo.class, new Context.Factory<FSInfo>() {
/*     */           public FSInfo make(Context param1Context) {
/*  52 */             CacheFSInfo cacheFSInfo = new CacheFSInfo();
/*  53 */             param1Context.put(FSInfo.class, cacheFSInfo);
/*  54 */             return cacheFSInfo;
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void clearCache() {
/*  60 */     this.cache.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public File getCanonicalFile(File paramFile) {
/*  65 */     Entry entry = getEntry(paramFile);
/*  66 */     return entry.canonicalFile;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean exists(File paramFile) {
/*  71 */     Entry entry = getEntry(paramFile);
/*  72 */     return entry.exists;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDirectory(File paramFile) {
/*  77 */     Entry entry = getEntry(paramFile);
/*  78 */     return entry.isDirectory;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFile(File paramFile) {
/*  83 */     Entry entry = getEntry(paramFile);
/*  84 */     return entry.isFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<File> getJarClassPath(File paramFile) throws IOException {
/*  92 */     Entry entry = getEntry(paramFile);
/*  93 */     if (entry.jarClassPath == null)
/*  94 */       entry.jarClassPath = super.getJarClassPath(paramFile); 
/*  95 */     return entry.jarClassPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Entry getEntry(File paramFile) {
/* 102 */     Entry entry = this.cache.get(paramFile);
/* 103 */     if (entry == null) {
/* 104 */       entry = new Entry();
/* 105 */       entry.canonicalFile = super.getCanonicalFile(paramFile);
/* 106 */       entry.exists = super.exists(paramFile);
/* 107 */       entry.isDirectory = super.isDirectory(paramFile);
/* 108 */       entry.isFile = super.isFile(paramFile);
/* 109 */       this.cache.put(paramFile, entry);
/*     */     } 
/* 111 */     return entry;
/*     */   }
/*     */ 
/*     */   
/* 115 */   private Map<File, Entry> cache = new ConcurrentHashMap<>();
/*     */   
/*     */   private static class Entry {
/*     */     File canonicalFile;
/*     */     boolean exists;
/*     */     boolean isFile;
/*     */     boolean isDirectory;
/*     */     List<File> jarClassPath;
/*     */     
/*     */     private Entry() {}
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\file\CacheFSInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */