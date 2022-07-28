/*     */ package com.sun.codemodel.internal.writer;
/*     */ 
/*     */ import com.sun.codemodel.internal.CodeWriter;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ public class FileCodeWriter
/*     */   extends CodeWriter
/*     */ {
/*     */   private final File target;
/*     */   private final boolean readOnly;
/*  53 */   private final Set<File> readonlyFiles = new HashSet<>();
/*     */   
/*     */   public FileCodeWriter(File target) throws IOException {
/*  56 */     this(target, false);
/*     */   }
/*     */   
/*     */   public FileCodeWriter(File target, String encoding) throws IOException {
/*  60 */     this(target, false, encoding);
/*     */   }
/*     */   
/*     */   public FileCodeWriter(File target, boolean readOnly) throws IOException {
/*  64 */     this(target, readOnly, null);
/*     */   }
/*     */   
/*     */   public FileCodeWriter(File target, boolean readOnly, String encoding) throws IOException {
/*  68 */     this.target = target;
/*  69 */     this.readOnly = readOnly;
/*  70 */     this.encoding = encoding;
/*  71 */     if (!target.exists() || !target.isDirectory())
/*  72 */       throw new IOException(target + ": non-existent directory"); 
/*     */   }
/*     */   
/*     */   public OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
/*  76 */     return new FileOutputStream(getFile(pkg, fileName));
/*     */   }
/*     */   
/*     */   protected File getFile(JPackage pkg, String fileName) throws IOException {
/*     */     File dir;
/*  81 */     if (pkg.isUnnamed()) {
/*  82 */       dir = this.target;
/*     */     } else {
/*  84 */       dir = new File(this.target, toDirName(pkg));
/*     */     } 
/*  86 */     if (!dir.exists()) dir.mkdirs();
/*     */     
/*  88 */     File fn = new File(dir, fileName);
/*     */     
/*  90 */     if (fn.exists() && 
/*  91 */       !fn.delete()) {
/*  92 */       throw new IOException(fn + ": Can't delete previous version");
/*     */     }
/*     */ 
/*     */     
/*  96 */     if (this.readOnly) this.readonlyFiles.add(fn); 
/*  97 */     return fn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 102 */     for (File f : this.readonlyFiles) {
/* 103 */       f.setReadOnly();
/*     */     }
/*     */   }
/*     */   
/*     */   private static String toDirName(JPackage pkg) {
/* 108 */     return pkg.name().replace('.', File.separatorChar);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\writer\FileCodeWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */