/*    */ package com.sun.tools.internal.ws.wscompile;
/*    */ 
/*    */ import com.sun.codemodel.internal.JPackage;
/*    */ import com.sun.codemodel.internal.writer.FileCodeWriter;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
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
/*    */ public class WSCodeWriter
/*    */   extends FileCodeWriter
/*    */ {
/*    */   private final Options options;
/*    */   
/*    */   public WSCodeWriter(File outDir, Options options) throws IOException {
/* 45 */     super(outDir, options.encoding);
/* 46 */     this.options = options;
/*    */   }
/*    */   
/*    */   protected File getFile(JPackage pkg, String fileName) throws IOException {
/* 50 */     File f = super.getFile(pkg, fileName);
/*    */     
/* 52 */     this.options.addGeneratedFile(f);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 58 */     return f;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wscompile\WSCodeWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */