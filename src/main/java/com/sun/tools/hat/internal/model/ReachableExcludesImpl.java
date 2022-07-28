/*     */ package com.sun.tools.hat.internal.model;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.Hashtable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReachableExcludesImpl
/*     */   implements ReachableExcludes
/*     */ {
/*     */   private File excludesFile;
/*     */   private long lastModified;
/*     */   private Hashtable methods;
/*     */   
/*     */   public ReachableExcludesImpl(File paramFile) {
/*  64 */     this.excludesFile = paramFile;
/*  65 */     readFile();
/*     */   }
/*     */   
/*     */   private void readFileIfNeeded() {
/*  69 */     if (this.excludesFile.lastModified() != this.lastModified) {
/*  70 */       synchronized (this) {
/*  71 */         if (this.excludesFile.lastModified() != this.lastModified) {
/*  72 */           readFile();
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void readFile() {
/*  79 */     long l = this.excludesFile.lastModified();
/*  80 */     Hashtable<Object, Object> hashtable = new Hashtable<>();
/*     */     
/*     */     try {
/*  83 */       BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(this.excludesFile)));
/*     */       
/*     */       String str;
/*     */       
/*  87 */       while ((str = bufferedReader.readLine()) != null) {
/*  88 */         hashtable.put(str, str);
/*     */       }
/*  90 */       this.lastModified = l;
/*  91 */       this.methods = hashtable;
/*  92 */     } catch (IOException iOException) {
/*  93 */       System.out.println("Error reading " + this.excludesFile + ":  " + iOException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExcluded(String paramString) {
/* 102 */     readFileIfNeeded();
/* 103 */     return (this.methods.get(paramString) != null);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\model\ReachableExcludesImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */