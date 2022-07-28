/*    */ package com.sun.codemodel.internal.writer;
/*    */ 
/*    */ import com.sun.codemodel.internal.CodeWriter;
/*    */ import com.sun.codemodel.internal.JPackage;
/*    */ import java.io.FilterOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.io.PrintStream;
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
/*    */ public class SingleStreamCodeWriter
/*    */   extends CodeWriter
/*    */ {
/*    */   private final PrintStream out;
/*    */   
/*    */   public SingleStreamCodeWriter(OutputStream os) {
/* 55 */     this.out = new PrintStream(os);
/*    */   }
/*    */   
/*    */   public OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
/* 59 */     String pkgName = pkg.name();
/* 60 */     if (pkgName.length() != 0) pkgName = pkgName + '.';
/*    */     
/* 62 */     this.out.println("-----------------------------------" + pkgName + fileName + "-----------------------------------");
/*    */ 
/*    */ 
/*    */     
/* 66 */     return new FilterOutputStream(this.out)
/*    */       {
/*    */         public void close() {}
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 74 */     this.out.close();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\writer\SingleStreamCodeWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */