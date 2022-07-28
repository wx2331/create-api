/*    */ package com.sun.codemodel.internal.fmt;
/*    */ 
/*    */ import com.sun.codemodel.internal.JResourceFile;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.io.OutputStreamWriter;
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
/*    */ 
/*    */ public class JTextFile
/*    */   extends JResourceFile
/*    */ {
/*    */   private String contents;
/*    */   
/*    */   public JTextFile(String name) {
/* 45 */     super(name);
/*    */ 
/*    */     
/* 48 */     this.contents = null;
/*    */   }
/*    */   public void setContents(String _contents) {
/* 51 */     this.contents = _contents;
/*    */   }
/*    */   
/*    */   public void build(OutputStream out) throws IOException {
/* 55 */     Writer w = new OutputStreamWriter(out);
/* 56 */     w.write(this.contents);
/* 57 */     w.close();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\fmt\JTextFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */