/*     */ package com.sun.tools.doclets.formats.html.markup;
/*     */ 
/*     */ import com.sun.tools.doclets.internal.toolkit.Content;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletAbortException;
/*     */ import com.sun.tools.doclets.internal.toolkit.util.DocletConstants;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
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
/*     */ public class StringContent
/*     */   extends Content
/*     */ {
/*     */   private StringBuilder stringContent;
/*     */   
/*     */   public StringContent() {
/*  52 */     this.stringContent = new StringBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringContent(String paramString) {
/*  61 */     this.stringContent = new StringBuilder();
/*  62 */     appendChars(paramString);
/*     */   }
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
/*     */   public void addContent(Content paramContent) {
/*  75 */     throw new DocletAbortException("not supported");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addContent(String paramString) {
/*  86 */     appendChars(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  94 */     return (this.stringContent.length() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int charCount() {
/*  99 */     return RawHtml.charCount(this.stringContent.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 107 */     return this.stringContent.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean write(Writer paramWriter, boolean paramBoolean) throws IOException {
/* 115 */     String str = this.stringContent.toString();
/* 116 */     paramWriter.write(str);
/* 117 */     return str.endsWith(DocletConstants.NL);
/*     */   }
/*     */   
/*     */   private void appendChars(String paramString) {
/* 121 */     for (byte b = 0; b < paramString.length(); b++) {
/* 122 */       char c = paramString.charAt(b);
/* 123 */       switch (c) { case '<':
/* 124 */           this.stringContent.append("&lt;"); break;
/* 125 */         case '>': this.stringContent.append("&gt;"); break;
/* 126 */         case '&': this.stringContent.append("&amp;"); break;
/* 127 */         default: this.stringContent.append(c);
/*     */           break; }
/*     */     
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\markup\StringContent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */