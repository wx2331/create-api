/*    */ package com.sun.codemodel.internal.fmt;
/*    */ 
/*    */ import com.sun.codemodel.internal.JResourceFile;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
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
/*    */ public final class JBinaryFile
/*    */   extends JResourceFile
/*    */ {
/* 43 */   private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*    */   
/*    */   public JBinaryFile(String name) {
/* 46 */     super(name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public OutputStream getDataStore() {
/* 56 */     return this.baos;
/*    */   }
/*    */   
/*    */   public void build(OutputStream os) throws IOException {
/* 60 */     os.write(this.baos.toByteArray());
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\fmt\JBinaryFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */