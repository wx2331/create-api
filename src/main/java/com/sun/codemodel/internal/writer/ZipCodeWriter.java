/*    */ package com.sun.codemodel.internal.writer;
/*    */ 
/*    */ import com.sun.codemodel.internal.CodeWriter;
/*    */ import com.sun.codemodel.internal.JPackage;
/*    */ import java.io.FilterOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.util.zip.ZipEntry;
/*    */ import java.util.zip.ZipOutputStream;
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
/*    */ public class ZipCodeWriter
/*    */   extends CodeWriter
/*    */ {
/*    */   private final ZipOutputStream zip;
/*    */   private final OutputStream filter;
/*    */   
/*    */   public ZipCodeWriter(OutputStream target) {
/* 49 */     this.zip = new ZipOutputStream(target);
/*    */     
/* 51 */     this.filter = new FilterOutputStream(this.zip)
/*    */       {
/*    */         public void close() {}
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
/* 61 */     String name = fileName;
/* 62 */     if (!pkg.isUnnamed()) name = toDirName(pkg) + name;
/*    */     
/* 64 */     this.zip.putNextEntry(new ZipEntry(name));
/* 65 */     return this.filter;
/*    */   }
/*    */ 
/*    */   
/*    */   private static String toDirName(JPackage pkg) {
/* 70 */     return pkg.name().replace('.', '/') + '/';
/*    */   }
/*    */   
/*    */   public void close() throws IOException {
/* 74 */     this.zip.close();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\writer\ZipCodeWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */