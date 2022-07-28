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
/*     */ public class RawHtml
/*     */   extends Content
/*     */ {
/*     */   private String rawHtmlContent;
/*  48 */   public static final Content nbsp = new RawHtml("&nbsp;");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RawHtml(String paramString) {
/*  56 */     this.rawHtmlContent = (String)nullCheck(paramString);
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
/*     */   public void addContent(Content paramContent) {
/*  68 */     throw new DocletAbortException("not supported");
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
/*     */   public void addContent(String paramString) {
/*  80 */     throw new DocletAbortException("not supported");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  87 */     return this.rawHtmlContent.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  95 */     return this.rawHtmlContent;
/*     */   }
/*     */   
/*  98 */   private enum State { TEXT, ENTITY, TAG, STRING; }
/*     */ 
/*     */   
/*     */   public int charCount() {
/* 102 */     return charCount(this.rawHtmlContent);
/*     */   }
/*     */   
/*     */   static int charCount(String paramString) {
/* 106 */     State state = State.TEXT;
/* 107 */     byte b1 = 0;
/* 108 */     for (byte b2 = 0; b2 < paramString.length(); b2++) {
/* 109 */       char c = paramString.charAt(b2);
/* 110 */       switch (state) {
/*     */         case TEXT:
/* 112 */           switch (c) {
/*     */             case '<':
/* 114 */               state = State.TAG;
/*     */               break;
/*     */             case '&':
/* 117 */               state = State.ENTITY;
/* 118 */               b1++;
/*     */               break;
/*     */           } 
/* 121 */           b1++;
/*     */           break;
/*     */ 
/*     */         
/*     */         case ENTITY:
/* 126 */           if (!Character.isLetterOrDigit(c)) {
/* 127 */             state = State.TEXT;
/*     */           }
/*     */           break;
/*     */         case TAG:
/* 131 */           switch (c) {
/*     */             case '"':
/* 133 */               state = State.STRING;
/*     */               break;
/*     */             case '>':
/* 136 */               state = State.TEXT;
/*     */               break;
/*     */           } 
/*     */           
/*     */           break;
/*     */         case STRING:
/* 142 */           switch (c) {
/*     */             case '"':
/* 144 */               state = State.TAG; break;
/*     */           } 
/*     */           break;
/*     */       } 
/*     */     } 
/* 149 */     return b1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean write(Writer paramWriter, boolean paramBoolean) throws IOException {
/* 157 */     paramWriter.write(this.rawHtmlContent);
/* 158 */     return this.rawHtmlContent.endsWith(DocletConstants.NL);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\markup\RawHtml.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */