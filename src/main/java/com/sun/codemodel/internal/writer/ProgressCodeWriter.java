/*    */ package com.sun.codemodel.internal.writer;
/*    */ 
/*    */ import com.sun.codemodel.internal.CodeWriter;
/*    */ import com.sun.codemodel.internal.JPackage;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.io.PrintStream;
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
/*    */ 
/*    */ 
/*    */ public class ProgressCodeWriter
/*    */   extends FilterCodeWriter
/*    */ {
/*    */   private final PrintStream progress;
/*    */   
/*    */   public ProgressCodeWriter(CodeWriter output, PrintStream progress) {
/* 46 */     super(output);
/* 47 */     this.progress = progress;
/* 48 */     if (progress == null) {
/* 49 */       throw new IllegalArgumentException();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
/* 55 */     report(pkg, fileName);
/* 56 */     return super.openBinary(pkg, fileName);
/*    */   }
/*    */   
/*    */   public Writer openSource(JPackage pkg, String fileName) throws IOException {
/* 60 */     report(pkg, fileName);
/* 61 */     return super.openSource(pkg, fileName);
/*    */   }
/*    */   
/*    */   private void report(JPackage pkg, String fileName) {
/* 65 */     if (pkg.isUnnamed()) { this.progress.println(fileName); }
/*    */     else
/* 67 */     { this.progress.println(pkg
/* 68 */           .name().replace('.', File.separatorChar) + File.separatorChar + fileName); }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\writer\ProgressCodeWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */