/*     */ package com.sun.tools.javac.file;
/*     */ 
/*     */ import com.sun.tools.javac.util.Context;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class ZipFileIndexCache
/*     */ {
/*  42 */   private final Map<File, ZipFileIndex> map = new HashMap<>();
/*     */   
/*     */   private static ZipFileIndexCache sharedInstance;
/*     */ 
/*     */   
/*     */   public static synchronized ZipFileIndexCache getSharedInstance() {
/*  48 */     if (sharedInstance == null)
/*  49 */       sharedInstance = new ZipFileIndexCache(); 
/*  50 */     return sharedInstance;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ZipFileIndexCache instance(Context paramContext) {
/*  55 */     ZipFileIndexCache zipFileIndexCache = (ZipFileIndexCache)paramContext.get(ZipFileIndexCache.class);
/*  56 */     if (zipFileIndexCache == null)
/*  57 */       paramContext.put(ZipFileIndexCache.class, zipFileIndexCache = new ZipFileIndexCache()); 
/*  58 */     return zipFileIndexCache;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ZipFileIndex> getZipFileIndexes() {
/*  67 */     return getZipFileIndexes(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized List<ZipFileIndex> getZipFileIndexes(boolean paramBoolean) {
/*  78 */     ArrayList<ZipFileIndex> arrayList = new ArrayList();
/*     */     
/*  80 */     arrayList.addAll(this.map.values());
/*     */     
/*  82 */     if (paramBoolean) {
/*  83 */       for (ZipFileIndex zipFileIndex : arrayList) {
/*  84 */         if (!zipFileIndex.isOpen()) {
/*  85 */           arrayList.remove(zipFileIndex);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*  90 */     return arrayList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized ZipFileIndex getZipFileIndex(File paramFile, RelativePath.RelativeDirectory paramRelativeDirectory, boolean paramBoolean1, String paramString, boolean paramBoolean2) throws IOException {
/*  97 */     ZipFileIndex zipFileIndex = getExistingZipIndex(paramFile);
/*     */     
/*  99 */     if (zipFileIndex == null || (zipFileIndex != null && paramFile.lastModified() != zipFileIndex.zipFileLastModified)) {
/* 100 */       zipFileIndex = new ZipFileIndex(paramFile, paramRelativeDirectory, paramBoolean2, paramBoolean1, paramString);
/*     */       
/* 102 */       this.map.put(paramFile, zipFileIndex);
/*     */     } 
/* 104 */     return zipFileIndex;
/*     */   }
/*     */   
/*     */   public synchronized ZipFileIndex getExistingZipIndex(File paramFile) {
/* 108 */     return this.map.get(paramFile);
/*     */   }
/*     */   
/*     */   public synchronized void clearCache() {
/* 112 */     this.map.clear();
/*     */   }
/*     */   
/*     */   public synchronized void clearCache(long paramLong) {
/* 116 */     Iterator<File> iterator = this.map.keySet().iterator();
/* 117 */     while (iterator.hasNext()) {
/* 118 */       File file = iterator.next();
/* 119 */       ZipFileIndex zipFileIndex = this.map.get(file);
/* 120 */       if (zipFileIndex != null) {
/* 121 */         long l = zipFileIndex.lastReferenceTimeStamp + paramLong;
/* 122 */         if (l < zipFileIndex.lastReferenceTimeStamp || 
/* 123 */           System.currentTimeMillis() > l) {
/* 124 */           this.map.remove(file);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void removeFromCache(File paramFile) {
/* 131 */     this.map.remove(paramFile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setOpenedIndexes(List<ZipFileIndex> paramList) throws IllegalStateException {
/* 138 */     if (this.map.isEmpty()) {
/* 139 */       String str = "Setting opened indexes should be called only when the ZipFileCache is empty. Call JavacFileManager.flush() before calling this method.";
/*     */ 
/*     */       
/* 142 */       throw new IllegalStateException(str);
/*     */     } 
/*     */     
/* 145 */     for (ZipFileIndex zipFileIndex : paramList)
/* 146 */       this.map.put(zipFileIndex.zipFile, zipFileIndex); 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\file\ZipFileIndexCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */