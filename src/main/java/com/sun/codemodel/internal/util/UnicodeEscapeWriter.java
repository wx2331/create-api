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
/*    */ public class UnicodeEscapeWriter
/*    */   extends FilterWriter
/*    */ {
/*    */   public UnicodeEscapeWriter(Writer next) {
/* 45 */     super(next);
/*    */   }
/*    */   
/*    */   public final void write(int ch) throws IOException {
/* 49 */     if (!requireEscaping(ch)) { this.out.write(ch); }
/*    */     else
/*    */     
/* 52 */     { this.out.write("\\u");
/* 53 */       String s = Integer.toHexString(ch);
/* 54 */       for (int i = s.length(); i < 4; i++)
/* 55 */         this.out.write(48); 
/* 56 */       this.out.write(s); }
/*    */   
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean requireEscaping(int ch) {
/* 65 */     if (ch >= 128) return true;
/*    */ 
/*    */     
/* 68 */     if (ch < 32 && " \t\r\n".indexOf(ch) == -1) return true;
/*    */     
/* 70 */     return false;
/*    */   }
/*    */   
/*    */   public final void write(char[] buf, int off, int len) throws IOException {
/* 74 */     for (int i = 0; i < len; i++)
/* 75 */       write(buf[off + i]); 
/*    */   }
/*    */   
/*    */   public final void write(char[] buf) throws IOException {
/* 79 */     write(buf, 0, buf.length);
/*    */   }
/*    */   
/*    */   public final void write(String buf, int off, int len) throws IOException {
/* 83 */     write(buf.toCharArray(), off, len);
/*    */   }
/*    */   
/*    */   public final void write(String buf) throws IOException {
/* 87 */     write(buf.toCharArray(), 0, buf.length());
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\interna\\util\UnicodeEscapeWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */