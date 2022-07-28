/*    */ package com.sun.codemodel.internal.util;
/*    */ 
/*    */ import java.io.FilterWriter;
/*    */ import java.io.IOException;
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
/*    */ public class JavadocEscapeWriter
/*    */   extends FilterWriter
/*    */ {
/*    */   public JavadocEscapeWriter(Writer next) {
/* 55 */     super(next);
/*    */   }
/*    */   
/*    */   public void write(int ch) throws IOException {
/* 59 */     if (ch == 60) {
/* 60 */       this.out.write("&lt;");
/*    */     }
/* 62 */     else if (ch == 38) {
/* 63 */       this.out.write("&amp;");
/*    */     } else {
/* 65 */       this.out.write(ch);
/*    */     } 
/*    */   }
/*    */   public void write(char[] buf, int off, int len) throws IOException {
/* 69 */     for (int i = 0; i < len; i++)
/* 70 */       write(buf[off + i]); 
/*    */   }
/*    */   
/*    */   public void write(char[] buf) throws IOException {
/* 74 */     write(buf, 0, buf.length);
/*    */   }
/*    */   
/*    */   public void write(String buf, int off, int len) throws IOException {
/* 78 */     write(buf.toCharArray(), off, len);
/*    */   }
/*    */   
/*    */   public void write(String buf) throws IOException {
/* 82 */     write(buf.toCharArray(), 0, buf.length());
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\interna\\util\JavadocEscapeWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */