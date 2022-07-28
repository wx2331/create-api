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
/*     */ public class DocType
/*     */   extends Content
/*     */ {
/*     */   private String docType;
/*  48 */   public static final DocType TRANSITIONAL = new DocType("Transitional", "http://www.w3.org/TR/html4/loose.dtd");
/*     */ 
/*     */   
/*  51 */   public static final DocType FRAMESET = new DocType("Frameset", "http://www.w3.org/TR/html4/frameset.dtd");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DocType(String paramString1, String paramString2) {
/*  60 */     this.docType = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 " + paramString1 + "//EN\" \"" + paramString2 + "\">" + DocletConstants.NL;
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
/*  73 */     throw new DocletAbortException("not supported");
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
/*  85 */     throw new DocletAbortException("not supported");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  92 */     return (this.docType.length() == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean write(Writer paramWriter, boolean paramBoolean) throws IOException {
/* 100 */     paramWriter.write(this.docType);
/* 101 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\doclets\formats\html\markup\DocType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */