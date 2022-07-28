/*    */ package com.sun.tools.internal.xjc;
/*    */ 
/*    */ import com.sun.codemodel.internal.CodeWriter;
/*    */ import com.sun.codemodel.internal.JPackage;
/*    */ import com.sun.codemodel.internal.writer.FilterCodeWriter;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.io.Writer;
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
/*    */ final class ProgressCodeWriter
/*    */   extends FilterCodeWriter
/*    */ {
/*    */   private int current;
/*    */   private final int totalFileCount;
/*    */   private final XJCListener progress;
/*    */   
/*    */   public ProgressCodeWriter(CodeWriter output, XJCListener progress, int totalFileCount) {
/* 46 */     super(output);
/* 47 */     this.progress = progress;
/* 48 */     this.totalFileCount = totalFileCount;
/* 49 */     if (progress == null) {
/* 50 */       throw new IllegalArgumentException();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public Writer openSource(JPackage pkg, String fileName) throws IOException {
/* 56 */     report(pkg, fileName);
/* 57 */     return super.openSource(pkg, fileName);
/*    */   }
/*    */   
/*    */   public OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
/* 61 */     report(pkg, fileName);
/* 62 */     return super.openBinary(pkg, fileName);
/*    */   }
/*    */   
/*    */   private void report(JPackage pkg, String fileName) {
/* 66 */     String name = pkg.name().replace('.', File.separatorChar);
/* 67 */     if (name.length() != 0) name = name + File.separatorChar; 
/* 68 */     name = name + fileName;
/*    */     
/* 70 */     if (this.progress.isCanceled())
/* 71 */       throw new AbortException(); 
/* 72 */     this.progress.generatedFile(name, this.current++, this.totalFileCount);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\ProgressCodeWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */