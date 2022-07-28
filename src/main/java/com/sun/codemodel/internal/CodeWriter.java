/*     */ package com.sun.codemodel.internal;
/*     */ 
/*     */ import com.sun.codemodel.internal.util.EncoderFactory;
/*     */ import com.sun.codemodel.internal.util.UnicodeEscapeWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.CharsetEncoder;
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
/*     */ 
/*     */ public abstract class CodeWriter
/*     */ {
/*  50 */   protected String encoding = null;
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
/*     */   public abstract OutputStream openBinary(JPackage paramJPackage, String paramString) throws IOException;
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
/*     */   public Writer openSource(JPackage pkg, String fileName) throws IOException {
/*  87 */     final OutputStreamWriter bw = (this.encoding != null) ? new OutputStreamWriter(openBinary(pkg, fileName), this.encoding) : new OutputStreamWriter(openBinary(pkg, fileName));
/*     */ 
/*     */     
/*     */     try {
/*  91 */       return (Writer)new UnicodeEscapeWriter(bw)
/*     */         {
/*     */           
/*  94 */           private final CharsetEncoder encoder = EncoderFactory.createEncoder(bw.getEncoding());
/*     */ 
/*     */           
/*     */           protected boolean requireEscaping(int ch) {
/*  98 */             if (ch < 32 && " \t\r\n".indexOf(ch) == -1) return true;
/*     */             
/* 100 */             if (ch < 128) return false;
/*     */             
/* 102 */             return !this.encoder.canEncode((char)ch);
/*     */           }
/*     */         };
/* 105 */     } catch (Throwable t) {
/* 106 */       return (Writer)new UnicodeEscapeWriter(bw);
/*     */     } 
/*     */   }
/*     */   
/*     */   public abstract void close() throws IOException;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\CodeWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */